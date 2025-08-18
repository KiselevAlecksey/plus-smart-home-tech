package ru.yandex.practicum.commerce.warehouse.warehouse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.NewProductInWarehouseRequestDto;

@Mapper(componentModel = "spring")
public interface ProductInWarehouseMapper {

    @Mapping(target = "id", source = "productId")
    @Mapping(target = "width", source = "dimension.width")
    @Mapping(target = "height", source = "dimension.height")
    @Mapping(target = "depth", source = "dimension.depth")
    @Mapping(target = "quantity", constant = "0L")
    ProductInWarehouse toEntity(NewProductInWarehouseRequestDto request);

    @Mapping(target = "fragile", ignore = true)
    @Mapping(target = "width", ignore = true)
    @Mapping(target = "height", ignore = true)
    @Mapping(target = "depth", ignore = true)
    @Mapping(target = "weight", ignore = true)
    ProductInWarehouse toEntity(ProductDto response);
}
