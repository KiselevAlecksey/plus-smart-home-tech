package ru.yandex.practicum.commerce.interactionapi.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.enums.OrderState;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record OrderDto(
        UUID orderId,
        UUID shoppingCartId,
        Set<ProductDto> products,
        UUID paymentId,
        UUID deliveryId,
        OrderState state,
        double deliveryWeight,
        double deliveryVolume,
        boolean fragile,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal totalPrice,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal deliveryPrice,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal productPrice
) {
}
