package ru.yandex.practicum.commerce.shoppingstore.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.product.dto.ProductCreateDto;
import ru.yandex.practicum.commerce.shoppingstore.product.dto.ProductQuantityStateRequest;
import ru.yandex.practicum.commerce.shoppingstore.product.dto.ProductResponseDto;
import ru.yandex.practicum.commerce.shoppingstore.product.dto.ProductUpdateDto;

import java.util.UUID;


public interface ShoppingStoreService {

    Page<ProductResponseDto> getProductsCategorySort(ProductCategory category, Pageable pageable);

    ProductResponseDto createProduct(ProductCreateDto createDto);

    ProductResponseDto updateProduct(ProductUpdateDto updateDto);

    boolean removeProductFromStore(UUID productId);

    boolean setProductQuantityState(ProductQuantityStateRequest stateRequest);

    ProductResponseDto getByProductId(String productId);
}
