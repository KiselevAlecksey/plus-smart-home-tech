package ru.yandex.practicum.commerce.shoppingstore.product;

import org.mapstruct.*;
import ru.yandex.practicum.commerce.interactionapi.dto.product.*;
import ru.yandex.practicum.commerce.shoppingstore.product.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageSrc", source = "imageSrc")
    @Mapping(target = "quantityState", source = "quantityState")
    @Mapping(target = "productState", source = "productState")
    @Mapping(target = "productCategory", source = "productCategory")
    @Mapping(target = "price", source = "price")
    ProductFullResponseDto toResponseDto(Product product);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageSrc", source = "imageSrc")
    @Mapping(target = "quantityState", source = "quantityState")
    @Mapping(target = "productState", source = "productState")
    @Mapping(target = "productCategory", source = "productCategory")
    @Mapping(target = "price", source = "price")
    Product toEntity(ProductFullDto dto);

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageSrc", source = "imageSrc")
    @Mapping(target = "quantityState", source = "quantityState")
    @Mapping(target = "productState", source = "productState")
    @Mapping(target = "productCategory", source = "productCategory")
    @Mapping(target = "price", source = "price")
    Product toEntityFromCreate(ProductCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageSrc", source = "imageSrc")
    @Mapping(target = "quantityState", source = "quantityState")
    @Mapping(target = "productState", source = "productState")
    @Mapping(target = "productCategory", source = "productCategory")
    @Mapping(target = "price", source = "price")
    void updateProductFromDto(ProductUpdateDto dto, @MappingTarget Product entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "productName", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "imageSrc", ignore = true)
    @Mapping(target = "productState", ignore = true)
    @Mapping(target = "productCategory", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "quantityState", source = "quantityState")
    void updateQuantityStateFromDto(ProductQuantityStateRequest stateRequest, @MappingTarget Product entity);
}
