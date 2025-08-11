package ru.yandex.practicum.commerce.interactionapi.dto.order;

import lombok.Builder;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.BookedProductsDto;

@Builder
public record CreateNewOrderRequest(
        ShoppingCartRequestDto shoppingCart,
        AddressDto deliveryAddress,
        BookedProductsDto bookedProducts,
        String userName
) {
}
