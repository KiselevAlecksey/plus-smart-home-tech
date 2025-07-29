package ru.yandex.practicum.commerce.interactionapi.feign;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.NewProductInWarehouseRequestDto;

public interface WarehouseController {
    @PutMapping
    void addProductToWarehouse(@RequestBody @Valid NewProductInWarehouseRequestDto request);

    @PostMapping("/check")
    BookedProductsDto checkProductQuantityForShoppingCart(@RequestBody @Valid ShoppingCartRequestDto request);

    @PostMapping("/add")
    void addProductInstanceToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request);

    @GetMapping("/address")
    AddressDto getAddressWarehouse();
}
