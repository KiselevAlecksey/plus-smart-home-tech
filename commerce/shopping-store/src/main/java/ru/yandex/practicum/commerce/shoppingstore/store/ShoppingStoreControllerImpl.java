package ru.yandex.practicum.commerce.shoppingstore.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.aspect.RestLogging;
import ru.yandex.practicum.commerce.interactionapi.dto.product.*;
import ru.yandex.practicum.commerce.interactionapi.feign.ShoppingStoreController;
import ru.yandex.practicum.commerce.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.commerce.interactionapi.enums.QuantityState;

import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreControllerImpl implements ShoppingStoreController {
    private final ShoppingStoreService storeService;

    @Override
    @RestLogging
    public Page<ProductFullResponseDto> getProductsCategorySort(
            @RequestParam String category,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        log.info("==> Get products list category {}, pageable {} start", category, pageable);

        ProductCategory productCategory = ProductCategory.from(category.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Не поддерживаемое состояние: " + category));

        Page<ProductFullResponseDto> productDtos = storeService.getProductsCategorySort(productCategory, pageable);
        log.info("<== Get products list {} end", productDtos);
        return productDtos;
    }

    @Override
    @RestLogging
    @ResponseStatus(HttpStatus.CREATED)
    public ProductFullResponseDto createProduct(ProductCreateDto createDto) {
        return storeService.createProduct(createDto);
    }

    @Override
    @RestLogging
    public ProductFullResponseDto updateProduct(ProductUpdateDto updateDto) {
        return storeService.updateProduct(updateDto);
    }

    @Override
    @RestLogging
    public boolean removeProductFromStore(UUID productId) {
        return storeService.removeProductFromStore(productId);
    }

    @Override
    @RestLogging
    public boolean setProductQuantityState(UUID productId, String quantityState) {
        QuantityState state = QuantityState.from(quantityState.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Не поддерживаемое состояние: " + quantityState));

        ProductQuantityStateRequest request = ProductQuantityStateRequest.builder()
                .productId(productId)
                .quantityState(state)
                .build();

        return storeService.setProductQuantityState(request);
    }

    @Override
    @RestLogging
    public ProductFullResponseDto getByProductId(String productId) {
        return storeService.getByProductId(productId);
    }

    @Override
    @RestLogging
    public ProductPriceDto fetchProductPricesByIds(ProductIdsDto dto) {
        return storeService.fetchProductPricesByIds(dto);
    }
}
