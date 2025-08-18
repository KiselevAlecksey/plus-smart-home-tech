package ru.yandex.practicum.commerce.shoppingcart.cart;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.shoppingcart.cart.product.CartProduct;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "shopping_cart", schema = "shopping_store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shopping_cart_id", updatable = false, nullable = false)
    UUID shoppingCartId;

    @Column(name = "user_name", nullable = false)
    String userName;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    ShoppingCartState state;

    @OneToMany(mappedBy = "shoppingCart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CartProduct> products;
}

