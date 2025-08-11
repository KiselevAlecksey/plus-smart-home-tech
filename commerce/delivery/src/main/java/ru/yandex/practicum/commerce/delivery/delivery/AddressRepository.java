package ru.yandex.practicum.commerce.delivery.delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findByDeliveryId(UUID deliveryId);
}
