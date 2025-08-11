package ru.yandex.practicum.commerce.payment.payment;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.interactionapi.dto.PaymentDto;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDto toPaymentDto(Payment payment);

    Payment toPayment(PaymentDto dto);
}
