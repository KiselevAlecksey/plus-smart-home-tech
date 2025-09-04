package ru.yandex.practicum.commerce.shoppingcart.cart;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartResponseDto;
import ru.yandex.practicum.commerce.shoppingcart.cart.product.CartProduct;


import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShoppingCartMapper {

    @Mapping(target = "products", expression = "java(mapCartProductsToMap(shoppingCart.getProducts()))")
    ShoppingCartResponseDto toResponseDto(ShoppingCart shoppingCart);

    @Mapping(target = "products", expression = "java(mapProductMapToCartProducts(dto.products()))")
    ShoppingCart toEntity(ShoppingCartResponseDto dto);

    default Map<UUID, Long> mapCartProductsToMap(Set<CartProduct> cartProducts) {
        if (cartProducts == null) return Map.of();
        return cartProducts.stream()
                .collect(Collectors.toMap(
                        CartProduct::getProductId,
                        CartProduct::getQuantity
                ));
    }

    default Set<CartProduct> mapProductMapToCartProducts(Map<UUID, Long> productMap) {
        return productMap.entrySet().stream()
                .map(entry -> {
                    CartProduct cartProduct = new CartProduct();
                    cartProduct.setProductId(entry.getKey());
                    cartProduct.setQuantity(entry.getValue());
                    return cartProduct;
                })
                .collect(Collectors.toSet());
    }
}
