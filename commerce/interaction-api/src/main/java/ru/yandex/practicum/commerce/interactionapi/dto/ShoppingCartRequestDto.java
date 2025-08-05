package ru.yandex.practicum.commerce.interactionapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;

import java.util.Set;
import java.util.UUID;

@Builder
public record ShoppingCartRequestDto(
    @NotNull UUID shoppingCartId,
    Set<ProductDto> products
) {
}
