package ru.yandex.practicum.commerce.interactionapi.feign;

import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.commerce.interactionapi.dto.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentController {

    @PostMapping
    PaymentDto paymentCreate(OrderDto dto);

    @PostMapping("/success")
    void paymentSuccess(UUID paymentId);

    @PostMapping("/totalCost")
    BigDecimal totalCost(OrderDto dto);

    @PostMapping("/refund")
    void refund(UUID paymentId);

    @PostMapping("/productCost")
    BigDecimal productCost(OrderDto dto);

    @PostMapping("/failed")
    void failed(UUID paymentId);
}
