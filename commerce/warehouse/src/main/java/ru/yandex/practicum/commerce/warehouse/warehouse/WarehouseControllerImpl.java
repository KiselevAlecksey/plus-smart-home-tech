package ru.yandex.practicum.commerce.warehouse.warehouse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.aspect.RestLogging;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.*;
import ru.yandex.practicum.commerce.interactionapi.feign.WarehouseController;

import java.util.Set;

import static ru.yandex.practicum.commerce.interactionapi.Util.X_REQUEST_ID_HEADER;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseControllerImpl implements WarehouseController {
    private final WarehouseService warehouseService;

    @Override
    public void addProductToWarehouse(@RequestBody NewProductInWarehouseRequestDto request) {
        warehouseService.addNewProductToWarehouse(request);
    }

    @Override
    public BookedProductsDto checkProductQuantityForShoppingCart(
            @RequestHeader(X_REQUEST_ID_HEADER) String headerValue,
            @RequestBody ShoppingCartRequestDto request
    ) {
        return warehouseService.checkProductQuantityForShoppingCart(request);
    }

    @Override
    public void addProductInstanceToWarehouse(@RequestBody AddProductToWarehouseRequest request) {
        warehouseService.addProductInstanceToWarehouse(request);
    }

    @Override
    public AddressDto getAddressWarehouse() {
        return warehouseService.getAddressWarehouse();
    }

    @Override
    public BookedProductsDto assembly(AssemblyProductsForOrderRequest request) {
        return warehouseService.assembly(request);
    }

    @Override
    public void returnOrder(Set<ProductDto> productDtos) {
        warehouseService.returnOrder(productDtos);
    }

    @Override
    public void shipToDelivery(ShippedToDeliveryRequest request) {
        warehouseService.shipToDelivery(request);
    }
}
