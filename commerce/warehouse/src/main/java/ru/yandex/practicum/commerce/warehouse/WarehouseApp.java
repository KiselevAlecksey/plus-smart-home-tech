package ru.yandex.practicum.commerce.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.commerce.interactionapi.exception.ErrorHandler;

@SpringBootApplication
@EnableFeignClients(basePackages = "ru.yandex.practicum.commerce.interactionapi.feign")
@Import(ErrorHandler.class)
public class WarehouseApp {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseApp.class, args);
    }
}
