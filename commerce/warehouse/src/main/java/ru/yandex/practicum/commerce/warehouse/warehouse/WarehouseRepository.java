package ru.yandex.practicum.commerce.warehouse.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<ProductInWarehouse, UUID> {
    Set<ProductInWarehouse> findByProductIdIn(List<UUID> uuids);
}
