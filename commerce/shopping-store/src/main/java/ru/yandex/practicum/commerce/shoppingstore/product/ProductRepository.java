package ru.yandex.practicum.commerce.shoppingstore.product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shoppingstore.product.enums.ProductCategory;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByProductCategory(ProductCategory category, Pageable pageable);
}
