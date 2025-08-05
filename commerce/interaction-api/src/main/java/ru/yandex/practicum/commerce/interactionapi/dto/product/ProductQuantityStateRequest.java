package ru.yandex.practicum.commerce.interactionapi.dto.product;

import lombok.Builder;
import ru.yandex.practicum.commerce.interactionapi.enums.QuantityState;

import java.util.UUID;

@Builder
public record ProductQuantityStateRequest(
        UUID productId,
        QuantityState quantityState
) {
}
