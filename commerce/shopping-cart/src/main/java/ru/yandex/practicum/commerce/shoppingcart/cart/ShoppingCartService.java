package ru.yandex.practicum.commerce.shoppingcart.cart;


import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductQuantityDto;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartResponseDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartResponseDto getShoppingCartByUserName(String userName);

    ShoppingCartResponseDto addProductsToShoppingCart(String userName, Map<UUID, Long> products);

    void removeShoppingCart(String userName);

    ShoppingCartResponseDto removeShoppingCartProducts(String userName, Set<UUID> products);

    ShoppingCartResponseDto changeProductQuantity(String userName, ProductQuantityDto changeQuantity);
}
