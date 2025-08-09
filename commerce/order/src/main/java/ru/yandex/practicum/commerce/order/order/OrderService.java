package ru.yandex.practicum.commerce.order.order;

import ru.yandex.practicum.commerce.interactionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.ProductReturnRequest;

import java.util.Collection;
import java.util.UUID;

public interface OrderService {

    Collection<OrderDto> getAllOrdersByUser(String userName);

    OrderDto createOrder(CreateNewOrderRequest newOrderRequest);

    OrderDto returnOrder(ProductReturnRequest returnRequest);

    OrderDto returnOrder(UUID orderId);

    OrderDto failedOrder(UUID orderId);

    OrderDto deliveryOrder(UUID orderId);

    OrderDto deliveryFailedOrder(UUID orderId);

    OrderDto completedOrder(UUID orderId);

    OrderDto calculateTotalOrder(UUID orderId);

    OrderDto calculateDeliveryOrder(UUID orderId);

    OrderDto assemblyOrder(UUID orderId);

    OrderDto assemblyFailedOrder(UUID orderId);
}
