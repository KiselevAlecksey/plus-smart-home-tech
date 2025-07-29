package ru.yandex.practicum.commerce.interactionapi.dto;

import java.util.UUID;

public record ProductDto(
        UUID productId,
        long quantity
) {
}
