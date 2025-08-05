package ru.yandex.practicum.commerce.interactionapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interactionapi.config.FeignErrorConfig;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart", configuration = FeignErrorConfig.class)
public interface ShoppingCartFeignClient {
}
