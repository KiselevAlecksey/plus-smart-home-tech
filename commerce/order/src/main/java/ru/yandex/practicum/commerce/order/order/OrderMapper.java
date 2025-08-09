package ru.yandex.practicum.commerce.order.order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "products", expression = "java(mapCartProductsToMap(order.getProducts()))")
    OrderDto toOrderDto(Order order);

    @Mapping(target = "products", expression = "java(mapProductMapToCartProducts(dto.products()))")
    Order toOrder(OrderDto dto);

    default Map<UUID, Long> mapCartProductsToMap(Set<CartProduct> cartProducts) {
        if (cartProducts == null) return Map.of();
        return cartProducts.stream()
                .collect(Collectors.toMap(
                        CartProduct::getProductId,
                        CartProduct::getQuantity
                ));
    }

    default Set<CartProduct> mapProductMapToCartProducts(Map<UUID, Long> productMap) {
        return productMap.entrySet().stream()
                .map(entry -> {
                    CartProduct cartProduct = new CartProduct();
                    cartProduct.setProductId(entry.getKey());
                    cartProduct.setQuantity(entry.getValue());
                    return cartProduct;
                })
                .collect(Collectors.toSet());
    }
}
