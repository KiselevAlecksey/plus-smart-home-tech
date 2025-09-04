package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;

@Builder(toBuilder = true)
public record BookedProductsDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal deliveryWeight,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal deliveryVolume,
        boolean fragile
) {
        public BookedProductsDto {
                deliveryWeight = deliveryWeight.setScale(2, RoundingMode.HALF_UP);
                deliveryVolume = deliveryVolume.setScale(2, RoundingMode.HALF_UP);
        }
}
