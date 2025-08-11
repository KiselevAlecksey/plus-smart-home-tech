package ru.yandex.practicum.commerce.payment.payment;

import ru.yandex.practicum.commerce.interactionapi.dto.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.feign.PaymentController;

import java.math.BigDecimal;

public interface PaymentService extends PaymentController {
    @Override
    PaymentDto payment(OrderDto dto);

    @Override
    BigDecimal totalCost(OrderDto dto);

    @Override
    void refund(String orderId);

    @Override
    BigDecimal productCost(OrderDto dto);

    @Override
    void failed(String paymentId);
}
