package ru.yandex.practicum.commerce.order.order;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interactionapi.enums.OrderState;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderServiceImpl implements OrderService {
    final OrderRepository orderRepository;
    final OrderMapper orderMapper;
    final ProductMapper productMapper;

    @Override
    public Page<OrderDto> getAllOrdersByUser(String userName, Pageable pageable) {
        return orderRepository.findByUserName(userName, pageable)
                .map(orderMapper::toOrderDto);
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        UUID uuid = request.shoppingCart().shoppingCartId();

        Order order = Order.builder()
                .orderId(uuid)
                .shoppingCartId(uuid)
                .products((request.shoppingCart().products().stream()
                        .map(productMapper::toEntityProduct)
                        .collect(Collectors.toSet())))
                .paymentId(uuid)
                .deliveryId(uuid)
                .state(OrderState.NEW)
                .deliveryWeight(request.bookedProducts().deliveryWeight())
                .deliveryVolume(request.bookedProducts().deliveryVolume())
                .fragile(request.bookedProducts().fragile())
                .totalPrice()
                .deliveryPrice()
                .productPrice(request.bookedProducts().prices().values().stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .userName(request.userName())
                .build();

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
