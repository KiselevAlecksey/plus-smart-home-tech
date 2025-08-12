package ru.yandex.practicum.commerce.payment.payment;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.interactionapi.dto.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.exception.BaseCustomException;
import ru.yandex.practicum.commerce.interactionapi.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interactionapi.feign.DeliveryFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feign.OrderFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feign.ShoppingStoreFeignClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {
    public static final double PERCENT_FEE = 0.1;
    PaymentMapper paymentMapper;
    PaymentRepository paymentRepository;
    ShoppingStoreFeignClient storeFeignClient;
    DeliveryFeignClient deliveryFeignClient;
    OrderFeignClient orderFeignClient;

    @Override
    public PaymentDto paymentCreate(OrderDto dto) {
        Payment payment = Payment.builder()
                .totalPayment(dto.totalPrice())
                .deliveryTotal(dto.deliveryPrice())
                .feeTotal(calculateFee(dto.productPrice()))
                .state(PaymentStatus.PENDING)
                .build();

        return paymentMapper.toPaymentDto(paymentRepository.save(payment));
    }

    @Override
    public void paymentSuccess(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(getOrderExceptionSupplier(paymentId))
                .toBuilder()
                .state(PaymentStatus.SUCCESS)
                .build();

        orderFeignClient.paymentSuccessOrder(payment.orderId);

        paymentRepository.saveAndFlush(payment);
    }

    @Override
    public BigDecimal totalCost(OrderDto dto) {
        BigDecimal fee = calculateFee(dto.productPrice());
        return dto.productPrice().add(fee).add(deliveryFeignClient.deliveryCost(dto));
    }

    @Cacheable(value = "fee", key = "#productPrice")
    private static BigDecimal calculateFee(BigDecimal productPrice) {
        return productPrice.multiply(BigDecimal.valueOf(PERCENT_FEE));
    }


    @Override
    public void refund(UUID paymentId) {
        paymentRepository.findById(paymentId).orElseThrow(getOrderExceptionSupplier(paymentId));
    }

    private static Supplier<BaseCustomException> getOrderExceptionSupplier(UUID paymentId) {
        return () -> NoOrderFoundException.builder()
                .message("Ошибка при поиске заказа")
                .userMessage("Заказ не найден. Пожалуйста, проверьте идентификатор")
                .httpStatus(HttpStatus.NOT_FOUND)
                .cause(new RuntimeException("Заказ с paymentID " + paymentId + " не найден"))
                .build();
    }

    @Override
    @Cacheable(value = "productCost", key = "#dto.orderId")
    public BigDecimal productCost(OrderDto dto) {
        Map<UUID, Long> productMap = dto.products().stream()
                .collect(Collectors.toMap(ProductDto::id, ProductDto::quantity));

        Map<UUID, BigDecimal> uuidPriceProducts = storeFeignClient.getByProductIds(productMap.keySet());

        Map<UUID, BigDecimal> totalPrices = productMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> uuidPriceProducts.get(entry.getKey()).multiply(BigDecimal.valueOf(entry.getValue())))
                );

        BigDecimal productCost = totalPrices.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return productCost;
    }

    @Override
    public void failed(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(getOrderExceptionSupplier(paymentId));
        orderFeignClient.paymentFailedOrder(payment.orderId);
        paymentRepository.saveAndFlush(payment.toBuilder().state(PaymentStatus.FAILED).build());
    }
}
