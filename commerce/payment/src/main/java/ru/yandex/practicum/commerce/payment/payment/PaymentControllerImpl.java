package ru.yandex.practicum.commerce.payment.payment;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interactionapi.aspect.RestLogging;
import ru.yandex.practicum.commerce.interactionapi.dto.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.feign.PaymentController;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/payment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentControllerImpl implements PaymentController {
    final PaymentService paymentService;


    @Override
    @RestLogging
    @PostMapping
    public PaymentDto payment(@RequestBody OrderDto dto) {
        return paymentService.payment(dto);
    }

    @Override
    @RestLogging
    @PostMapping("/totalCost")
    public BigDecimal totalCost(@RequestBody OrderDto dto) {
        return paymentService.totalCost(dto);
    }

    @Override
    @RestLogging
    @PostMapping("/refund")
    public void refund(@RequestBody String orderId) {
        paymentService.refund(orderId);
    }

    @Override
    @RestLogging
    @PostMapping("/productCost")
    public BigDecimal productCost(@RequestBody OrderDto dto) {
        return paymentService.productCost(dto);
    }

    @Override
    @RestLogging
    @PostMapping("/failed")
    public void failed(@RequestBody String paymentId) {
        paymentService.failed(paymentId);
    }
}
