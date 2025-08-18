package ru.yandex.practicum.commerce.interactionapi.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

public class ShoppingCartNotFoundException extends BaseCustomException {
    @Builder
    protected ShoppingCartNotFoundException(
            String message,
            String userMessage,
            HttpStatus httpStatus,
            Throwable cause
    ) {
        super(message, userMessage, httpStatus, cause);
    }
}
