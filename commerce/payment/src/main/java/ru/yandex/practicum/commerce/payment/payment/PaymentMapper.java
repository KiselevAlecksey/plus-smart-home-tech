package ru.yandex.practicum.commerce.payment.payment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.interactionapi.dto.PaymentDto;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "paymentId", source = "id")
    PaymentDto toPaymentDto(Payment payment);

    @Mapping(target = "id", source = "paymentId")
    Payment toPayment(PaymentDto dto);
}
