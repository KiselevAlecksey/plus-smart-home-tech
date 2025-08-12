package ru.yandex.practicum.commerce.warehouse.warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.*;
import ru.yandex.practicum.commerce.interactionapi.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interactionapi.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.interactionapi.feign.DeliveryFeignClient;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.yandex.practicum.commerce.interactionapi.Util.ADDRESSES;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final ProductInWarehouseMapper productInWarehouseMapper;
    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    private final OrderBookingRepository orderBookingRepository;
    private final DeliveryFeignClient deliveryFeignClient;

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
        if (request.products().isEmpty()) return getDefaultBookedProductsDto();
        Set<ProductInWarehouse> productsInCart = getProductsSetFromDto(request.products().stream());
        Map<UUID, ProductInWarehouse> warehouseProducts = getWarehouseProductsMap(productsInCart);
        checkProductQuantitiesOrThrow(productsInCart, warehouseProducts);

        return getBookedProductsDto(productsInCart, warehouseProducts);
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

    @Override
    @Transactional
    public BookedProductsDto assembly(AssemblyProductsForOrderRequest request) {
        Map<UUID, ProductInWarehouse> orderProductsMap = request.products().stream()
                .collect(Collectors.toMap(ProductDto::id, productInWarehouseMapper::toEntity));
        Set<ProductInWarehouse> products = getProductsSetFromDto(request.products().stream());
        Map<UUID, ProductInWarehouse> warehouseProductsMap = getWarehouseProductsMap(products);

        checkProductQuantitiesOrThrow(products, warehouseProductsMap);

        Map<UUID, ProductInWarehouse> warehouseProductsUpdated = warehouseProductsMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            ProductInWarehouse product = entry.getValue();
                            long newQuantity = product.getQuantity() - orderProductsMap.get(entry.getKey()).getQuantity();
                            return product.toBuilder()
                                    .quantity(newQuantity)
                                    .build();
                        })
                );

        warehouseRepository.saveAll(warehouseProductsUpdated.values());

        OrderBooking booking = OrderBooking.builder()
                .id(UUID.randomUUID())
                .orderId(request.orderId())
                .build();

        request.products().forEach(productDto -> {
            BookedProductItem.builder()
                    .orderBooking(booking)
                    .productInWarehouse(warehouseProductsMap.get(productDto.id()))
                    .quantity(productDto.quantity())
                    .build();
        });

        orderBookingRepository.save(booking);
        return getBookedProductsDto(products, warehouseProductsMap);
    }

    @Override
    @Transactional
    public void returnOrder(Set<ProductDto> productDtos) {
        Set<ProductInWarehouse> products = getProductsSetFromDto(productDtos.stream());

        Map<UUID, ProductInWarehouse> warehouseProducts = getWarehouseProductsMap(products);

        List<ProductInWarehouse> productsToUpdate = productDtos.stream()
                .map(dto -> {
                    ProductInWarehouse product = warehouseProducts.get(dto.id());
                    long newQuantity = product.getQuantity() + dto.quantity();
                    return product.toBuilder()
                            .quantity(newQuantity)
                            .build();
                })
                .toList();

        warehouseRepository.saveAll(productsToUpdate);
    }

    @Override
    public void shipToDelivery(ShippedToDeliveryRequest request) {
        OrderBooking orderBooking = orderBookingRepository.findByOrderId(request.orderId())
                .orElseThrow();

        OrderBooking updatedOrderBooking = orderBooking.toBuilder().deliveryId(request.deliveryId()).build();

        orderBookingRepository.save(updatedOrderBooking);
    }


    private void checkProductQuantitiesOrThrow(
            Set<ProductInWarehouse> productsInCart,
            Map<UUID, ProductInWarehouse> warehouseProducts
    ) {

        checkProductsInWarehouseOrThrow(warehouseProducts, productsInCart);

        productsInCart.forEach(cartProduct -> {
            ProductInWarehouse product = warehouseProducts.get(cartProduct.getId());
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
                .userMessage("Товара " + cartProduct.getId()
                        + " недостаточно на складе. Доступно: "
                        + (warehouseProduct != null ? warehouseProduct.getQuantity() : null)
                        + ", требуется: " + cartProduct.getQuantity())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .cause(new RuntimeException("Недостаточное количество товара"))
                .build();
    }

    private static void checkProductsInWarehouseOrThrow(
            Map<UUID, ProductInWarehouse> warehouseMap,
            Set<ProductInWarehouse> productsInCart
    ) {
        if (warehouseMap.isEmpty()) {
            throw ProductNotFoundException.builder()
                    .message("Продукт не найден")
                    .userMessage("Товара " + productsInCart.stream()
                            + " нет в базе.")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .cause(new RuntimeException("Продукт не найден"))
                    .build();
        }
    }

    private Map<UUID, ProductInWarehouse> getWarehouseProductsMap(Set<ProductInWarehouse> productsInCart) {
        return warehouseRepository.findByIdIn(
                productsInCart.stream()
                        .map(ProductInWarehouse::getId)
                        .collect(Collectors.toList())
                ).stream()
                .collect(Collectors.toMap(
                        ProductInWarehouse::getId,
                        Function.identity())
                );
    }

    private Set<ProductInWarehouse> getProductsSetFromDto(Stream<ProductDto> stream) {
        return stream
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
            Set<ProductInWarehouse> productsInCart,
            Map<UUID, ProductInWarehouse> warehouseMap
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
        ProductInWarehouse warehouseProduct = warehouseMap.get(cartProduct.getId());

        return warehouseProduct.toBuilder()
                .quantity(cartProduct.getQuantity())
                .build();
    }

    private static BookedProductsDto computeDeliveryMetrics(List<ProductInWarehouse> products) {
        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean anyFragile = false;

        for (ProductInWarehouse product : products) {
            totalWeight += product.getWeight() * product.getQuantity();
            totalVolume += product.getWidth() * product.getHeight()
                    * product.getDepth() * product.getQuantity();
            anyFragile = anyFragile || product.isFragile();
        }

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(anyFragile)
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
