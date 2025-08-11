package ru.yandex.practicum.commerce.delivery.delivery;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.enums.DeliveryState;
import ru.yandex.practicum.commerce.interactionapi.exception.NoDeliveryFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static ru.yandex.practicum.commerce.interactionapi.Util.ADDRESSES;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryServiceImpl implements DeliveryService {
    public static final int ADDRESS_COEFFICIENT1 = 1;
    public static final int ADDRESS_COEFFICIENT2 = 2;
    public static final double FRAGILE_COEFFICIENT = 0.2;
    public static final double WEIGHT_COEFFICIENT = 0.3;
    public static final double VOLUME_COEFFICIENT = 0.2;
    public static final double ADDRESS_TO_COEFFICIENT = 0.2;
    AddressRepository addressRepository;
    DeliveryRepository deliveryRepository;
    DeliveryMapper deliveryMapper;
    BigDecimal baseCostDelivery = BigDecimal.valueOf(5.0);

    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        addressRepository.saveAll(List.of(
                deliveryMapper.toToAddressEntity(deliveryDto.toAddress()),
                deliveryMapper.toFromAddressEntity(deliveryDto.fromAddress()))
        );
        return deliveryMapper.toDto(deliveryRepository.save(deliveryMapper.toEntity(deliveryDto)));
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
        deliveryRepository.save(delivery);
    }

    @Override
    public BigDecimal deliveryCost(OrderDto orderDto) {
        BigDecimal deliveryCost = baseCostDelivery;
        List<Address> addresses = addressRepository.findByDeliveryId(orderDto.deliveryId());
        Address addressFrom = addresses.stream()
                .filter(Address::getIsWarehouse)
                .findFirst()
                .orElseThrow();

        Address addressTo = addresses.stream()
                .filter(address -> !address.getIsWarehouse())
                .findFirst()
                .orElseThrow();

        if (isAddressEqualsAddress1(addressFrom)) {
            deliveryCost = deliveryCost.multiply(BigDecimal.valueOf(ADDRESS_COEFFICIENT1));
        } else {
            deliveryCost = deliveryCost.multiply(BigDecimal.valueOf(ADDRESS_COEFFICIENT2)).add(baseCostDelivery);
        }

        if (orderDto.fragile()) {
            deliveryCost = deliveryCost.multiply(BigDecimal.valueOf(FRAGILE_COEFFICIENT)).add(deliveryCost);
        }

        deliveryCost = deliveryCost.multiply(
                BigDecimal.valueOf(orderDto.deliveryWeight() * WEIGHT_COEFFICIENT))
                .add(deliveryCost)
                .multiply(BigDecimal.valueOf(orderDto.deliveryVolume() * VOLUME_COEFFICIENT));

        return isAddressEqualsAddress1(addressTo) ? deliveryCost
                : deliveryCost.multiply(BigDecimal.valueOf(ADDRESS_TO_COEFFICIENT)).add(deliveryCost);
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
