package ru.yandex.practicum.commerce.warehouse.warehouse;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "products_in_warehouse", schema = "shopping_store")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(exclude = "bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInWarehouse {

    @Id
    @Column(name = "product_id", updatable = false, nullable = false)
    UUID id;

    @Column(name = "quantity", nullable = false)
    Long quantity;

    @Column(name = "fragile", nullable = false)
    boolean fragile;

    @Column(name = "width", nullable = false)
    double width;

    @Column(name = "height", nullable = false)
    double height;

    @Column(name = "depth", nullable = false)
    double depth;

    @Column(name = "weight", nullable = false)
    double weight;

    @OneToMany(mappedBy = "productInWarehouse")
    @Builder.Default
    Set<BookedProductItem> bookings = new HashSet<>();
}
