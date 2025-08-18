package ru.yandex.practicum.commerce.order.order;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.interactionapi.dto.ProductResponseDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductQuantityDto;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProductMapper {

    ProductResponseDto toResponseProductDto(CartProduct product);

    ProductDto toProductDto(CartProduct product);

    CartProduct toEntityProduct(ProductResponseDto dto);

    CartProduct toEntityProduct(ProductDto dto);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "quantity", source = "newQuantity")
    CartProduct toEntityFromChangeDto(ProductQuantityDto dto);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "newQuantity", source = "quantity")
    ProductQuantityDto toDto(CartProduct product);
}
