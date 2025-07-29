package ru.yandex.practicum.commerce.shoppingcart.cart;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.ProductQuantityDto;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartResponseDto;
import ru.yandex.practicum.commerce.interactionapi.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.interactionapi.exception.ShoppingCartNotFoundException;
import ru.yandex.practicum.commerce.shoppingcart.cart.product.CartProduct;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    final ShoppingCartRepository cartRepository;
    final ShoppingCartMapper cartMapper;
    final ProductMapper productMapper;
    final ProductRepository productRepository;

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
    @Transactional
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

        products.forEach((productId, quantity) -> {
            CartProduct product = productRepository.findById(productId)
                    .orElseThrow(() -> ProductNotFoundException.builder()
                            .message("Ошибка при поиске продукта")
                            .userMessage("Продукт не найден. Пожалуйста, проверьте идентификатор")
                            .httpStatus(HttpStatus.NOT_FOUND)
                            .cause(new RuntimeException("Продукт с ID " + productId + " не найден"))
                            .build());

            Optional<CartProduct> existingProduct = cart.getProducts().stream()
                    .filter(p -> p.getProductId().equals(productId))
                    .findFirst();

            if (existingProduct.isPresent()) {
                CartProduct cartProduct = existingProduct.get();
                cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
            } else {
                CartProduct cartProduct = CartProduct.builder()
                        .productId(productId)
                        .shoppingCart(cart)
                        .quantity(quantity)
                        .build();
                cart.getProducts().add(cartProduct);
            }
        });

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
    @Transactional
    public ShoppingCartResponseDto removeShoppingCartProducts(String userName, Set<UUID> products) {
        ShoppingCart cart = cartRepository.findByUserName(userName)
                .orElseThrow(() -> ShoppingCartNotFoundException.builder()
                        .message("Ошибка при поиске корзины")
                        .userMessage("Корзина не найдена для пользователя: " + userName)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .cause(new RuntimeException("Корзина не найдена"))
                        .build());

        cart.getProducts().removeIf(product -> products.contains(product.getProductId()));

        ShoppingCart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDto(savedCart);
    }

    @Override
    @Transactional
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

        ShoppingCart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDto(savedCart);
    }
}
