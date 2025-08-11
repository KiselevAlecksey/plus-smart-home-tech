package ru.yandex.practicum.commerce.warehouse.warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interactionapi.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.NewProductInWarehouseRequestDto;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.yandex.practicum.commerce.interactionapi.Util.ADDRESSES;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final ProductInWarehouseMapper productInWarehouseMapper;
    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    @Transactional
    @CachePut(value = "product", key = "#request.productId")
    public void addNewProductToWarehouse(NewProductInWarehouseRequestDto request) {
        ProductInWarehouse product = productInWarehouseMapper.toEntity(request);
        warehouseRepository.save(product);
    }

    @Override
    @Cacheable(value = "product", key = "#request.productId")
    public BookedProductsDto checkProductQuantityForShoppingCart(ShoppingCartRequestDto request) {
        BookedProductsDto bookedProductsDto = getDefaultBookedProductsDto();

        if (request.products().isEmpty()) return bookedProductsDto;

        Set<ProductInWarehouse> productsInCart = getProductsInCart(request);
        Set<ProductInWarehouse> warehouseProducts = getWarehouseProducts(productsInCart);

        checkProductsInWarehouseOrThrow(warehouseProducts, productsInCart);
        Map<UUID, ProductInWarehouse> warehouseMap = toProductsMap(warehouseProducts);
        checkProductQuantitiesOrThrow(productsInCart, warehouseMap);

        return getBookedProductsDto(productsInCart, warehouseMap);
    }

    @Override
    @Transactional
    @CachePut(value = "product", key = "#request.productId")
    public void addProductInstanceToWarehouse(AddProductToWarehouseRequest request) {
        ProductInWarehouse product = getProductInWarehouseOrThrow(request);

        warehouseRepository.saveAndFlush(product.toBuilder()
                .quantity(product.getQuantity() + request.quantity())
                .build());
    }
    
    @Override
    @Cacheable(value = "address")
    public AddressDto getAddressWarehouse() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    private static void checkProductQuantitiesOrThrow(
            Set<ProductInWarehouse> productsInCart,
            Map<UUID, ProductInWarehouse> warehouseMap
    ) {
        productsInCart.forEach(cartProduct -> {
            ProductInWarehouse product = warehouseMap.get(cartProduct.getProductId());
            if (product == null || product.getQuantity() < cartProduct.getQuantity()) {
                throwInsufficientQuantityException(cartProduct, product);
            }
        });
    }

    private static void throwInsufficientQuantityException(
            ProductInWarehouse cartProduct,
            ProductInWarehouse warehouseProduct
    ) {
        throw ProductInShoppingCartLowQuantityInWarehouse.builder()
                .message("Недостаточно товара на складе")
                .userMessage("Товара " + cartProduct.getProductId()
                        + " недостаточно на складе. Доступно: "
                        + (warehouseProduct != null ? warehouseProduct.getQuantity() : null)
                        + ", требуется: " + cartProduct.getQuantity())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .cause(new RuntimeException("Недостаточное количество товара"))
                .build();
    }

    private static Map<UUID, ProductInWarehouse> toProductsMap(Set<ProductInWarehouse> warehouseProducts) {
        return warehouseProducts.stream()
                .collect(Collectors.toMap(ProductInWarehouse::getProductId, Function.identity()));
    }

    private static void checkProductsInWarehouseOrThrow(
            Set<ProductInWarehouse> warehouseProducts,
            Set<ProductInWarehouse> productsInCart
    ) {
        if (warehouseProducts.isEmpty()) {
            throw ProductNotFoundException.builder()
                    .message("Продукт не найден")
                    .userMessage("Товара " + productsInCart.stream()
                            + " нет в базе.")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .cause(new RuntimeException("Продукт не найден"))
                    .build();
        }
    }

    private Set<ProductInWarehouse> getWarehouseProducts(Set<ProductInWarehouse> productsInCart) {
        return warehouseRepository.findByProductIdIn(
                productsInCart.stream()
                        .map(ProductInWarehouse::getProductId)
                        .collect(Collectors.toList())
        );
    }

    private Set<ProductInWarehouse> getProductsInCart(ShoppingCartRequestDto request) {
        return request.products().stream()
                .map(productInWarehouseMapper::toEntity)
                .collect(Collectors.toSet());
    }

    private static BookedProductsDto getDefaultBookedProductsDto() {
        return BookedProductsDto.builder()
                .deliveryWeight(0.0)
                .deliveryVolume(0.0)
                .fragile(false)
                .build();
    }

    private static BookedProductsDto getBookedProductsDto(
            Set<ProductInWarehouse> productsInCart, Map<UUID,
            ProductInWarehouse> warehouseMap
    ) {
        return productsInCart.stream()
                .map(cartProduct -> getProductInWarehouseMap(warehouseMap, cartProduct))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        WarehouseServiceImpl::computeDeliveryMetrics
                ));
    }

    private static ProductInWarehouse getProductInWarehouseMap(
            Map<UUID, ProductInWarehouse> warehouseMap,
            ProductInWarehouse cartProduct
    ) {
        ProductInWarehouse warehouseProduct = warehouseMap.get(cartProduct.getProductId());

        return warehouseProduct.toBuilder()
                .quantity(cartProduct.getQuantity())
                .build();
    }

    private static BookedProductsDto computeDeliveryMetrics(List<ProductInWarehouse> products) {
        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean anyFragile = false;
        Map<UUID, BigDecimal> prices = new HashMap<>();

        for (ProductInWarehouse product : products) {
            prices.put(product.getProductId(), product.getPrice());
            totalWeight += product.getWeight() * product.getQuantity();
            totalVolume += product.getWidth() * product.getHeight()
                    * product.getDepth() * product.getQuantity();
            anyFragile = anyFragile || product.isFragile();
        }

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(anyFragile)
                .prices(prices)
                .build();
    }

    private ProductInWarehouse getProductInWarehouseOrThrow(AddProductToWarehouseRequest request) {
        return warehouseRepository.findById(request.productId())
                .orElseThrow(() -> ProductNotFoundException.builder()
                        .message("Ошибка при поиске продукта")
                        .userMessage("Продукт не найден. Пожалуйста, проверьте идентификатор")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .cause(new RuntimeException("Продукт с ID " + request.productId() + " не найден"))
                        .build());
    }
}
