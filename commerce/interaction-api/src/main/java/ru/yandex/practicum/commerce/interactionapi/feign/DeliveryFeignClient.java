package ru.yandex.practicum.commerce.interactionapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interactionapi.config.FeignErrorConfig;

@FeignClient(name = "delivery", path = "/api/v1/delivery", configuration = FeignErrorConfig.class)
public interface DeliveryFeignClient extends DeliveryController {
}
