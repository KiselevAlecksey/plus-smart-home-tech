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
    public void serialize(StackTraceElement value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField("className", value.getClassName());
        gen.writeStringField("methodName", value.getMethodName());
        gen.writeStringField("fileName", value.getFileName());
        gen.writeNumberField("lineNumber", value.getLineNumber());
        gen.writeBooleanField("nativeMethod", value.isNativeMethod());
        gen.writeStringField("moduleName", value.getModuleName());
        gen.writeStringField("moduleVersion", value.getModuleVersion());
        gen.writeStringField("classLoaderName", value.getClassLoaderName());
        gen.writeEndObject();
    }
}
