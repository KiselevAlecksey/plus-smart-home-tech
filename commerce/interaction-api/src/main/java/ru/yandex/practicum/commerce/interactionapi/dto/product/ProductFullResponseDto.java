package ru.yandex.practicum.commerce.interactionapi.dto.product;



import ru.yandex.practicum.commerce.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.commerce.interactionapi.enums.ProductState;
import ru.yandex.practicum.commerce.interactionapi.enums.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductFullResponseDto(
    UUID productId,
    String productName,
    String description,
    String imageSrc,
    QuantityState quantityState,
    ProductState productState,
    ProductCategory productCategory,
        BigDecimal price
) {
}
