package ru.yandex.practicum.commerce.interactionapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductInShoppingCartLowQuantityInWarehouse extends BaseCustomException {

    public ProductInShoppingCartLowQuantityInWarehouse(
            String message,
            String userMessage,
            HttpStatus httpStatus,
            Throwable cause
    ) {
        super(
                message != null ? message : "Недостаточно товара на складе",
                userMessage != null ? userMessage : "Пожалуйста, уменьшите количество",
                httpStatus != null ? httpStatus : HttpStatus.BAD_REQUEST,
                cause
        );
    }
}
