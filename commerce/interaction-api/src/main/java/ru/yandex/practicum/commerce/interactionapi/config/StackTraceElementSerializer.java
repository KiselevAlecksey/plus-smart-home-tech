package ru.yandex.practicum.commerce.interactionapi.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class StackTraceElementSerializer extends StdSerializer<StackTraceElement> {
    public StackTraceElementSerializer() {
        super(StackTraceElement.class);
    }

    @Override
    public void serialize(StackTraceElement value, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        generator.writeStartObject();
        generator.writeStringField("className", value.getClassName());
        generator.writeStringField("methodName", value.getMethodName());
        generator.writeStringField("fileName", value.getFileName());
        generator.writeNumberField("lineNumber", value.getLineNumber());
        generator.writeBooleanField("nativeMethod", value.isNativeMethod());
        generator.writeStringField("moduleName", value.getModuleName());
        generator.writeStringField("moduleVersion", value.getModuleVersion());
        generator.writeStringField("classLoaderName", value.getClassLoaderName());
        generator.writeEndObject();
    }
}
