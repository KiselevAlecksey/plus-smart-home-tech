package ru.yandex.practicum.commerce.interactionapi.dto;

import java.util.UUID;

public record ProductResponseDto(
        UUID productId,
        long quantity
) {
}
