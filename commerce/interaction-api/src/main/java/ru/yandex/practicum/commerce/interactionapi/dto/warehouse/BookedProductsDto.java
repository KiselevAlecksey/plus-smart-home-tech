package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import lombok.Builder;

@Builder(toBuilder = true)
public record BookedProductsDto(
        double deliveryWeight,
        double deliveryVolume,
        boolean fragile
) {
}
