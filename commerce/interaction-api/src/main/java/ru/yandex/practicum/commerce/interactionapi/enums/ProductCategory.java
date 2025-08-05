package ru.yandex.practicum.commerce.interactionapi.enums;

import java.util.Optional;

public enum ProductCategory {
    CONTROL,
    SENSORS,
    LIGHTING;

    public static Optional<ProductCategory> from(String category) {
        for (ProductCategory value : ProductCategory.values()) {
            if (value.name().equals(category)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
