package ru.yandex.practicum.commerce.shoppingstore.store;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.dto.product.*;
import ru.yandex.practicum.commerce.interactionapi.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.shoppingstore.product.Product;
import ru.yandex.practicum.commerce.shoppingstore.product.ProductRepository;
import ru.yandex.practicum.commerce.interactionapi.enums.ProductState;
import ru.yandex.practicum.commerce.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.product.ProductMapper;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    final ProductRepository productRepository;

    final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#categoty")
    public Page<ProductFullResponseDto> getProductsCategorySort(ProductCategory category, Pageable pageable) {
        return productRepository.findByProductCategory(category, pageable)
                .map(productMapper::toResponseDto);
    }

    @Override
    @CachePut(value = "product", key = "#result.id")
    public ProductFullResponseDto createProduct(ProductCreateDto createDto) {
        Product product = productMapper.toEntityFromCreate(createDto);
        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Override
    @CachePut(value = "product", key = "#updateDto.productId")
    public ProductFullResponseDto updateProduct(ProductUpdateDto updateDto) {
        Product product = productRepository.findById(updateDto.productId())
                .orElseThrow(() -> ProductNotFoundException.builder()
                        .message("Ошибка при поиске продукта")
                        .userMessage("Продукт не найден. Пожалуйста, проверьте идентификатор")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .cause(new RuntimeException("Продукт с ID " + updateDto.productId() + " не найден"))
                        .build());

        productMapper.updateProductFromDto(updateDto, product);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponseDto(updatedProduct);
    }

    @Override
    @CacheEvict(value = "product", key = "#productId")
    public boolean removeProductFromStore(UUID productId) {
        try {
            return productRepository.findById(productId)
                    .map(product -> {
                        product.setProductState(ProductState.DEACTIVATE);
                        productRepository.saveAndFlush(product);
                        return true;
                    })
                    .orElse(false);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    @CachePut(value = "product", key = "#stateRequest.productId")
    public boolean setProductQuantityState(ProductQuantityStateRequest stateRequest) {
        try {
            return productRepository.findById(stateRequest.productId())
                    .map(product -> {
                        productMapper.updateQuantityStateFromDto(stateRequest, product);
                        productRepository.save(product);
                        return true;
                    })
                    .orElse(false);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    @Cacheable(value = "product", key = "#productId")
    @Transactional(readOnly = true)
    public ProductFullResponseDto getByProductId(String productId) {
            return productRepository.findById(UUID.fromString(productId))
                    .map(productMapper::toResponseDto)
                    .orElseThrow(() -> ProductNotFoundException.builder()
                            .message("Ошибка при поиске продукта")
                            .userMessage("Продукт не найден. Пожалуйста, проверьте идентификатор")
                            .httpStatus(HttpStatus.NOT_FOUND)
                            .cause(new RuntimeException("Продукт с ID " + productId + " не найден"))
                            .build());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPriceDto fetchProductPricesByIds(ProductIdsDto dto) {
        try {
            return new ProductPriceDto(productRepository.findAllByIdIn(dto.productIds()).stream()
                    .collect(Collectors.toMap(Product::getId, Product::getPrice)));
        } catch (IllegalArgumentException e) {
            throw ProductNotFoundException.builder()
                    .message("Невалидный идентификатор продукта")
                    .userMessage("Некорректный формат идентификатора продукта")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .cause(e)
                    .build();
        }
    }
}
