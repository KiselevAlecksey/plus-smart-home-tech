package ru.yandex.practicum.commerce.shoppingcart.cart.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.shoppingcart.cart.ShoppingCart;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "cart_products", schema = "shopping_store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartProduct {

    @Id
    @Column(name = "product_id", updatable = false, nullable = false)
    UUID productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    ShoppingCart shoppingCart;

    @Column(name = "quantity", nullable = false)
    Long quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartProduct that = (CartProduct) o;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
