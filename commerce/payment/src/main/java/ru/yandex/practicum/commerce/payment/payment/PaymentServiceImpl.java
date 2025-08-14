package ru.yandex.practicum.commerce.payment.payment;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductIdsDto;
import ru.yandex.practicum.commerce.interactionapi.exception.BaseCustomException;
import ru.yandex.practicum.commerce.interactionapi.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interactionapi.feign.DeliveryFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feign.OrderFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feign.ShoppingStoreFeignClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ru.yandex.practicum.commerce.interactionapi.Util.PERCENT_FEE;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {
    PaymentMapper paymentMapper;
    PaymentRepository paymentRepository;
    ShoppingStoreFeignClient storeFeignClient;
    DeliveryFeignClient deliveryFeignClient;
    OrderFeignClient orderFeignClient;

    @Override
    @Transactional
    public PaymentDto paymentCreate(OrderDto dto) {
        Payment payment = Payment.builder()
                .totalPayment(dto.totalPrice())
                .deliveryTotal(dto.deliveryPrice())
                .feeTotal(calculateFee(dto.productPrice()))
                .state(PaymentStatus.PENDING)
                .orderId(dto.orderId())
                .build();

        return paymentMapper.toPaymentDto(paymentRepository.save(payment));
    }

    @Override
    @Transactional
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
    @Transactional
    public BigDecimal totalCost(OrderDto dto) {
        BigDecimal fee = calculateFee(dto.productPrice());
        return dto.productPrice().add(fee).add(deliveryFeignClient.deliveryCost(dto)).setScale(2, RoundingMode.HALF_UP);
    }

    @Cacheable(value = "fee")
    private static BigDecimal calculateFee(BigDecimal productPrice) {
        return productPrice.multiply(PERCENT_FEE).setScale(2, RoundingMode.HALF_UP);
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
                .collect(Collectors.toMap(ProductDto::productId, ProductDto::quantity));
        Set<UUID> products = productMap.keySet();

        Map<UUID, BigDecimal> uuidPriceProducts = storeFeignClient.fetchProductPricesByIds(
                new ProductIdsDto(products)).productPricesMap();

        System.out.println(uuidPriceProducts);
        Map<UUID, BigDecimal> totalPrices = productMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> uuidPriceProducts.get(entry.getKey()).multiply(BigDecimal.valueOf(entry.getValue())))
                );

        return totalPrices.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional
    public void failed(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(getOrderExceptionSupplier(paymentId));
        orderFeignClient.paymentFailedOrder(payment.orderId);
        paymentRepository.saveAndFlush(payment.toBuilder().state(PaymentStatus.FAILED).build());
    }
}
