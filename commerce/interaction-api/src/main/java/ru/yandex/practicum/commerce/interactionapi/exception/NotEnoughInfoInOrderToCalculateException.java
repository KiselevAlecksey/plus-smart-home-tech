package ru.yandex.practicum.commerce.interactionapi.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

public class NotEnoughInfoInOrderToCalculateException extends BaseCustomException {
    @Builder
    protected NotEnoughInfoInOrderToCalculateException(
            String message,
            String userMessage,
            HttpStatus httpStatus,
            Throwable cause
    ) {
        super(message, userMessage, httpStatus, cause);
    }
}
