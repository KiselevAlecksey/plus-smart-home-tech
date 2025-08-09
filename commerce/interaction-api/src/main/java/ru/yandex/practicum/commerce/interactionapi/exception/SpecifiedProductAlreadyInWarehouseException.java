package ru.yandex.practicum.commerce.interactionapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SpecifiedProductAlreadyInWarehouseException extends BaseCustomException {

    protected SpecifiedProductAlreadyInWarehouseException(
            String message,
            String userMessage,
            HttpStatus httpStatus,
            Throwable cause
    ) {
        super(message, userMessage, httpStatus, cause);
    }
}
