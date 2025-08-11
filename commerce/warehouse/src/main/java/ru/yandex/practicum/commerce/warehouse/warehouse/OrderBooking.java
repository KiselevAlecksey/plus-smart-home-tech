package ru.yandex.practicum.commerce.warehouse.warehouse;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "order_bookings", schema = "shopping_store")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderBooking {
    @Id
    @Column(name = "order_booking_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @OneToMany(mappedBy = "orderBooking", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<BookedProductItem> bookedItems = new HashSet<>();

    public void addBookedItem(BookedProductItem item) {
        bookedItems.add(item);
        item.setOrderBooking(this);
    }
}
