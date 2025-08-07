package ru.yandex.practicum.commerce.interactionapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interactionapi.config.FeignErrorConfig;

@FeignClient(
        name = "warehouse",
        path = "/api/v1/warehouse",
        fallback = WarehouseFeignClientFallback.class,
        configuration = FeignErrorConfig.class
)
public interface WarehouseFeignClient extends WarehouseController {
}
