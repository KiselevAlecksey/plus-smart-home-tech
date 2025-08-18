package ru.yandex.practicum.commerce.interactionapi.dto.product;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record ProductPriceDto(
        Map<UUID, BigDecimal> productPricesMap
) {
}
