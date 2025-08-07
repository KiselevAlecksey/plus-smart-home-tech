package ru.yandex.practicum.commerce.shoppingcart.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.aspect.RestLogging;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductQuantityDto;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartResponseDto;
import ru.yandex.practicum.commerce.interactionapi.feign.ShoppingCartController;


import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartControllerImpl implements ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Override
    @GetMapping
    @RestLogging
    public ShoppingCartResponseDto getShoppingCartByUserName(
            @RequestParam(value = "username") @NotBlank String userName) {
        return shoppingCartService.getShoppingCartByUserName(userName);
    }

    @Override
    @PutMapping
    @RestLogging
    public ShoppingCartResponseDto addProductsToShoppingCart(
            @RequestParam(value = "username") @NotBlank String userName,
            @RequestBody(required = false) @Valid Map<UUID, Long> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Products map cannot be empty");
        }
        return shoppingCartService.addProductsToShoppingCart(userName, products);
    }

    @Override
    @DeleteMapping
    @RestLogging
    public void removeShoppingCart(@RequestParam(value = "username") @NotBlank String userName) {
        shoppingCartService.removeShoppingCart(userName);
    }

    @Override
    @PostMapping("/remove")
    @RestLogging
    public ShoppingCartResponseDto removeShoppingCartProducts(
            @RequestParam(value = "username") @NotBlank String userName,
            @RequestBody Set<UUID> products) {
        return shoppingCartService.removeShoppingCartProducts(userName, products);
    }

    @Override
    @PostMapping("/change-quantity")
    @RestLogging
    public ShoppingCartResponseDto changeProductQuantity(
            @RequestParam(value = "username") @NotBlank String userName,
            @RequestBody(required = false) @Validated ProductQuantityDto changeQuantity) {
        return shoppingCartService.changeProductQuantity(userName, changeQuantity);
    }
}
