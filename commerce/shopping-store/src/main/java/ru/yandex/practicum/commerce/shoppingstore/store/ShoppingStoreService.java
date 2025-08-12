package ru.yandex.practicum.commerce.shoppingstore.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductFullResponseDto;
import ru.yandex.practicum.commerce.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductCreateDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductUpdateDto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public interface ShoppingStoreService {

    Page<ProductFullResponseDto> getProductsCategorySort(ProductCategory category, Pageable pageable);

    ProductFullResponseDto createProduct(ProductCreateDto createDto);

    ProductFullResponseDto updateProduct(ProductUpdateDto updateDto);

    boolean removeProductFromStore(UUID productId);

    boolean setProductQuantityState(ProductQuantityStateRequest stateRequest);

    ProductFullResponseDto getByProductId(String productId);

    Map<UUID, BigDecimal> getPriceMapByProductIds(Set<UUID> productId);

}
