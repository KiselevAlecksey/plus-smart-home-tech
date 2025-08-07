package ru.yandex.practicum.commerce.interactionapi.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.NewProductInWarehouseRequestDto;

@Component
public class WarehouseFeignClientFallback implements WarehouseFeignClient {

    private static final Logger log = LoggerFactory.getLogger(WarehouseFeignClientFallback.class);

    @Override
    public void addProductToWarehouse(NewProductInWarehouseRequestDto request) {
        log.warn("Fallback: addProductToWarehouse - сервис недоступен. Запрос: {}", request);
    }

    @Override
    public BookedProductsDto checkProductQuantityForShoppingCart(String headerValue, ShoppingCartRequestDto request) {
        log.warn("Fallback: checkProductQuantityForShoppingCart - сервис недоступен. Запрос: {}", request);

        return BookedProductsDto.builder().build();
    }

    @Override
    public void addProductInstanceToWarehouse(AddProductToWarehouseRequest request) {
        log.warn("Fallback: addProductInstanceToWarehouse - сервис недоступен. Запрос: {}", request);
    }

    @Override
    public AddressDto getAddressWarehouse() {
        log.warn("Fallback: getAddressWarehouse - сервис недоступен");
        return AddressDto.builder()
                .country("Адрес не доступен")
                .build();
    }
}
