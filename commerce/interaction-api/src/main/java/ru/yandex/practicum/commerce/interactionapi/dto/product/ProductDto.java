package ru.yandex.practicum.commerce.interactionapi.dto.product;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductDto(
        UUID id,
        long quantity
) {
}
