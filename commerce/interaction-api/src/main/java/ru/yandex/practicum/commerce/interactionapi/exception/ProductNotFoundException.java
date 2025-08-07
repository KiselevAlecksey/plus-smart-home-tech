package ru.yandex.practicum.commerce.interactionapi.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductNotFoundException extends AbstractResourceNotFoundException {

    @Builder
    protected ProductNotFoundException(String message, String userMessage, HttpStatus httpStatus, Throwable cause) {
        super(message, userMessage, httpStatus, cause);
    }
}
