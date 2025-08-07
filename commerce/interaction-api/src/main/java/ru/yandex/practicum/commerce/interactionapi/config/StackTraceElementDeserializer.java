package ru.yandex.practicum.commerce.interactionapi.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class StackTraceElementDeserializer extends StdDeserializer<StackTraceElement> {
    public StackTraceElementDeserializer() {
        super(StackTraceElement.class);
    }

    @Override
    public StackTraceElement deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        String declaringClass = node.get("className").asText();
        String methodName = node.get("methodName").asText();
        String fileName = node.has("fileName") ? node.get("fileName").asText() : null;
        int lineNumber = node.get("lineNumber").asInt();
        boolean nativeMethod = node.has("nativeMethod") && node.get("nativeMethod").asBoolean();

        String moduleName = node.has("moduleName") ? node.get("moduleName").asText() : null;
        String moduleVersion = node.has("moduleVersion") ? node.get("moduleVersion").asText() : null;

        try {
            if (moduleName != null) {
                Constructor<StackTraceElement> constructor = StackTraceElement.class
                        .getConstructor(String.class, String.class, String.class,
                                String.class, String.class, String.class, int.class);
                return constructor.newInstance(
                        moduleName, moduleVersion,
                        declaringClass, methodName, fileName,
                        node.has("classLoaderName") ? node.get("classLoaderName").asText() : null,
                        lineNumber);
            } else {
                return new StackTraceElement(
                        declaringClass, methodName, fileName, lineNumber);
            }
        } catch (Exception e) {
            throw new IOException("Failed to construct StackTraceElement", e);
        }
    }
}
