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
    @Cacheable(value = "userShoppingCart", key = "#userName")
    @GetMapping
    ShoppingCartResponseDto getShoppingCartByUserName(
            @RequestParam(value = "username") @NotBlank String userName);

    @PutMapping
    ShoppingCartResponseDto addProductsToShoppingCart(
            @RequestParam(value = "username") @NotBlank String userName,
            @RequestBody(required = false) @Valid Map<UUID, Long> products);

    @CacheEvict(value = "userShoppingCart", key = "#userName")
    @DeleteMapping
    void removeShoppingCart(@RequestParam(value = "username") @NotBlank String userName);

    @CachePut(value = "userShoppingCart", key = "#userName")
    @PostMapping("/remove")
    ShoppingCartResponseDto removeShoppingCartProducts(
            @RequestParam(value = "username") @NotBlank String userName,
            @RequestBody Set<UUID> products);

    @CachePut(value = "userShoppingCart", key = "#userName")
    @PostMapping("/change-quantity")
    ShoppingCartResponseDto changeProductQuantity(
            @RequestParam(value = "username") @NotBlank String userName,
            @RequestBody(required = false) @Validated ProductQuantityDto changeQuantity);
}
