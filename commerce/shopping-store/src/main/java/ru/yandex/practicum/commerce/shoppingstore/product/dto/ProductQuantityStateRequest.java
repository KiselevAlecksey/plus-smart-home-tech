package ru.yandex.practicum.commerce.shoppingstore.product.dto;

import lombok.Builder;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.QuantityState;

import java.util.UUID;

@Builder
public record ProductQuantityStateRequest(
        UUID productId,
        QuantityState quantityState
) {
}
