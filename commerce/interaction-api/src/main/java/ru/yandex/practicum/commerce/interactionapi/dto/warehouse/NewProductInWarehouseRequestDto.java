package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import jakarta.validation.constraints.Min;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.DimensionDto;

import java.util.UUID;

public record NewProductInWarehouseRequestDto(
        UUID productId,
        boolean fragile,
        DimensionDto dimension,
        @Min(1) double weight
) {
}
