package ru.yandex.practicum.commerce.shoppingcart.cart;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.commerce.interactionapi.dto.ProductResponseDto;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartResponseDto;
import ru.yandex.practicum.commerce.shoppingcart.cart.product.CartProduct;


import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {

    ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "products", source = "products")
    ShoppingCartResponseDto toResponseDto(ShoppingCart shoppingCart);

    @Mapping(target = "products", source = "products")
    ShoppingCart toEntity(ShoppingCartResponseDto dto);

    default Set<ProductResponseDto> mapProductsToDto(Set<CartProduct> products) {
        return products.stream()
                .map(productMapper::toResponseProductDto)
                .collect(Collectors.toSet());
    }

    default Set<CartProduct> mapDtoToProducts(Set<ProductResponseDto> dtos) {
        return dtos.stream()
                .map(productMapper::toEntityProduct)
                .collect(Collectors.toSet());
    }
}
