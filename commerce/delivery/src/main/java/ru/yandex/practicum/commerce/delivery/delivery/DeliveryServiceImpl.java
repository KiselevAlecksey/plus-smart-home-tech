package ru.yandex.practicum.commerce.delivery.delivery;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.enums.DeliveryState;
import ru.yandex.practicum.commerce.interactionapi.exception.NoDeliveryFoundException;
import ru.yandex.practicum.commerce.interactionapi.feign.OrderFeignClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static ru.yandex.practicum.commerce.interactionapi.Util.*;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryServiceImpl implements DeliveryService {
    AddressRepository addressRepository;
    DeliveryRepository deliveryRepository;
    DeliveryMapper deliveryMapper;
    OrderFeignClient orderFeignClient;

    @Override
    @Transactional
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        Address fromAddress = ensureAddressExists(deliveryDto.fromAddress());
        Address toAddress = ensureAddressExists(deliveryDto.toAddress());

        Delivery delivery = deliveryMapper.toEntity(deliveryDto);
        delivery.setFromAddress(fromAddress);
        delivery.setToAddress(toAddress);
        System.out.println(delivery);

        return deliveryMapper.toDto(deliveryRepository.saveAndFlush(delivery));
    }

    public Address ensureAddressExists(AddressDto addressDto) {
        return addressRepository.findByCountryAndCityAndStreetAndHouseAndFlat(
                addressDto.country(),
                addressDto.city(),
                addressDto.street(),
                addressDto.house(),
                addressDto.flat()
        ).orElseGet(() -> {
            Address newAddress = deliveryMapper.toToAddressEntity(addressDto);
            return addressRepository.saveAndFlush(newAddress);
        });
    }

    @Override
    public void deliverySuccessful(UUID deliveryId) {
        Delivery delivery = getDeliveryOrThrow(deliveryId);
        delivery.setState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
    }

    private Delivery getDeliveryOrThrow(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> NoDeliveryFoundException.builder()
                        .message("Ошибка при поиске доставки")
                        .userMessage("Доставка не найдена" + deliveryId)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .cause(new RuntimeException("Доставка не найдена"))
                        .build());
    }

    @Override
    public void deliveryPicked(UUID deliveryId) {
        Delivery delivery = getDeliveryOrThrow(deliveryId);
        delivery.setState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);
    }

    @Override
    public void deliveryFailed(UUID deliveryId) {
        Delivery delivery = getDeliveryOrThrow(deliveryId);
        delivery.setState(DeliveryState.FAILED);
        orderFeignClient.deliveryFailedOrder(delivery.getOrderId());
        deliveryRepository.save(delivery);
    }

    @Override
    @Cacheable(value = "cost")
    public BigDecimal deliveryCost(OrderDto orderDto) {
        BigDecimal deliveryCost = baseCostDelivery;
        Delivery delivery = getDeliveryOrThrow(orderDto.deliveryId());
        Address addressFrom = delivery.getFromAddress();
        Address addressTo = delivery.getToAddress();

        if (isAddressEqualsAddress1(addressFrom)) {
            deliveryCost = deliveryCost.multiply(BigDecimal.valueOf(ADDRESS_COEFFICIENT1))
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            deliveryCost = deliveryCost.multiply(BigDecimal.valueOf(ADDRESS_COEFFICIENT2))
                    .add(baseCostDelivery)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        if (orderDto.fragile()) {
            deliveryCost = deliveryCost.multiply(FRAGILE_COEFFICIENT)
                    .add(deliveryCost)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        deliveryCost = deliveryCost.multiply(
                orderDto.deliveryWeight()
                        .multiply(WEIGHT_COEFFICIENT)
        ).setScale(2, RoundingMode.HALF_UP);

        deliveryCost = deliveryCost.multiply(
                orderDto.deliveryVolume()
                        .multiply(VOLUME_COEFFICIENT)
        ).setScale(2, RoundingMode.HALF_UP);

        return isAddressEqualsAddress1(addressTo) ? deliveryCost
                : deliveryCost.multiply(ADDRESS_TO_COEFFICIENT).add(deliveryCost).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public UUID getDeliveryId(String orderId) {
        return deliveryRepository.findByOrderId(UUID.fromString(orderId));
    }

    private static boolean isAddressEqualsAddress1(Address addressFrom) {
        return addressFrom.getCountry().equals(ADDRESSES[0])
                || addressFrom.getCity().equals(ADDRESSES[0])
                || addressFrom.getStreet().equals(ADDRESSES[0])
                || addressFrom.getHouse().equals(ADDRESSES[0])
                || addressFrom.getFlat().equals(ADDRESSES[0]);
    }
}
