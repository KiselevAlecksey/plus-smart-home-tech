package ru.yandex.practicum.commerce.interactionapi.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseFeignClient extends WarehouseController {
}
