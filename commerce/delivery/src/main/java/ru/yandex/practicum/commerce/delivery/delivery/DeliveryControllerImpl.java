package ru.yandex.practicum.commerce.delivery.delivery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interactionapi.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.feign.DeliveryController;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/delivery")
public class DeliveryControllerImpl implements DeliveryController {
    private final DeliveryService deliveryService;

    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        return deliveryService.planDelivery(deliveryDto);
    }

    @Override
    public void deliverySuccessful(UUID deliveryId) {
        deliveryService.deliverySuccessful(deliveryId);
    }

    @Override
    public void deliveryPicked(UUID deliveryId) {
        deliveryService.deliveryPicked(deliveryId);
    }

    @Override
    public void deliveryFailed(UUID deliveryId) {
        deliveryService.deliveryFailed(deliveryId);
    }

    @Override
    public BigDecimal deliveryCost(OrderDto orderDto) {
        return deliveryService.deliveryCost(orderDto);
    }

    @Override
    public UUID getDeliveryId(String orderId) {
        return deliveryService.getDeliveryId(orderId);
    }
}
