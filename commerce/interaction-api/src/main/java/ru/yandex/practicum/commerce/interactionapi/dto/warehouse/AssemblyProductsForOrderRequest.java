package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import lombok.Builder;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;

import java.util.Set;
import java.util.UUID;

@Builder
public record AssemblyProductsForOrderRequest(
        Set<ProductDto> products,
        UUID orderId
) {
}
