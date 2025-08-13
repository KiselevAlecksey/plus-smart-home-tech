package ru.yandex.practicum.commerce.interactionapi.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.enums.OrderState;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
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

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal deliveryWeight,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal deliveryVolume,

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
        public OrderDto {
                totalPrice = totalPrice != null ? totalPrice.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                deliveryPrice = deliveryPrice != null ? deliveryPrice.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                productPrice = productPrice != null ? productPrice.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

                deliveryWeight = deliveryWeight != null ? deliveryWeight.setScale(2, RoundingMode.HALF_UP) : null;
                deliveryVolume = deliveryVolume != null ? deliveryVolume.setScale(2, RoundingMode.HALF_UP) : null;
        }
}
