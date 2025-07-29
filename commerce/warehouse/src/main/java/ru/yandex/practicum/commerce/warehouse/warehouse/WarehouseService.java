package ru.yandex.practicum.commerce.warehouse.warehouse;

import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.NewProductInWarehouseRequestDto;

public interface WarehouseService {
    void addNewProductToWarehouse(NewProductInWarehouseRequestDto request);

    BookedProductsDto checkProductQuantityForShoppingCart(ShoppingCartRequestDto request);

    void addProductInstanceToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getAddressWarehouse();
}
