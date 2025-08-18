package ru.yandex.practicum.commerce.order.order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_cart_products", schema = "shopping_store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_product_id", updatable = false, nullable = false)
    UUID id;

    @Column(name = "product_id", updatable = false, nullable = false)
    UUID productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @Column(name = "shopping_cart_id")
    UUID shoppingCartId;

    @Column(name = "quantity", nullable = false)
    Long quantity;

    @Column(name = "price", precision = 12, scale = 2)
    BigDecimal price = BigDecimal.ZERO;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartProduct that = (CartProduct) o;
        return id != null && Objects.equals(productId, that.productId) && Objects.equals(shoppingCartId, that.shoppingCartId);
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }
}
