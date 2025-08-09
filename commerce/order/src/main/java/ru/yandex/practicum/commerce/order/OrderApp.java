package ru.yandex.practicum.commerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.commerce.interactionapi.aspect.RestLoggingAspect;
import ru.yandex.practicum.commerce.interactionapi.config.CommonConfig;
import ru.yandex.practicum.commerce.interactionapi.exception.ErrorHandler;

@SpringBootApplication
@EnableDiscoveryClient
@Import({ErrorHandler.class, RestLoggingAspect.class, CommonConfig.class})
public class OrderApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class, args);
    }
}
