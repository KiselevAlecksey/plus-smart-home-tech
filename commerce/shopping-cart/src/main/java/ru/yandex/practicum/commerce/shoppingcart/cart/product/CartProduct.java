package ru.yandex.practicum.commerce.shoppingcart.cart.product;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.shoppingcart.cart.ShoppingCart;

import java.util.UUID;

@Entity
@Table(name = "cart_products", schema = "shopping_store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartProduct {

    @Id
    @Column(name = "product_id", updatable = false, nullable = false)
    private UUID productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCart shoppingCart;

    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
