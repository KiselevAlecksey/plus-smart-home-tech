package ru.yandex.practicum.commerce.order.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.aspect.RestLogging;
import ru.yandex.practicum.commerce.interactionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interactionapi.feign.OrderController;

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
    public Page<OrderDto> getAllOrdersByUser(
            @RequestParam(value = "username") @NotBlank String userName,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        return orderService.getAllOrdersByUser(userName, pageable);
    }

    @Override
    @PutMapping
    @RestLogging
    public OrderDto createOrder(@RequestBody @Valid CreateNewOrderRequest newOrderRequest) {
        return orderService.createOrder(newOrderRequest);
    }

    @Override
    @RestLogging
    public OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest returnRequest) {
        return orderService.returnOrder(returnRequest);
    }

    @Override
    @RestLogging
    public OrderDto paymentOrder(@RequestBody @NotBlank UUID orderId) {
        return orderService.paymentOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto paymentFailedOrder(@RequestBody @NotBlank UUID orderId) {
        return orderService.paymentFailedOrder(orderId);
    }

    @Override
    public OrderDto paymentSuccessOrder(UUID orderId) {
        return orderService.paymentSuccessOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto deliveryOrder(@RequestBody @NotBlank UUID orderId) {
        return orderService.deliveryOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto deliveryFailedOrder(@RequestBody @NotBlank UUID orderId) {
        return orderService.deliveryFailedOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto completedOrder(@RequestBody @NotBlank UUID orderId) {
        return orderService.completedOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto calculateTotalOrder(@RequestBody @NotBlank UUID orderId) {
        return orderService.calculateTotalOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto calculateDeliveryOrder(@RequestBody @NotBlank UUID orderId) {
        return orderService.calculateDeliveryOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto assemblyOrder(@RequestBody @NotBlank UUID orderId) {
        return orderService.assemblyOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto assemblyFailedOrder(@RequestBody @NotBlank UUID orderId) {
        return orderService.assemblyFailedOrder(orderId);
    }
}
