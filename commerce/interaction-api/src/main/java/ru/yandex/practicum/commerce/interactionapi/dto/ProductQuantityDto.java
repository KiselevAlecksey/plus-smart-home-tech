package ru.yandex.practicum.commerce.interactionapi.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductQuantityDto(
    @NotNull UUID productId,
    long newQuantity
) {
}
