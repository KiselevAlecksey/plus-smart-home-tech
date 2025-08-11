package ru.yandex.practicum.commerce.order.order;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interactionapi.enums.OrderState;
import ru.yandex.practicum.commerce.interactionapi.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interactionapi.feign.DeliveryFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feign.PaymentFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feign.WarehouseFeignClient;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    ProductMapper productMapper;
    PaymentFeignClient paymentFeignClient;
    WarehouseFeignClient warehouseFeignClient;
    DeliveryFeignClient deliveryFeignClient;
    CartProductRepository cartProductRepository;

    @Override
    public Page<OrderDto> getAllOrdersByUser(String userName, Pageable pageable) {
        return orderRepository.findByUserName(userName, pageable)
                .map(orderMapper::toOrderDto);
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        Order order = Order.builder()
                .shoppingCartId(request.shoppingCart().shoppingCartId())
                .products((request.shoppingCart().products().stream()
                        .map(productMapper::toEntityProduct)
                        .collect(Collectors.toSet())))
                .state(OrderState.NEW)
                .userName(request.userName())
                .address(orderMapper.toAddress(request.deliveryAddress()))
                .build();

        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest returnRequest) {

        // убрать из заказа возвратные продукты

        Order order = getOrder(returnRequest.orderId());
        return orderMapper.toOrderDto(
                order.toBuilder()
                        .state(OrderState.PRODUCT_RETURNED)
                        .build()
        );
    }

    @Override
    public OrderDto paymentOrder(UUID orderId) {
        Order order = getOrder(orderId);
        PaymentDto paymentDto = paymentFeignClient.payment(orderMapper.toOrderDto(order));

        return orderMapper.toOrderDto(
                order.toBuilder()
                        .state(OrderState.PAID)
                        .paymentId(paymentDto.paymentId())
                        .build()
        );
    }

    @Override
    public OrderDto paymentFailedOrder(UUID orderId) {
        Order order = getOrder(orderId);
        return orderMapper.toOrderDto(
                order.toBuilder()
                        .state(OrderState.PAYMENT_FAILED)
                        .build()
        );
    }

    @Override
    public OrderDto deliveryOrder(UUID orderId) {
        Order order = getOrder(orderId);
        return orderMapper.toOrderDto(
                order.toBuilder()
                        .state(OrderState.DELIVERED)
                        .build()
        );
    }

    @Override
    public OrderDto deliveryFailedOrder(UUID orderId) {
        Order order = getOrder(orderId);
        return orderMapper.toOrderDto(
                order.toBuilder()
                        .state(OrderState.DELIVERY_FAILED)
                        .build()
        );
    }

    @Override
    public OrderDto completedOrder(UUID orderId) {
        Order order = getOrder(orderId);
        return orderMapper.toOrderDto(
                order.toBuilder()
                        .state(OrderState.COMPLETED)
                        .build()
        );
    }

    @Override
    public OrderDto calculateTotalOrder(UUID orderId) {
        Order order = getOrder(orderId);
        BigDecimal productCost = paymentFeignClient.productCost(orderMapper.toOrderDto(order));
        return orderMapper.toOrderDto(
                order.toBuilder()
                        .productPrice(productCost)
                        .totalPrice(productCost.add(order.getDeliveryPrice()))
                        .build()
        );
    }

    @Override
    public OrderDto calculateDeliveryOrder(UUID orderId) {
        Order order = getOrder(orderId);
        BigDecimal deliveryCost = deliveryFeignClient.deliveryCost(orderMapper.toOrderDto(order));

        return orderMapper.toOrderDto(
                order.toBuilder()
                        .deliveryPrice(deliveryCost)
                        .deliveryId(deliveryFeignClient.getDeliveryId(String.valueOf(orderId)))
                        .build()
        );
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) {
        Order order = getOrder(orderId);

        Set<ProductDto> productDtos = order.getProducts().stream()
                .map(productMapper::toProductDto)
                .collect(Collectors.toSet());

        BookedProductsDto bookedProductsDto = warehouseFeignClient.assembly(AssemblyProductsForOrderRequest.builder()
                .orderId(orderId)
                .products(productDtos)
                .build());

        return orderMapper.toOrderDto(
                order.toBuilder()
                        .state(OrderState.ASSEMBLED)
                        .deliveryWeight(bookedProductsDto.deliveryWeight())
                        .deliveryVolume(bookedProductsDto.deliveryVolume())
                        .fragile(bookedProductsDto.fragile()).build()
        );
    }

    private Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> NoOrderFoundException.builder()
                .message("Ошибка при поиске заказа")
                .userMessage("Заказ не найден. Пожалуйста, проверьте идентификатор")
                .httpStatus(HttpStatus.NOT_FOUND)
                .cause(new RuntimeException("Заказ с ID " + orderId + " не найден"))
                .build());
    }

    @Override
    public OrderDto assemblyFailedOrder(UUID orderId) {
        Order order = getOrder(orderId);
        return orderMapper.toOrderDto(
                order.toBuilder()
                        .state(OrderState.ASSEMBLY_FAILED)
                        .build()
        );
    }
}
