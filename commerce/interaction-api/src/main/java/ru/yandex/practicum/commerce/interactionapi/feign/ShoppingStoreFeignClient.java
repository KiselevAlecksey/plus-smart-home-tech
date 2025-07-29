package ru.yandex.practicum.commerce.interactionapi.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreFeignClient extends ShoppingStoreController {
}
