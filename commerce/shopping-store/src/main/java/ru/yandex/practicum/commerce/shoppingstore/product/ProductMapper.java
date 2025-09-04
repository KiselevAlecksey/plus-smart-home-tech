package ru.yandex.practicum.commerce.shoppingstore.product;

import org.mapstruct.*;
import ru.yandex.practicum.commerce.interactionapi.dto.product.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "productId", source = "id")
    ProductFullResponseDto toResponseDto(Product product);

    Product toEntity(ProductFullDto dto);

    @Mapping(target = "id", ignore = true)
    Product toEntityFromCreate(ProductCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageSrc", source = "imageSrc")
    @Mapping(target = "quantityState", source = "quantityState")
    @Mapping(target = "productState", source = "productState")
    @Mapping(target = "productCategory", source = "productCategory")
    @Mapping(target = "price", source = "price")
    void updateProductFromDto(ProductUpdateDto dto, @MappingTarget Product entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quantityState", source = "stateRequest.quantityState")
    @Mapping(target = "productName", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "imageSrc", ignore = true)
    @Mapping(target = "productState", ignore = true)
    @Mapping(target = "productCategory", ignore = true)
    @Mapping(target = "price", ignore = true)
    void updateQuantityStateFromDto(ProductQuantityStateRequest stateRequest, @MappingTarget Product entity);
}