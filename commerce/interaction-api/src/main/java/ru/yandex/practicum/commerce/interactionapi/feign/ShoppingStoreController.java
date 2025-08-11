package ru.yandex.practicum.commerce.interactionapi.feign;


import jakarta.validation.constraints.NotBlank;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.aspect.RestLogging;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductCreateDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductFullResponseDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductUpdateDto;


import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ShoppingStoreController {

    @GetMapping
    Page<ProductFullResponseDto> getProductsCategorySort(
            @RequestParam String category,
            @PageableDefault(page = 0, size = 10) Pageable pageable);


    @PutMapping
    @CachePut(value = "product", key = "#result.productId")
    @ResponseStatus(HttpStatus.CREATED)
    ProductFullResponseDto createProduct(@RequestBody @Validated ProductCreateDto createDto);


    @PostMapping
    @CachePut(value = "product", key = "#updateDto.productId")
    ProductFullResponseDto updateProduct(@RequestBody @Validated ProductUpdateDto updateDto);

    @PostMapping("/removeProductFromStore")
    @CacheEvict(value = "product", key = "#productId")
    boolean removeProductFromStore(@RequestBody @NotBlank UUID productId);

    @PostMapping("/quantityState")
    @CachePut(value = "product", key = "#stateRequest.productId")
    boolean setProductQuantityState(@RequestParam @NotBlank UUID productId, @RequestParam String quantityState);

    @GetMapping("/{productId}")
    @Cacheable(value = "product", key = "#productId")
    ProductFullResponseDto getByProductId(@PathVariable String productId);

    @GetMapping("/products")
    Map<UUID, BigDecimal> getByProductIds(@RequestBody Set<UUID> productIds);
}
