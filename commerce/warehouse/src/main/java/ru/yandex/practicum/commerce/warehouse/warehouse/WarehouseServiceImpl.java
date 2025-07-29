package ru.yandex.practicum.commerce.warehouse.warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.interactionapi.dto.ShoppingCartRequestDto;
import ru.yandex.practicum.commerce.interactionapi.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interactionapi.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.dto.AddressDto;
import ru.yandex.practicum.commerce.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.warehouse.dto.NewProductInWarehouseRequestDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final ProductInWarehouseMapper productInWarehouseMapper;

    @Override
    public void addNewProductToWarehouse(NewProductInWarehouseRequestDto request) {
        ProductInWarehouse product = productInWarehouseMapper.toEntity(request);

        warehouseRepository.save(product);
    }

    @Override
    public BookedProductsDto checkProductQuantityForShoppingCart(ShoppingCartRequestDto request) {
        BookedProductsDto bookedProductsDto = BookedProductsDto.builder()
                .deliveryWeight(0.0)
                .deliveryVolume(0.0)
                .fragile(false)
                .build();

        if (request.products().isEmpty()) return bookedProductsDto;

        Set<ProductInWarehouse> productsInCart = request.products().stream()
                .map(productInWarehouseMapper::toEntity)
                .collect(Collectors.toSet());

        Set<ProductInWarehouse> warehouseProducts = warehouseRepository.findByProductIdIn(
                productsInCart.stream()
                        .map(ProductInWarehouse::getProductId)
                        .collect(Collectors.toList())
        );

        Map<UUID, ProductInWarehouse> warehouseMap = warehouseProducts.stream()
                .collect(Collectors.toMap(ProductInWarehouse::getProductId, Function.identity()));

        productsInCart.forEach(cartProduct -> {
            ProductInWarehouse warehouseProduct = warehouseMap.get(cartProduct.getProductId());
            if (warehouseProduct == null || warehouseProduct.getQuantity() < cartProduct.getQuantity()) {
                throw ProductInShoppingCartLowQuantityInWarehouse.builder()
                        .message("Недостаточно товара на складе")
                        .userMessage("Товара " + cartProduct.getProductId()
                                + " недостаточно на складе. Доступно: "
                                + (warehouseProduct != null ? warehouseProduct.getQuantity() : null) + ", требуется: " + cartProduct.getQuantity())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .cause(new RuntimeException("Недостаточное количество товара"))
                        .build();
            }
        });

        BookedProductsDto bookedProducts = productsInCart.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        products -> {
                            double totalWeight = 0.0;
                            double totalVolume = 0.0;
                            boolean anyFragile = false;

                            for (ProductInWarehouse product : products) {
                                totalWeight += product.getWeight() * product.getQuantity();
                                totalVolume += product.getWidth() * product.getHeight() * product.getDepth() * product.getQuantity();
                                anyFragile = anyFragile || product.isFragile();
                            }

                            return BookedProductsDto.builder()
                                    .deliveryWeight(totalWeight)
                                    .deliveryVolume(totalVolume)
                                    .fragile(anyFragile)
                                    .build();
                        }
                ));

        return bookedProducts;
    }



    @Override
    public void addProductInstanceToWarehouse(AddProductToWarehouseRequest request) {
        ProductInWarehouse product = warehouseRepository.findById(request.productId())
                .orElseThrow(() -> ProductNotFoundException.builder()
                        .message("Ошибка при поиске продукта")
                        .userMessage("Продукт не найден. Пожалуйста, проверьте идентификатор")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .cause(new RuntimeException("Продукт с ID " + request.productId() + " не найден"))
                        .build());

        warehouseRepository.saveAndFlush(product.toBuilder()
                .quantity(product.getQuantity() + request.quantity())
                .build());
    }

    @Override
    public AddressDto getAddressWarehouse() {
        return null;
    }
}
