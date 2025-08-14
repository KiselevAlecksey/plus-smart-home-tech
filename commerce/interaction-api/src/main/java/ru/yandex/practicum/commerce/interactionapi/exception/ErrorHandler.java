package ru.yandex.practicum.commerce.interactionapi.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.interactionapi.dto.ErrorDto;

import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorHandler {
    private final ObjectMapper mapper;

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorDto handleNotAuthorizedUserException(NotAuthorizedUserException e) {
        log.warn("Ошибка в запросе:", e);
        return getBuildErrorDto(BAD_REQUEST.toString(), e);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorDto handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        log.warn("Ошибка в запросе:", e);
        return getBuildErrorDto(BAD_REQUEST.toString(), e);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorDto handleConstraintViolation(final ConstraintViolationException e) {
        log.warn("Ошибка в запросе:", e);
        return getBuildErrorDto(BAD_REQUEST.toString(), e);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        log.warn("Ошибка в запросе:", e);
        List<String> errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return getBuildErrorDto(errors, e);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorDto handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("Пустое тело запроса:", e);
        return getBuildErrorDto(BAD_REQUEST.toString(), e);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDto> handleFeign(FeignException e) throws Exception {
        return new ResponseEntity<>(mapper.readValue(e.contentUTF8(), ErrorDto.class), valueOf(e.status()));
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorDto handleProductNotFound(ProductNotFoundException e) {
        log.warn("Не найден товар:", e);
        return getBuildErrorDto(NOT_FOUND.toString(), e);
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorDto> handleException(ProductInShoppingCartLowQuantityInWarehouse e) {
        return ResponseEntity.status(e.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(getBuildErrorDto(BAD_REQUEST.toString(), e));
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorDto handleException(Throwable e) {
        log.warn("Непредвиденная ошибка:", e);
        return getBuildErrorDto(INTERNAL_SERVER_ERROR.toString(), e);
    }

    private static ErrorDto getBuildErrorDto(String code, Throwable e) {
        return ErrorDto.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(code)
                .userMessage(e.getMessage())
                .message(e.getMessage())
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    private static ErrorDto getBuildErrorDto(List<String> errors, MethodArgumentNotValidException e) {
        return ErrorDto.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(BAD_REQUEST.toString())
                .userMessage(e.getMessage())
                .message(StringUtils.join(errors, ';'))
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }
}
