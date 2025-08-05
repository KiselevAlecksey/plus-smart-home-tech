package ru.yandex.practicum.commerce.interactionapi.dto;


import java.util.Map;
import java.util.UUID;

public record ShoppingCartResponseDto(
    UUID shoppingCartId,
    Map<UUID, Long> products
) {
}
