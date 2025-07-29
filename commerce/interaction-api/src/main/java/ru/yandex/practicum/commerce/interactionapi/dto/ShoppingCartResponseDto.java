package ru.yandex.practicum.commerce.interactionapi.dto;

import java.util.Set;
import java.util.UUID;

public record ShoppingCartResponseDto(
    UUID shoppingCartId,
    Set<ProductResponseDto> products
) {
}
