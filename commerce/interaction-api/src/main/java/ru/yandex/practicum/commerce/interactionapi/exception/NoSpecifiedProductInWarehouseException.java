package ru.yandex.practicum.commerce.interactionapi.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSpecifiedProductInWarehouseException extends AbstractResourceNotFoundException {

    @Builder
    protected NoSpecifiedProductInWarehouseException(
            String message,
            String userMessage,
            HttpStatus httpStatus,
            Throwable cause
    ) {
        super(message, userMessage, httpStatus, cause);
    }
}
