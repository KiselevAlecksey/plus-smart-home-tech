package ru.yandex.practicum.commerce.interactionapi;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ErrorDto {
    private Throwable cause;

    private StackTraceElement[] stackTrace;

    private String httpStatus;

    private String userMessage;

    private String message;

    private Throwable[] suppressed;

    private String localizedMessage;
}
