package ru.yandex.practicum.commerce.order.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.interactionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.ProductReturnRequest;

import java.util.UUID;

public interface OrderService {

    Page<OrderDto> getAllOrdersByUser(String userName, Pageable pageable);

    OrderDto createOrder(CreateNewOrderRequest newOrderRequest);

    OrderDto returnOrder(ProductReturnRequest returnRequest);

    OrderDto paymentOrder(UUID orderId);

    OrderDto paymentFailedOrder(UUID orderId);

    OrderDto paymentSuccessOrder(UUID orderId);

    OrderDto deliveryOrder(UUID orderId);

    OrderDto deliveryFailedOrder(UUID orderId);

    OrderDto completedOrder(UUID orderId);

    OrderDto calculateTotalOrder(UUID orderId);

    OrderDto calculateDeliveryOrder(UUID orderId);

    OrderDto assemblyOrder(UUID orderId);

    OrderDto assemblyFailedOrder(UUID orderId);
}
