package ru.yandex.practicum.commerce.interactionapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interactionapi.config.FeignErrorConfig;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store", configuration = FeignErrorConfig.class)
public interface ShoppingStoreFeignClient extends ShoppingStoreController {
}
