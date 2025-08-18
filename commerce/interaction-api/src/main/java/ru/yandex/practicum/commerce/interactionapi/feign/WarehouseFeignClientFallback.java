package ru.yandex.practicum.commerce.interactionapi.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.*;

import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public BookedProductsDto assembly(AssemblyProductsForOrderRequest request) {
        log.warn("Fallback: assembly - сервис недоступен. Запрос: {}", request);
        return BookedProductsDto.builder().build();
    }

    @Override
    public void returnOrder(Set<ProductDto> productDtos) {
        log.error(
                "Fallback: Невозможно вернуть товары на склад. Сервис недоступен. " +
                        "Products: {}",
                productDtos.stream()
                        .map(ProductDto::productId)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void shipToDelivery(ShippedToDeliveryRequest request) {
        log.error(
                "Fallback: Невозможно отправить товары в доставку. Сервис склада недоступен. " +
                        "OrderId: {}, Products: {}",
                request.orderId(),
                request.deliveryId()
        );
    }
}
