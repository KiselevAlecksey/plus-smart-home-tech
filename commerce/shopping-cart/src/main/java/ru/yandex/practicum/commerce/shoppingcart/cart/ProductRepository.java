package ru.yandex.practicum.commerce.shoppingcart.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shoppingcart.cart.product.CartProduct;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<CartProduct, UUID> {
}
