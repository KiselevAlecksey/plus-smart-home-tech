package ru.yandex.practicum.commerce.payment.payment;

import ru.yandex.practicum.commerce.interactionapi.dto.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.feign.PaymentController;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService extends PaymentController {
    @Override
    PaymentDto paymentCreate(OrderDto dto);

    @Override
    void paymentSuccess(UUID paymentId);

    @Override
    BigDecimal totalCost(OrderDto dto);

    @Override
    void refund(UUID paymentId);

    @Override
    BigDecimal productCost(OrderDto dto);

    @Override
    void failed(UUID paymentId);
}
