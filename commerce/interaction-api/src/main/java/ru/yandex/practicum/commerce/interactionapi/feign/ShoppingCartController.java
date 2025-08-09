package ru.yandex.practicum.commerce.interactionapi.feign;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartResponseDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductQuantityDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ShoppingCartController {

    @GetMapping
    @Cacheable(value = "userShoppingCart", key = "#userName")
    ShoppingCartResponseDto getShoppingCartByUserName(
            @RequestParam(value = "username") @NotBlank String userName);

    @PutMapping
    ShoppingCartResponseDto addProductsToShoppingCart(
            @RequestParam(value = "username") @NotBlank String userName,
            @RequestBody(required = false) @Valid Map<UUID, Long> products);


    @DeleteMapping
    @CacheEvict(value = "userShoppingCart", key = "#userName")
    void removeShoppingCart(@RequestParam(value = "username") @NotBlank String userName);


    @PostMapping("/remove")
    @CachePut(value = "userShoppingCart", key = "#userName")
    ShoppingCartResponseDto removeShoppingCartProducts(
            @RequestParam(value = "username") @NotBlank String userName,
            @RequestBody Set<UUID> products);


    @PostMapping("/change-quantity")
    @CachePut(value = "userShoppingCart", key = "#userName")
    ShoppingCartResponseDto changeProductQuantity(
            @RequestParam(value = "username") @NotBlank String userName,
            @RequestBody(required = false) @Validated ProductQuantityDto changeQuantity);
}
