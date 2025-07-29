package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddressDto(
        @NotNull String country,
        @NotNull String city,
        @NotNull String street,
        @NotNull String house,
        @NotNull String flat
) {
}
