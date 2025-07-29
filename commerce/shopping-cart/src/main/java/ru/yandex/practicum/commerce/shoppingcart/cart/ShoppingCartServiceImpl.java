package ru.yandex.practicum.commerce.shoppingcart.cart;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.*;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductQuantityDto;
import ru.yandex.practicum.commerce.interactionapi.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.interactionapi.exception.ShoppingCartNotFoundException;
import ru.yandex.practicum.commerce.interactionapi.feign.WarehouseFeignClient;
import ru.yandex.practicum.commerce.shoppingcart.cart.product.CartProduct;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    final ShoppingCartRepository cartRepository;
    final ShoppingCartMapper cartMapper;
    final WarehouseFeignClient warehouseClient;

    @Override
    @Transactional
    public ShoppingCartResponseDto getShoppingCartByUserName(String userName) {
        ShoppingCart cart = cartRepository.findByUserName(userName)
                .orElseGet(() -> {
                    ShoppingCart newCart = ShoppingCart.builder()
                            .userName(userName)
                            .state(ShoppingCartState.ACTIVE)
                            .products(new HashSet<>())
                            .build();
                    return cartRepository.save(newCart);
                });
        return cartMapper.toResponseDto(cart);
    }

    @Override
    public ShoppingCartResponseDto addProductsToShoppingCart(String userName, Map<UUID, Long> products) {
        ShoppingCart cart = cartRepository.findByUserName(userName)
                .orElseGet(() -> {
                    ShoppingCart newCart = ShoppingCart.builder()
                            .userName(userName)
                            .state(ShoppingCartState.ACTIVE)
                            .products(new HashSet<>())
                            .build();
                    return cartRepository.save(newCart);
                });

        ShoppingCartRequestDto requestDto = ShoppingCartRequestDto.builder()
                .shoppingCartId(cart.getShoppingCartId())
                .products(products.entrySet().stream()
                        .map(entry -> new ProductDto(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toSet()))
                .build();

        warehouseClient.checkProductQuantityForShoppingCart(requestDto);

        products.forEach((productId, quantity) -> {
            Optional<CartProduct> existingProduct = cart.getProducts().stream()
                    .filter(cp -> cp.getProductId().equals(productId))
                    .findFirst();
            if (existingProduct.isPresent()) {
                CartProduct cp = existingProduct.get();
                cp.setQuantity(cp.getQuantity() + quantity);
            } else {
                CartProduct newCartProduct = new CartProduct();
                newCartProduct.setShoppingCart(cart);

                newCartProduct.setProductId(productId);

                newCartProduct.setQuantity(quantity);
                cart.getProducts().add(newCartProduct);
            }
        });

        return transactionalSaveCart(cart);
    }

    @Transactional
    private ShoppingCartResponseDto transactionalSaveCart(ShoppingCart cart) {
        ShoppingCart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDto(savedCart);
    }

    @Override
    @Transactional
    public void removeShoppingCart(String userName) {
        ShoppingCart cart = cartRepository.findByUserName(userName)
                .orElseThrow(() -> ShoppingCartNotFoundException.builder()
                        .message("Ошибка при поиске корзины")
                        .userMessage("Корзина не найдена для пользователя: " + userName)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .cause(new RuntimeException("Корзина не найдена"))
                        .build());

        cartRepository.delete(cart);
    }

    @Override
    public ShoppingCartResponseDto removeShoppingCartProducts(String userName, Set<UUID> products) {
        ShoppingCart cart = cartRepository.findByUserName(userName)
                .orElseThrow(() -> ShoppingCartNotFoundException.builder()
                        .message("Ошибка при поиске корзины")
                        .userMessage("Корзина не найдена для пользователя: " + userName)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .cause(new RuntimeException("Корзина не найдена"))
                        .build());

        cart.getProducts().removeIf(product -> products.contains(product.getProductId()));

        return transactionalSaveCart(cart);
    }

    @Override
    public ShoppingCartResponseDto changeProductQuantity(String userName, ProductQuantityDto changeQuantity) {
        ShoppingCart cart = cartRepository.findByUserName(userName)
                .orElseThrow(() -> ShoppingCartNotFoundException.builder()
                        .message("Ошибка при поиске корзины")
                        .userMessage("Корзина не найдена для пользователя: " + userName)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .cause(new RuntimeException("Корзина не найдена"))
                        .build());

        CartProduct product = cart.getProducts().stream()
                .filter(p -> p.getProductId().equals(changeQuantity.productId()))
                .findFirst()
                .orElseThrow(() -> ProductNotFoundException.builder()
                        .message("Ошибка при поиске продукта в корзине")
                        .userMessage("Продукт не найден в вашей корзине")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .cause(new RuntimeException("Продукт не найден в корзине"))
                        .build());

        product.setQuantity(changeQuantity.newQuantity());

        return transactionalSaveCart(cart);
    }
}
