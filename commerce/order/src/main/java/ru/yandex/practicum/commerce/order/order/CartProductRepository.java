package ru.yandex.practicum.commerce.order.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface CartProductRepository extends JpaRepository<CartProduct, UUID> {
}
