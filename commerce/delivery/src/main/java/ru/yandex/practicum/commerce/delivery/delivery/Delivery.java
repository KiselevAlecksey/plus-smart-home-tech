package ru.yandex.practicum.commerce.delivery.delivery;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.interactionapi.enums.DeliveryState;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "deliveries", schema = "shopping_store")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Delivery {
    @Id
    @Column(name = "delivery_id", updatable = false, nullable = false)
    UUID deliveryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_address_id", nullable = false)
    Address fromAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_address_id", nullable = false)
    Address toAddress;

    @Column(name = "order_id", nullable = false)
    UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    DeliveryState state;
}
