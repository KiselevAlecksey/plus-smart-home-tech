package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Builder(toBuilder = true)
public record BookedProductsDto(
        double deliveryWeight,
        double deliveryVolume,
        boolean fragile
) {
}
