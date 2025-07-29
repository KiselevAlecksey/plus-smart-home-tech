package ru.yandex.practicum.commerce.shoppingstore.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Товар, продаваемый в интернет-магазине")
public record ProductResponseDto(
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
