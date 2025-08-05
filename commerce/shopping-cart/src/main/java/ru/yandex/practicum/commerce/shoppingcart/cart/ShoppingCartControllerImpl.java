package ru.yandex.practicum.commerce.shoppingcart.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    public ShoppingCartResponseDto getShoppingCartByUserName(
            @RequestParam(value = "username") @NotBlank String userName) {
        log.info("==> Get shoppingCart by username {} start", userName);
        ShoppingCartResponseDto dto = shoppingCartService.getShoppingCartByUserName(userName);
        log.info("<== Get shoppingCart by username {} end", userName);
        return dto;
    }

    @Override
    @PutMapping
    public ShoppingCartResponseDto addProductsToShoppingCart(
            @RequestParam(value = "username") @NotBlank String userName,
            @RequestBody(required = false) @Valid Map<UUID, Long> products) {

        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Products map cannot be empty");
        }

        log.info("==> Add products {} shoppingCart by username {} start", products, userName);
        ShoppingCartResponseDto dto = shoppingCartService.addProductsToShoppingCart(userName, products);
        log.info("<== Add products {} shoppingCart by username {} end", products, userName);
        return dto;
    }

    @Override
    @DeleteMapping
    public void removeShoppingCart(@RequestParam(value = "username") @NotBlank String userName) {
        log.info("==> Remove shoppingCart by username {} start", userName);
        shoppingCartService.removeShoppingCart(userName);
        log.info("<== Remove shoppingCart by username {} end", userName);
    }

    @Override
    @PostMapping("/remove")
    public ShoppingCartResponseDto removeShoppingCartProducts(
            @RequestParam(value = "username") @NotBlank String userName,
            @RequestBody Set<UUID> products) {
        log.info("==> Remove shoppingCart products {} by username {} start", products, userName);
        ShoppingCartResponseDto dto = shoppingCartService.removeShoppingCartProducts(userName, products);
        log.info("<== Remove shoppingCart products {} by username {} end", products, userName);
        return dto;
    }

    @Override
    @PostMapping("/change-quantity")
    public ShoppingCartResponseDto changeProductQuantity(
            @RequestParam(value = "username") @NotBlank String userName,
            @RequestBody(required = false) @Validated ProductQuantityDto changeQuantity) {
        log.info("==> Change productQuantity {} by username {} start", changeQuantity, userName);
        ShoppingCartResponseDto dto = shoppingCartService.changeProductQuantity(userName, changeQuantity);
        log.info("<== Change productQuantity {} by username {} end", changeQuantity, userName);
        return dto;
    }
}
