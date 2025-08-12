package ru.yandex.practicum.commerce.payment.payment;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payments", schema = "shopping_store")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Payment {

    @Id
    @Column(name = "payment_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "total_payment", nullable = false)
    BigDecimal totalPayment;

    @Column(name = "delivery_total")
    BigDecimal deliveryTotal;

    @Column(name = "fee_total")
    BigDecimal feeTotal;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    PaymentStatus state;

    @Column(name = "order_id")
    UUID orderId;
}
