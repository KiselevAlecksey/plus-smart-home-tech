package ru.yandex.practicum.commerce.shoppingcart.cart;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.interactionapi.dto.ProductResponseDto;
import ru.yandex.practicum.commerce.interactionapi.dto.ProductQuantityDto;
import ru.yandex.practicum.commerce.shoppingcart.cart.product.CartProduct;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "quantity", ignore = true)
    ProductResponseDto toResponseProductDto(CartProduct product);

    @Mapping(target = "shoppingCart", ignore = true)
    CartProduct toEntityProduct(ProductResponseDto dto);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "quantity", source = "newQuantity")
    CartProduct toEntityFromChangeDto(ProductQuantityDto dto);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "newQuantity", source = "quantity")
    ProductQuantityDto toDto(CartProduct product);
}
