package ru.yandex.practicum.commerce.shoppingcart.cart;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.ProductResponseDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductQuantityDto;
import ru.yandex.practicum.commerce.shoppingcart.cart.product.CartProduct;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponseDto toResponseProductDto(CartProduct product);

    @Mapping(target = "shoppingCart", ignore = true)
    CartProduct toEntityProduct(ProductResponseDto dto);

    @Mapping(target = "shoppingCart", ignore = true)
    CartProduct toEntityProduct(ProductDto dto);

    @Mapping(target = "quantity", source = "newQuantity")
    @Mapping(target = "shoppingCart", ignore = true)
    CartProduct toEntityFromChangeDto(ProductQuantityDto dto);

    @Mapping(target = "newQuantity", source = "quantity")
    ProductQuantityDto toDto(CartProduct product);
}
