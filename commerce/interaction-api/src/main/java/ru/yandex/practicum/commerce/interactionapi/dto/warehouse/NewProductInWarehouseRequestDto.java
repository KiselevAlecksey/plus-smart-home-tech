package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.DimensionDto;

import java.math.BigDecimal;
import java.util.UUID;

public record NewProductInWarehouseRequestDto(
        UUID productId,
        boolean fragile,
        DimensionDto dimension,
        @Min(1) double weight,

        @NotNull
        @DecimalMin("1.0")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal price
) {
}
