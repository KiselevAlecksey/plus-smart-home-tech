package ru.yandex.practicum.commerce.interactionapi.enums;

import java.util.Optional;

public enum OrderState {
    NEW,
    ON_PAYMENT,
    ON_DELIVERY,
    DONE,
    DELIVERED,
    ASSEMBLED,
    PAID,
    COMPLETED,
    DELIVERY_FAILED,
    ASSEMBLY_FAILED,
    PAYMENT_FAILED,
    PRODUCT_RETURNED,
    CANCELED;

    public static Optional<OrderState> from(String category) {
        for (OrderState value : OrderState.values()) {
            if (value.name().equals(category)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
