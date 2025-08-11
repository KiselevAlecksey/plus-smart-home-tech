package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import java.util.UUID;

public record ShippedToDeliveryRequest(
        UUID orderId,
        UUID deliveryId
) {
}
