package ru.yandex.practicum.commerce.interactionapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interactionapi.config.FeignErrorConfig;

@FeignClient(name = "order", path = "/api/v1/order", configuration = FeignErrorConfig.class)
public interface OrderFeignClient extends OrderController {
}
