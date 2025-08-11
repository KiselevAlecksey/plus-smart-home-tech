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
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductFullResponseDto;
import ru.yandex.practicum.commerce.interactionapi.feign.ShoppingStoreController;
import ru.yandex.practicum.commerce.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductCreateDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductUpdateDto;
import ru.yandex.practicum.commerce.interactionapi.enums.QuantityState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/shopping-store")
public class ShoppingStoreControllerImpl implements ShoppingStoreController {
    private final ShoppingStoreService storeService;

    @Override
    @GetMapping
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
    @PutMapping
    @RestLogging
    @ResponseStatus(HttpStatus.CREATED)
    public ProductFullResponseDto createProduct(@RequestBody @Validated ProductCreateDto createDto) {
        return storeService.createProduct(createDto);
    }

    @Override
    @PostMapping
    @RestLogging
    public ProductFullResponseDto updateProduct(@RequestBody @Validated ProductUpdateDto updateDto) {
        return storeService.updateProduct(updateDto);
    }

    @Override
    @PostMapping("/removeProductFromStore")
    @RestLogging
    public boolean removeProductFromStore(@RequestBody UUID productId) {
        return storeService.removeProductFromStore(productId);
    }

    @Override
    @PostMapping("/quantityState")
    @RestLogging
    public boolean setProductQuantityState(@RequestParam UUID productId, @RequestParam String quantityState) {
        QuantityState state = QuantityState.from(quantityState.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Не поддерживаемое состояние: " + quantityState));

        ProductQuantityStateRequest request = ProductQuantityStateRequest.builder()
                .productId(productId)
                .quantityState(state)
                .build();

        return storeService.setProductQuantityState(request);
    }

    @Override
    @GetMapping("/{productId}")
    @RestLogging
    public ProductFullResponseDto getByProductId(@PathVariable String productId) {
        return storeService.getByProductId(productId);
    }

    @Override
    @GetMapping("/products")
    @RestLogging
    public Map<UUID, BigDecimal> getByProductIds(@RequestBody Set<UUID> productIds) {
        return storeService.getByProductIds(productIds);
    }
}
