package ru.yandex.practicum.commerce.interactionapi.feign;

import jakarta.validation.Valid;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.NewProductInWarehouseRequestDto;

import static ru.yandex.practicum.commerce.interactionapi.Util.X_REQUEST_ID_HEADER;

public interface WarehouseController {
    @PutMapping
    @CachePut(value = "product", key = "#request.productId")
    void addProductToWarehouse(@RequestBody @Valid NewProductInWarehouseRequestDto request);

    @PostMapping("/check")
    @Cacheable(value = "product", key = "#request.productId")
    BookedProductsDto checkProductQuantityForShoppingCart(
            @RequestHeader(X_REQUEST_ID_HEADER) String headerValue,
            @RequestBody @Valid ShoppingCartRequestDto request
    );

    @PostMapping("/add")
    @CachePut(value = "product", key = "#request.productId")
    void addProductInstanceToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request);

    @GetMapping("/address")
    AddressDto getAddressWarehouse();
}
