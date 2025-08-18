package ru.yandex.practicum.commerce.interactionapi.feign;

import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.aspect.RestLogging;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.*;

import java.util.Set;

import static ru.yandex.practicum.commerce.interactionapi.Util.X_REQUEST_ID_HEADER;

public interface WarehouseController {
    @PutMapping
    @RestLogging
    @ResponseStatus(HttpStatus.CREATED)
    @CachePut(value = "product", key = "#request.productId")
    void addProductToWarehouse(@RequestBody @Valid NewProductInWarehouseRequestDto request);

    @PostMapping("/check")
    @RestLogging
    @Cacheable(value = "product", key = "#request.productId")
    BookedProductsDto checkProductQuantityForShoppingCart(
            @RequestHeader(X_REQUEST_ID_HEADER) String headerValue,
            @RequestBody @Valid ShoppingCartRequestDto request
    );

    @PostMapping("/add")
    @RestLogging
    @CachePut(value = "product", key = "#request.productId")
    void addProductInstanceToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request);

    @GetMapping("/address")
    @RestLogging
    AddressDto getAddressWarehouse();

    @PostMapping("assembly")
    @RestLogging
    @CacheEvict(value = "product", key = "#request.productId")
    BookedProductsDto assembly(@RequestBody @Valid AssemblyProductsForOrderRequest request);

    @PostMapping("return")
    @RestLogging
    @CacheEvict(value = "product", key = "#request.productId")
    void returnOrder(@RequestBody @Valid Set<ProductDto> productDtos);

    @PostMapping("shipped")
    @RestLogging
    @CacheEvict(value = "product", key = "#request.productId")
    void shipToDelivery(@RequestBody @Valid ShippedToDeliveryRequest request);
}
