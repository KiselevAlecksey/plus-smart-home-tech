package ru.yandex.practicum.commerce.warehouse.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddProductToWarehouseRequest(
        @NotNull UUID productId,
        @Min(1) long quantity
) {
}
