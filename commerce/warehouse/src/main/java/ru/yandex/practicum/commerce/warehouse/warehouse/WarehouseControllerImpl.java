package ru.yandex.practicum.commerce.warehouse.warehouse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.NewProductInWarehouseRequestDto;
import ru.yandex.practicum.commerce.interactionapi.feign.WarehouseController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseControllerImpl implements WarehouseController {
    private final WarehouseService warehouseService;

    @Override
    @PutMapping
    public void addProductToWarehouse(@RequestBody NewProductInWarehouseRequestDto request) {
        log.info("==> Add new product to warehouse {} start", request);
        warehouseService.addNewProductToWarehouse(request);
        log.info("==> Add new product to warehouse {} end", request);
    }

    @Override
    @PostMapping("/check")
    public BookedProductsDto checkProductQuantityForShoppingCart(@RequestBody ShoppingCartRequestDto request) {
        log.info("==> Check product quantity warehouse {} start", request);
        BookedProductsDto dto = warehouseService.checkProductQuantityForShoppingCart(request);
        log.info("==> Check product quantity warehouse {} end", request);
        return dto;
    }

    @Override
    @PostMapping("/add")
    public void addProductInstanceToWarehouse(@RequestBody AddProductToWarehouseRequest request) {
        log.info("==> Add product instance warehouse {} start", request);
        warehouseService.addProductInstanceToWarehouse(request);
        log.info("==> Add product instance warehouse {} end", request);
    }

    @Override
    @GetMapping("/address")
    public AddressDto getAddressWarehouse() {
        log.info("==> Get address warehouse start");
        AddressDto addressDto = warehouseService.getAddressWarehouse();
        log.info("==> Check address warehouse end");
        return addressDto;
    }
}
