package ru.yandex.practicum.infra.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interactionapi.dto.ErrorDto;

@RestController
public class FallbackController {
    @GetMapping("/fallback/shopping-cart")
    public ResponseEntity<ErrorDto> shoppingCartFallback() {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ErrorDto.builder()
                        .message("Service unavailable")
                        .userMessage("Please try again later")
                        .httpStatus("SERVICE_UNAVAILABLE")
                        .build());
    }
}