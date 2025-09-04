package ru.yandex.practicum.commerce.interactionapi.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.commerce.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.commerce.interactionapi.enums.ProductState;
import ru.yandex.practicum.commerce.interactionapi.enums.QuantityState;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductCreateDto(

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

    ProductCategory productCategory,

        @NotNull
        @DecimalMin("1.0")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal price
) {
}
