package ru.yandex.practicum.commerce.interactionapi.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record ShoppingCartRequestDto(
    @NotNull UUID shoppingCartId,
    Set<ProductDto> products
) {
}
