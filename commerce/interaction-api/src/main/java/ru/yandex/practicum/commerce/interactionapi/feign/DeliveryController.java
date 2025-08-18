package ru.yandex.practicum.commerce.interactionapi.feign;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.aspect.RestLogging;
import ru.yandex.practicum.commerce.interactionapi.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.ShippedToDeliveryRequest;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryController {

    @PutMapping
    @RestLogging
    DeliveryDto planDelivery(@RequestBody @Valid DeliveryDto deliveryDto);

    @PostMapping("/successful")
    @RestLogging
    void deliverySuccessful(@RequestBody UUID deliveryId);

    @PostMapping("/picked")
    @RestLogging
    void deliveryPicked(@RequestBody UUID deliveryId);

    @PostMapping("/failed")
    @RestLogging
    void deliveryFailed(@RequestBody UUID deliveryId);

    @PostMapping("/cost")
    @RestLogging
    BigDecimal deliveryCost(@RequestBody @Valid OrderDto orderDto);

    @RestLogging
    @GetMapping("/{orderId}")
    UUID getDeliveryId(@PathVariable String orderId);
}
