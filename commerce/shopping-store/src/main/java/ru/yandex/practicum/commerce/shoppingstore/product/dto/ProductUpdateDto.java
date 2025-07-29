package ru.yandex.practicum.commerce.shoppingstore.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Описательная часть изменяемого товара в системе")
public record ProductUpdateDto(
        @NotNull
        UUID productId,

        @NotBlank
        String productName,

        @NotBlank
        String description,

        @Nullable
        String imageSrc,

        @NotNull
        QuantityState quantityState,

        @NotNull
        ProductState productState,

        @NotNull
        ProductCategory productCategory,

        @NotNull
        @DecimalMin("1.0")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal price
) {
}
