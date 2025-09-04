package ru.yandex.practicum.commerce.order.order;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.dto.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interactionapi.enums.DeliveryState;
import ru.yandex.practicum.commerce.interactionapi.enums.OrderState;
import ru.yandex.practicum.commerce.interactionapi.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interactionapi.feign.DeliveryFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feign.PaymentFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feign.WarehouseFeignClient;

import java.math.BigDecimal;
import java.util.Map;
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

    @Override
    public Page<OrderDto> getAllOrdersByUser(String userName, Pageable pageable) {
        return orderRepository.findByUserName(userName, pageable)
                .map(orderMapper::toOrderDto);
    }

    @Override
    @Transactional
    public OrderDto createOrder(CreateNewOrderRequest request) {
        Order order = Order.builder()
                .shoppingCartId(request.shoppingCart().shoppingCartId())
                .state(OrderState.NEW)
                .userName(request.userName())
                .paymentId(UUID.randomUUID())
                .deliveryId(UUID.randomUUID())
                .build();

        Set<CartProduct> cartProducts = request.shoppingCart().products().stream()
                .map(productMapper::toEntityProduct)
                .peek(cp -> cp.setOrder(order))
                .peek(cp -> cp.setShoppingCartId(order.getShoppingCartId()))
                .collect(Collectors.toSet());

        Address address = orderMapper.toAddress(request.deliveryAddress());
        address.setOrder(order);

        order.setProducts(cartProducts);
        order.setAddress(address);

        Order orderSaved = orderRepository.save(order);

        return orderMapper.toOrderDto(orderSaved);
    }

    @Override
    @Transactional
    public OrderDto returnOrder(ProductReturnRequest returnRequest) {
        Map<UUID, CartProduct> productDtoMap = returnRequest.products().stream()
                .collect(Collectors.toMap(ProductDto::productId, productMapper::toEntityProduct));

        Order order = getOrderOrThrow(returnRequest.orderId());

        warehouseFeignClient.returnOrder(
                order.getProducts().stream()
                        .filter(p -> p.equals(productDtoMap.get(p.getProductId())))
                        .map(productMapper::toProductDto)
                        .collect(Collectors.toSet())
        );

        order.getProducts().removeIf(p -> p.equals(productDtoMap.get(p.getProductId())));

        OrderDto dto = orderMapper.toOrderDto(order);

        order.setDeliveryPrice(deliveryFeignClient.deliveryCost(dto));
        order.setProductPrice(paymentFeignClient.productCost(dto));
        order.setTotalPrice(paymentFeignClient.totalCost(dto));

        return orderMapper.toOrderDto(
                orderRepository.saveAndFlush(order.toBuilder()
                        .state(OrderState.PRODUCT_RETURNED)
                        .build()
                )
        );
    }

    @Override
    @Transactional
    public OrderDto paymentOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        PaymentDto paymentDto = paymentFeignClient.paymentCreate(orderMapper.toOrderDto(order));

        return orderMapper.toOrderDto(
                orderRepository.saveAndFlush(order.toBuilder()
                        .state(OrderState.ON_PAYMENT)
                        .paymentId(paymentDto.paymentId())
                        .build()
                )
        );
    }

    @Override
    @Transactional
    public OrderDto paymentSuccessOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        return orderMapper.toOrderDto(
                orderRepository.saveAndFlush(order.toBuilder()
                        .state(OrderState.PAID)
                        .build()
                )
        );
    }

    @Override
    @Transactional
    public OrderDto paymentFailedOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        return orderMapper.toOrderDto(
                orderRepository.saveAndFlush(
                        order.toBuilder()
                                .state(OrderState.PAYMENT_FAILED)
                                .build()
                )
        );
    }

    @Override
    @Transactional
    public OrderDto deliveryOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        return orderMapper.toOrderDto(
                orderRepository.saveAndFlush(
                        order.toBuilder()
                                .state(OrderState.ON_DELIVERY)
                                .build()
                )
        );
    }

    @Override
    @Transactional
    public OrderDto deliveryFailedOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        return orderMapper.toOrderDto(
                orderRepository.saveAndFlush(
                        order.toBuilder()
                                .state(OrderState.DELIVERY_FAILED)
                                .build()
                )
        );
    }

    @Override
    @Transactional
    public OrderDto completedOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        return orderMapper.toOrderDto(
                orderRepository.saveAndFlush(
                        order.toBuilder()
                                .state(OrderState.COMPLETED)
                                .build()
                )
        );
    }

    @Override
    @Transactional
    public OrderDto calculateTotalOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        BigDecimal productCost = paymentFeignClient.productCost(orderMapper.toOrderDto(order));
        return orderMapper.toOrderDto(
                orderRepository.saveAndFlush(
                        order.toBuilder()
                                .productPrice(productCost)
                                .totalPrice(productCost.add(order.getDeliveryPrice()))
                                .build()
                )
        );
    }

    @Override
    @Transactional
    public OrderDto calculateDeliveryOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);

        order.setDeliveryId(deliveryFeignClient.planDelivery(
                DeliveryDto.builder()
                        .fromAddress(warehouseFeignClient.getAddressWarehouse())
                        .toAddress(orderMapper.toAddressDto(order.getAddress()))
                        .orderId(orderId)
                        .deliveryState(DeliveryState.CREATED)
                        .build())
                .deliveryId()
        );

        BigDecimal deliveryCost = deliveryFeignClient.deliveryCost(orderMapper.toOrderDto(order));

        return orderMapper.toOrderDto(
                orderRepository.saveAndFlush(
                        order.toBuilder()
                                .deliveryPrice(deliveryCost)
                                .build()
                )
        );
    }

    @Override
    @Transactional
    public OrderDto assemblyOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);

        Set<ProductDto> productDtos = order.getProducts().stream()
                .map(productMapper::toProductDto)
                .collect(Collectors.toSet());

        BookedProductsDto bookedProductsDto = warehouseFeignClient.assembly(AssemblyProductsForOrderRequest.builder()
                .orderId(orderId)
                .products(productDtos)
                .build());

        return orderMapper.toOrderDto(
                orderRepository.saveAndFlush(
                        order.toBuilder()
                                .state(OrderState.ASSEMBLED)
                                .deliveryWeight(bookedProductsDto.deliveryWeight())
                                .deliveryVolume(bookedProductsDto.deliveryVolume())
                                .fragile(bookedProductsDto.fragile())
                                .build()
                )
        );
    }

    @Override
    @Transactional
    public OrderDto assemblyFailedOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        return orderMapper.toOrderDto(
                orderRepository.saveAndFlush(
                        order.toBuilder()
                                .state(OrderState.ASSEMBLY_FAILED)
                                .build()
                )
        );
    }

    private Order getOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> NoOrderFoundException.builder()
                        .message("Ошибка при поиске заказа")
                        .userMessage("Заказ не найден. Пожалуйста, проверьте идентификатор")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .cause(new RuntimeException("Заказ с ID " + orderId + " не найден"))
                        .build());
    }
}
