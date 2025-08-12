package ru.yandex.practicum.commerce.order.order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.interactionapi.enums.OrderState;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders", schema = "shopping_store")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @Column(name = "order_id", updatable = false, nullable = false)
    UUID orderId;

    @Column(name = "shopping_cart_id", nullable = false)
    UUID shoppingCartId;

    @Column(name = "payment_id", nullable = false)
    UUID paymentId;

    @Column(name = "delivery_id", nullable = false)
    UUID deliveryId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CartProduct> products = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    OrderState state;

    @Column(name = "delivery_weight")
    double deliveryWeight;

    @Column(name = "delivery_volume")
    double deliveryVolume;

    @Column(name = "fragile")
    boolean fragile;

    @Column(name = "total_price", precision = 10, scale = 2)
    BigDecimal totalPrice;

    @Column(name = "delivery_price", precision = 10, scale = 2)
    BigDecimal deliveryPrice;

    @Column(name = "product_price", precision = 10, scale = 2)
    BigDecimal productPrice;
}
