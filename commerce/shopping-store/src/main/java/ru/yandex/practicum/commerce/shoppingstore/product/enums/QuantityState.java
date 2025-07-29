package ru.yandex.practicum.commerce.shoppingstore.product.enums;

import java.util.Optional;

public enum QuantityState {
    ENDED,
    FEW,
    ENOUGH,
    MANY;

    public static Optional<QuantityState> from(String category) {
        for (QuantityState value : QuantityState.values()) {
            if (value.name().equals(category)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
