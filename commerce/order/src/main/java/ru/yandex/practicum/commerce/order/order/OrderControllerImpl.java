package ru.yandex.practicum.commerce.order.order;

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
    public OrderDto createOrder(CreateNewOrderRequest newOrderRequest) {
        return orderService.createOrder(newOrderRequest);
    }

    @Override
    @RestLogging
    public OrderDto returnOrder(ProductReturnRequest returnRequest) {
        return orderService.returnOrder(returnRequest);
    }

    @Override
    @RestLogging
    public OrderDto paymentOrder(UUID orderId) {
        return orderService.paymentOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto paymentFailedOrder(UUID orderId) {
        return orderService.paymentFailedOrder(orderId);
    }

    @Override
    public OrderDto paymentSuccessOrder(UUID orderId) {
        return orderService.paymentSuccessOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto deliveryOrder(UUID orderId) {
        return orderService.deliveryOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto deliveryFailedOrder(UUID orderId) {
        return orderService.deliveryFailedOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto completedOrder(UUID orderId) {
        return orderService.completedOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto calculateTotalOrder(UUID orderId) {
        return orderService.calculateTotalOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto calculateDeliveryOrder(UUID orderId) {
        return orderService.calculateDeliveryOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto assemblyOrder(UUID orderId) {
        return orderService.assemblyOrder(orderId);
    }

    @Override
    @RestLogging
    public OrderDto assemblyFailedOrder(UUID orderId) {
        return orderService.assemblyFailedOrder(orderId);
    }
}
