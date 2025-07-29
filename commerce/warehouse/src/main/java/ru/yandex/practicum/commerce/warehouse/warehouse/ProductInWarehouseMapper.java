package ru.yandex.practicum.commerce.warehouse.warehouse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.interactionapi.dto.ProductDto;
import ru.yandex.practicum.commerce.warehouse.dto.NewProductInWarehouseRequestDto;

@Mapper(componentModel = "spring")
public interface ProductInWarehouseMapper {

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "fragile", source = "fragile")
    @Mapping(target = "width", source = "dimension.width")
    @Mapping(target = "height", source = "dimension.height")
    @Mapping(target = "depth", source = "dimension.depth")
    @Mapping(target = "weight", source = "weight")
    @Mapping(target = "quantity", constant = "0L")
    ProductInWarehouse toEntity(NewProductInWarehouseRequestDto request);

    @Mapping(target = "productId", source = "response.productId")
    @Mapping(target = "quantity", source = "response.quantity")
    @Mapping(target = "fragile", ignore = true)
    @Mapping(target = "width", ignore = true)
    @Mapping(target = "height", ignore = true)
    @Mapping(target = "depth", ignore = true)
    @Mapping(target = "weight", ignore = true)
    ProductInWarehouse toEntity(ProductDto response);
}
