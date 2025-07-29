package ru.yandex.practicum.commerce.warehouse.warehouse;

import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.dto.AddressDto;
import ru.yandex.practicum.commerce.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.warehouse.dto.NewProductInWarehouseRequestDto;

public interface WarehouseService {
    void addNewProductToWarehouse(NewProductInWarehouseRequestDto request);

    BookedProductsDto checkProductQuantityForShoppingCart(ShoppingCartRequestDto request);

    void addProductInstanceToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getAddressWarehouse();
}
