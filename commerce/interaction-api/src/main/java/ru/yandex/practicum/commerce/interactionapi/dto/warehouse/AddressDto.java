package ru.yandex.practicum.commerce.interactionapi.dto.warehouse;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AddressDto(
        @Null UUID id,
        @NotNull String country,
        @NotNull String city,
        @NotNull String street,
        @NotNull String house,
        @NotNull String flat
) {
}
