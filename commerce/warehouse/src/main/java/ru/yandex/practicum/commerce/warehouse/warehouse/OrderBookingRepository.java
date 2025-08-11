package ru.yandex.practicum.commerce.warehouse.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderBookingRepository extends JpaRepository<OrderBooking, UUID> {
    Optional<OrderBooking> findByOrderId(UUID orderId);
}
