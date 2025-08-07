package ru.yandex.practicum.commerce.shoppingcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.commerce.interactionapi.aspect.RestLoggingAspect;
import ru.yandex.practicum.commerce.interactionapi.config.CommonConfig;
import ru.yandex.practicum.commerce.interactionapi.exception.ErrorHandler;

@SpringBootApplication
@EnableFeignClients(basePackages = "ru.yandex.practicum.commerce.interactionapi.feign")
@EnableAspectJAutoProxy
@Import({ErrorHandler.class, RestLoggingAspect.class, CommonConfig.class})
public class ShoppingCartApp {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApp.class, args);
    }
}
