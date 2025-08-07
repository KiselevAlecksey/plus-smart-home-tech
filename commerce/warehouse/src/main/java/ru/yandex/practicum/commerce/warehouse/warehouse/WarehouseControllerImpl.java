package ru.yandex.practicum.commerce.warehouse.warehouse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.annotation.RestLogging;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.NewProductInWarehouseRequestDto;
import ru.yandex.practicum.commerce.interactionapi.feign.WarehouseController;

import static ru.yandex.practicum.commerce.interactionapi.Util.X_REQUEST_ID_HEADER;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseControllerImpl implements WarehouseController {
    private final WarehouseService warehouseService;

    @Override
    @PutMapping
    @RestLogging
    @ResponseStatus(HttpStatus.CREATED)
    public void addProductToWarehouse(@RequestBody NewProductInWarehouseRequestDto request) {
        warehouseService.addNewProductToWarehouse(request);
    }

    @Override
    @PostMapping("/check")
    @RestLogging
    public BookedProductsDto checkProductQuantityForShoppingCart(
            @RequestHeader(X_REQUEST_ID_HEADER) String headerValue,
            @RequestBody ShoppingCartRequestDto request
    ) {
        return warehouseService.checkProductQuantityForShoppingCart(request);
    }

    @Override
    @PostMapping("/add")
    @RestLogging
    public void addProductInstanceToWarehouse(@RequestBody AddProductToWarehouseRequest request) {
        warehouseService.addProductInstanceToWarehouse(request);
    }

    @Override
    @GetMapping("/address")
    @RestLogging
    public AddressDto getAddressWarehouse() {
        return warehouseService.getAddressWarehouse();
    }
}
