package ru.yandex.practicum.commerce.order.order;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.ProductReturnRequest;

import java.util.Collection;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderServiceImpl implements OrderService {
    final OrderRepository orderRepository;

    @Override
    public Collection<OrderDto> getAllOrdersByUser(String userName) {
        return ;
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest newOrderRequest) {
        return null;
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest returnRequest) {
        return null;
    }

    @Override
    public OrderDto returnOrder(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto failedOrder(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto deliveryOrder(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto deliveryFailedOrder(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto completedOrder(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto calculateTotalOrder(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto calculateDeliveryOrder(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto assemblyFailedOrder(UUID orderId) {
        return null;
    }
}
