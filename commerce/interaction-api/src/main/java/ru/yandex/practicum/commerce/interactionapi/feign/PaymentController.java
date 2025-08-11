package ru.yandex.practicum.commerce.interactionapi.feign;

import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.commerce.interactionapi.dto.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;

import java.math.BigDecimal;

public interface PaymentController {

    @PostMapping
    PaymentDto payment(OrderDto dto);

    @PostMapping("/totalCost")
    BigDecimal totalCost(OrderDto dto);

    @PostMapping("/refund")
    void refund(String orderId);

    @PostMapping("/productCost")
    BigDecimal productCost(OrderDto dto);

    @PostMapping("/failed")
    void failed(String paymentId);
}
