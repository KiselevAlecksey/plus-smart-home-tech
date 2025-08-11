package ru.yandex.practicum.commerce.interactionapi.exception;

import org.springframework.http.HttpStatus;

public class ShoppingCartNotFoundException extends BaseCustomException {

    protected ShoppingCartNotFoundException(
            String message,
            String userMessage,
            HttpStatus httpStatus,
            Throwable cause
    ) {
        super(message, userMessage, httpStatus, cause);
    }
}
