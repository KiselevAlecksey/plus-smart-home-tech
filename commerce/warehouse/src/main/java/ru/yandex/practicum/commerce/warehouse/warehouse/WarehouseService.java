package ru.yandex.practicum.commerce.warehouse.warehouse;

import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.*;

import java.util.Set;

public interface WarehouseService {
    void addNewProductToWarehouse(NewProductInWarehouseRequestDto request);

    BookedProductsDto checkProductQuantityForShoppingCart(ShoppingCartRequestDto request);

    void addProductInstanceToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getAddressWarehouse();

    BookedProductsDto assembly(AssemblyProductsForOrderRequest request);

    void returnOrder(Set<ProductDto> productDtos);

    void shipToDelivery(ShippedToDeliveryRequest request);
}
