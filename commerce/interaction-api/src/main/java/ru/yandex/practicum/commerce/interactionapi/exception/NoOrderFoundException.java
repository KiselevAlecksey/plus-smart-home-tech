package ru.yandex.practicum.commerce.interactionapi.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

public class NoOrderFoundException extends BaseCustomException {
    @Builder
    protected NoOrderFoundException(
            String message,
            String userMessage,
            HttpStatus httpStatus,
            Throwable cause
    ) {
        super(message, userMessage, httpStatus, cause);
    }
}
