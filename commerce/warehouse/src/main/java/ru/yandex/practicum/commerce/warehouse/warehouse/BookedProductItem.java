package ru.yandex.practicum.commerce.warehouse.warehouse;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "booked_product_items", schema = "shopping_store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookedProductItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_booking_id")
    private OrderBooking orderBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductInWarehouse productInWarehouse;

    @Column(nullable = false)
    private long quantity;
}
