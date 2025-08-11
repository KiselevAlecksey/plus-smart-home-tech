package ru.yandex.practicum.commerce.interactionapi.exception;

import org.springframework.http.HttpStatus;

public class NoSpecifiedProductInWarehouseException extends BaseCustomException {

    protected NoSpecifiedProductInWarehouseException(
            String message,
            String userMessage,
            HttpStatus httpStatus,
            Throwable cause
    ) {
        super(message, userMessage, httpStatus, cause);
    }
}
