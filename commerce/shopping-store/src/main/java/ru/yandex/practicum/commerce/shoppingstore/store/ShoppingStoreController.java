package ru.yandex.practicum.commerce.shoppingstore.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.product.dto.ProductCreateDto;
import ru.yandex.practicum.commerce.shoppingstore.product.dto.ProductQuantityStateRequest;
import ru.yandex.practicum.commerce.shoppingstore.product.dto.ProductResponseDto;
import ru.yandex.practicum.commerce.shoppingstore.product.dto.ProductUpdateDto;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.QuantityState;

import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/shopping-store")
public class ShoppingStoreController {
    private final ShoppingStoreService storeService;

    @GetMapping
    public Page<ProductResponseDto> getProductsCategorySort(
            @RequestParam String category,
            @PageableDefault(page = 0, size = 10)  Pageable pageable) {
        log.info("==> Get products list category {}, pageable {} start", category, pageable);

        ProductCategory productCategory = ProductCategory.from(category.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Не поддерживаемое состояние: " + category));

        Page<ProductResponseDto> productDtos = storeService.getProductsCategorySort(productCategory, pageable);
        log.info("<== Get products list {} end", productDtos);
        return productDtos;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(@RequestBody @Validated ProductCreateDto createDto) {
        log.info("==> Create product {} start", createDto);
        ProductResponseDto responseDto = storeService.createProduct(createDto);
        log.info("==> Create product {} end", responseDto);
        return responseDto;
    }

    @PostMapping
    public ProductResponseDto updateProduct(@RequestBody @Validated ProductUpdateDto updateDto) {
        log.info("==> Update product {} start", updateDto);
        ProductResponseDto responseDto = storeService.updateProduct(updateDto);
        log.info("==> Update product {} end", updateDto);
        return responseDto;
    }

    @PostMapping("/removeProductFromStore")
    public boolean removeProductFromStore(@RequestBody UUID productId) {
        log.info("==> Update product {} start", productId);
        boolean bool = storeService.removeProductFromStore(productId);
        log.info("==> Update product {} end", bool);
        return bool;
    }

    @PostMapping("/quantityState")
    public boolean setProductQuantityState(@RequestParam UUID productId, @RequestParam String quantityState) {
        log.info("==> Update product productId {}, quantityState {} start", productId, quantityState);
        QuantityState state = QuantityState.from(quantityState.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Не поддерживаемое состояние: " + quantityState));

        ProductQuantityStateRequest request = ProductQuantityStateRequest.builder()
                .productId(productId)
                .quantityState(state)
                .build();

        boolean bool = storeService.setProductQuantityState(request);
        log.info("==> Update product {} end", bool);
        return bool;
    }

    @GetMapping("/{productId}")
    public ProductResponseDto getByProductId(@PathVariable String productId) {
        log.info("==> Update product {} start", productId);
        ProductResponseDto responseDto = storeService.getByProductId(productId);
        log.info("==> Update product {} end", responseDto);
        return responseDto;
    }
}
