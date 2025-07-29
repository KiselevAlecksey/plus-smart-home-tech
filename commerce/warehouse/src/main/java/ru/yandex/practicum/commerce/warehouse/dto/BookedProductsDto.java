package ru.yandex.practicum.commerce.warehouse.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record BookedProductsDto(
        double deliveryWeight,
        double deliveryVolume,
        boolean fragile
) {
}
