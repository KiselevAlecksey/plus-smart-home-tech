package ru.yandex.practicum.commerce.shoppingstore.store;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.shoppingstore.product.Product;
import ru.yandex.practicum.commerce.shoppingstore.product.ProductRepository;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.product.dto.ProductCreateDto;
import ru.yandex.practicum.commerce.shoppingstore.product.dto.ProductQuantityStateRequest;
import ru.yandex.practicum.commerce.shoppingstore.product.dto.ProductResponseDto;
import ru.yandex.practicum.commerce.shoppingstore.product.dto.ProductUpdateDto;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.product.mapper.ProductMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    final ProductRepository productRepository;

    final ProductMapper productMapper;

    @Override
    public Page<ProductResponseDto> getProductsCategorySort(ProductCategory category, Pageable pageable) {
        return productRepository.findByProductCategory(category, pageable)
                .map(productMapper::toResponseDto);
    }

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductCreateDto createDto) {
        Product product = productMapper.toEntityFromCreate(createDto);
        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(ProductUpdateDto updateDto) {
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
    @Transactional
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
    @Transactional
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
    public ProductResponseDto getByProductId(String productId) {
        try {
            return productRepository.findById(UUID.fromString(productId))
                    .map(productMapper::toResponseDto)
                    .orElseThrow(() -> ProductNotFoundException.builder()
                            .message("Ошибка при поиске продукта")
                            .userMessage("Продукт не найден. Пожалуйста, проверьте идентификатор")
                            .httpStatus(HttpStatus.NOT_FOUND)
                            .cause(new RuntimeException("Продукт с ID " + productId + " не найден"))
                            .build());
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
