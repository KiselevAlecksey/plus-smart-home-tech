package ru.yandex.practicum.commerce.interactionapi.exception;

import org.springframework.http.HttpStatus;

public class NotEnoughInfoInOrderToCalculateException extends BaseCustomException {

    protected NotEnoughInfoInOrderToCalculateException(
            String message,
            String userMessage,
            HttpStatus httpStatus,
            Throwable cause
    ) {
        super(message, userMessage, httpStatus, cause);
    }
}
