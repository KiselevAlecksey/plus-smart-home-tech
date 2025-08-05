package ru.yandex.practicum.commerce.interactionapi.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Getter
public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException{
    private final Instant timestamp;
    private final HttpStatus httpStatus;
    private final String userMessage;
    private final List<StackTraceElement> stackTraceElements;
    private final List<Throwable> suppressedExceptions;

    @Builder
    private SpecifiedProductAlreadyInWarehouseException(String message, String userMessage, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.timestamp = Instant.now();
        this.httpStatus = httpStatus;
        this.userMessage = userMessage;
        this.stackTraceElements = Arrays.asList(this.getStackTrace());
        this.suppressedExceptions = Arrays.asList(this.getSuppressed());
    }

    @Getter
    public static class StackTraceElementDetail {
        private final String classLoaderName;
        private final String moduleName;
        private final String moduleVersion;
        private final String methodName;
        private final String fileName;
        private final int lineNumber;
        private final String className;
        private final boolean nativeMethod;

        public StackTraceElementDetail(StackTraceElement element) {
            this.classLoaderName = element.getClassLoaderName();
            this.moduleName = element.getModuleName();
            this.moduleVersion = element.getModuleVersion();
            this.methodName = element.getMethodName();
            this.fileName = element.getFileName();
            this.lineNumber = element.getLineNumber();
            this.className = element.getClassName();
            this.nativeMethod = element.isNativeMethod();
        }
    }
}
