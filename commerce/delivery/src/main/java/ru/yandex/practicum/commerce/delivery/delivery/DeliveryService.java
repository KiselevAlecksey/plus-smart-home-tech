package ru.yandex.practicum.commerce.delivery.delivery;

import ru.yandex.practicum.commerce.interactionapi.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {
    DeliveryDto planDelivery(DeliveryDto deliveryDto);
    void deliverySuccessful(UUID deliveryId);
    void deliveryPicked(UUID deliveryId);
    void deliveryFailed(UUID deliveryId);
    BigDecimal deliveryCost(OrderDto orderDto);
}
