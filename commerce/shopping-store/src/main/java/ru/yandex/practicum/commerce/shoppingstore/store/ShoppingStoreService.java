package ru.yandex.practicum.commerce.shoppingstore.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.interactionapi.dto.product.*;
import ru.yandex.practicum.commerce.interactionapi.enums.ProductCategory;

import java.util.UUID;

public interface ShoppingStoreService {

    Page<ProductFullResponseDto> getProductsCategorySort(ProductCategory category, Pageable pageable);

    ProductFullResponseDto createProduct(ProductCreateDto createDto);

    ProductFullResponseDto updateProduct(ProductUpdateDto updateDto);

    boolean removeProductFromStore(UUID productId);

    boolean setProductQuantityState(ProductQuantityStateRequest stateRequest);

    ProductFullResponseDto getByProductId(String productId);

    ProductPriceDto fetchProductPricesByIds(ProductIdsDto dto);

}
