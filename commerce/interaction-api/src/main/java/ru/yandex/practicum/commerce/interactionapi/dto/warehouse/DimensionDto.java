package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import jakarta.validation.constraints.Min;

public record DimensionDto(
        @Min(1) double width,
        @Min(1) double height,
        @Min(1) double depth
) {
}
