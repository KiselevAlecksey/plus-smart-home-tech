package ru.yandex.practicum.commerce.interactionapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interactionapi.config.FeignErrorConfig;

@FeignClient(name = "payment", path = "/api/v1/payment", configuration = FeignErrorConfig.class)
public interface PaymentFeignClient extends PaymentController {
}
