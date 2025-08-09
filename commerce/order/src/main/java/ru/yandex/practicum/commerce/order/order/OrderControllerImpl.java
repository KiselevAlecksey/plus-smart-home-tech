package ru.yandex.practicum.commerce.order.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.aspect.RestLogging;
import ru.yandex.practicum.commerce.interactionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interactionapi.feign.OrderController;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/order")
public class OrderControllerImpl implements OrderController {
    private final OrderService orderService;

    @Override
    @GetMapping
    @RestLogging
    public Collection<OrderDto> getAllOrdersByUser(@RequestParam(value = "username") @NotBlank String userName) {
        return null;
    }

    @Override
    @PutMapping
    @RestLogging
    public OrderDto createOrder(@RequestBody @Valid CreateNewOrderRequest newOrderRequest) {
        return null;
    }

    @Override
    @RestLogging
    public OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest returnRequest) {
        return null;
    }

    @Override
    @RestLogging
    public OrderDto returnOrder(@RequestBody @NotBlank UUID orderId) {
        return null;
    }

    @Override
    @RestLogging
    public OrderDto failedOrder(@RequestBody @NotBlank UUID orderId) {
        return null;
    }

    @Override
    @RestLogging
    public OrderDto deliveryOrder(@RequestBody @NotBlank UUID orderId) {
        return null;
    }

    @Override
    @RestLogging
    public OrderDto deliveryFailedOrder(@RequestBody @NotBlank UUID orderId) {
        return null;
    }

    @Override
    @RestLogging
    public OrderDto completedOrder(@RequestBody @NotBlank UUID orderId) {
        return null;
    }

    @Override
    @RestLogging
    public OrderDto calculateTotalOrder(@RequestBody @NotBlank UUID orderId) {
        return null;
    }

    @Override
    @RestLogging
    public OrderDto calculateDeliveryOrder(@RequestBody @NotBlank UUID orderId) {
        return null;
    }

    @Override
    @RestLogging
    public OrderDto assemblyOrder(@RequestBody @NotBlank UUID orderId) {
        return null;
    }

    @Override
    @RestLogging
    public OrderDto assemblyFailedOrder(@RequestBody @NotBlank UUID orderId) {
        return null;
    }
}
