package ru.yandex.practicum.commerce.interactionapi.dto.order;

import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;

import java.util.Set;
import java.util.UUID;

public record ProductReturnRequest(
        UUID orderId,
        Set<ProductDto> products
) {
}
