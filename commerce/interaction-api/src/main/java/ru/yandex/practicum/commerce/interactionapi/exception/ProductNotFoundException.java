package ru.yandex.practicum.commerce.interactionapi.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends BaseCustomException {
    @Builder
    protected ProductNotFoundException(
            String message,
            String userMessage,
            HttpStatus httpStatus,
            Throwable cause
    ) {
        super(message, userMessage, httpStatus, cause);
    }
}
