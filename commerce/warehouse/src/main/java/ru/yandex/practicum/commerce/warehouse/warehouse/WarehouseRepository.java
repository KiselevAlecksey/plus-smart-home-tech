package ru.yandex.practicum.commerce.warehouse.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<ProductInWarehouse, UUID> {
    List<ProductInWarehouse> findByIdIn(List<UUID> uuids);
}
