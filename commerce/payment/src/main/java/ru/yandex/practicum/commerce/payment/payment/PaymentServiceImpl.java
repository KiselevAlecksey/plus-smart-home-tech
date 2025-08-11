package ru.yandex.practicum.commerce.payment.payment;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.interactionapi.dto.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.feign.DeliveryFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feign.ShoppingStoreFeignClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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


    @Override
    public PaymentDto payment(OrderDto dto) {

        return null;
    }

    @Override
    public BigDecimal totalCost(OrderDto dto) {
        BigDecimal fee = dto.productPrice().multiply(BigDecimal.valueOf(PERCENT_FEE));

        BigDecimal totalCost = dto.productPrice().add(fee).add(deliveryFeignClient.deliveryCost(dto));

        return totalCost;
    }

    @Override
    public void refund(String orderId) {

    }

    @Override
    @Cacheable(value = "productCost", key = "#dto.paymentId")
    public BigDecimal productCost(OrderDto dto) {
        Map<UUID, Long> productMap = dto.products().stream()
                .collect(Collectors.toMap(ProductDto::productId, ProductDto::quantity));

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
    public void failed(String paymentId) {

    }
}
