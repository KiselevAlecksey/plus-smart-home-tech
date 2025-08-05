package ru.yandex.practicum.commerce.interactionapi.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.commerce.interactionapi.dto.ErrorDto;
import ru.yandex.practicum.commerce.interactionapi.exception.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FeignErrorConfig implements ErrorDecoder {
    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
            byte[] bodyBytes;
            String rawBody;
        ErrorDto errorDto;
            try {
                bodyBytes = response.body().asInputStream().readAllBytes();
                rawBody = new String(bodyBytes, StandardCharsets.UTF_8);
                if (StringUtils.isBlank(rawBody)) {
                    return new Default().decode(methodKey, response);
                }

                errorDto = objectMapper.readValue(rawBody, ErrorDto.class);
            } catch (IOException e) {
                log.error("Failed to read response body", e);
                return createFallbackException(status, "Failed to read response body");
            }

            HttpStatus httpStatus = HttpStatus.valueOf(status);

        Exception exception = switch (httpStatus) {
            case BAD_REQUEST -> handleBadRequest(errorDto);
            case NOT_FOUND -> handleNotFound(errorDto);
            case CONFLICT -> buildSpecifiedProductException(errorDto);
            default -> new Default().decode(methodKey, response);
        };

        return exception;
    }

    private Exception createFallbackException(int status, String message) {
        return new CustomFeignException(
                ErrorDto.builder()
                        .message(message)
                        .userMessage("Service unavailable")
                        .httpStatus(String.valueOf(status))
                        .build(),
                status,
                "fallback"
        );
    }

    private Exception buildSpecifiedProductException(ErrorDto errorDto) {
        return SpecifiedProductAlreadyInWarehouseException.builder()
                .message(errorDto.getMessage())
                .userMessage(errorDto.getUserMessage())
                .httpStatus(HttpStatus.CONFLICT)
                .cause(new RuntimeException(errorDto.getCause()))
                .build();
    }

    private Exception handleBadRequest(ErrorDto errorDto) {
        if (errorDto.getMessage().contains("Недостаточно товара на складе")) {
            return ProductInShoppingCartLowQuantityInWarehouse.builder()
                    .message(errorDto.getMessage())
                    .userMessage(errorDto.getUserMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .cause(new RuntimeException(errorDto.getCause()))
                    .build();
        }
        return new RuntimeException(errorDto.getMessage());
    }

    private Exception handleNotFound(ErrorDto errorDto) {
        if (errorDto.getMessage().contains("Продукт не найден")) {
            return ProductNotFoundException.builder()
                    .message(errorDto.getMessage())
                    .userMessage(errorDto.getUserMessage())
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .cause(new RuntimeException(errorDto.getCause()))
                    .build();
        } else if (errorDto.getMessage().contains("Корзина не найдена")) {
            return ShoppingCartNotFoundException.builder()
                    .message(errorDto.getMessage())
                    .userMessage(errorDto.getUserMessage())
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .cause(new RuntimeException(errorDto.getCause()))
                    .build();
        } else if (errorDto.getMessage().contains("Указанный продукт отсутствует")) {
            return NoSpecifiedProductInWarehouseException.builder()
                    .message(errorDto.getMessage())
                    .userMessage(errorDto.getUserMessage())
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .cause(new RuntimeException(errorDto.getCause()))
                    .build();
        }
        return new RuntimeException(errorDto.getMessage());
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new SimpleModule() {
            {
                addSerializer(Throwable.class, new ThrowableSerializer());
                addDeserializer(Throwable.class, new ThrowableDeserializer());
                addSerializer(StackTraceElement.class, new StackTraceElementSerializer());
                addDeserializer(StackTraceElement.class, new StackTraceElementDeserializer());
            }
        });

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static class ThrowableSerializer extends StdSerializer<Throwable> {
        public ThrowableSerializer() {
            super(Throwable.class);
        }

        @Override
        public void serialize(Throwable value, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
            gen.writeStartObject();
            gen.writeStringField("exceptionClass", value.getClass().getName());
            gen.writeStringField("message", value.getMessage());
            gen.writeObjectField("stackTrace", value.getStackTrace());
            gen.writeObjectField("cause", value.getCause());
            gen.writeStringField("localizedMessage", value.getLocalizedMessage());
            gen.writeEndObject();
        }
    }

    public static class ThrowableDeserializer extends StdDeserializer<Throwable> {
        public ThrowableDeserializer() {
            super(Throwable.class);
        }

        @Override
        public Throwable deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            String message = node.get("message").asText();
            Throwable cause = p.getCodec().treeToValue(node.get("cause"), Throwable.class);
            StackTraceElement[] stackTrace = p.getCodec().treeToValue(node.get("stackTrace"), StackTraceElement[].class);

            Throwable throwable = new Throwable(message, cause);
            throwable.setStackTrace(stackTrace);
            return throwable;
        }
    }
}


@Getter
class CustomFeignException extends FeignException {
    private final ErrorDto errorDto;

    public CustomFeignException(ErrorDto errorDto, int status, String methodKey) {
        super(status, "Feign client error: " + methodKey + " - " + errorDto.getMessage());
        this.errorDto = errorDto;
    }
}

