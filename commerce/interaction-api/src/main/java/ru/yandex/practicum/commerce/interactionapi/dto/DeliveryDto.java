package ru.yandex.practicum.commerce.interactionapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.enums.DeliveryState;

import java.util.UUID;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DeliveryDto(
        UUID deliveryId,
        AddressDto fromAddress,
        AddressDto toAddress,
        UUID orderId,
        DeliveryState deliveryState
) {}
