package ru.yandex.practicum.commerce.interactionapi.feign;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.ProductReturnRequest;

import java.util.UUID;

public interface OrderController {

    @GetMapping
    @Cacheable(value = "orders", key = "#userName")
    Page<OrderDto> getAllOrdersByUser(
            @RequestParam(value = "username") @NotBlank String userName, Pageable pageable);

    @PutMapping
    @CacheEvict(value = "orders", key = "#userName")
    OrderDto createOrder(@RequestBody @Valid CreateNewOrderRequest newOrderRequest);

    @PostMapping("/return")
    @CacheEvict(value = "orders", key = "#userName")
    OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest returnRequest);

    @PostMapping("/payment")
    OrderDto returnOrder(@RequestBody @NotBlank UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto failedOrder(@RequestBody @NotBlank UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliveryOrder(@RequestBody @NotBlank UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto deliveryFailedOrder(@RequestBody @NotBlank UUID orderId);

    @PostMapping("/completed")
    OrderDto completedOrder(@RequestBody @NotBlank UUID orderId);

    @PostMapping("/calculate/total")
    @CachePut(value = "order", key = "#userName")
    OrderDto calculateTotalOrder(@RequestBody @NotBlank UUID orderId);

    @PostMapping("/calculate/delivery")
    @CachePut(value = "order", key = "#userName")
    OrderDto calculateDeliveryOrder(@RequestBody @NotBlank UUID orderId);

    @PostMapping("/assembly")
    OrderDto assemblyOrder(@RequestBody @NotBlank UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto assemblyFailedOrder(@RequestBody @NotBlank UUID orderId);
}
