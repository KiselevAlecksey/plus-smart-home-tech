package ru.yandex.practicum.commerce.interactionapi.dto.product;

import java.util.Set;
import java.util.UUID;

public record ProductIdsDto(
        Set<UUID> productIds
) {
}
