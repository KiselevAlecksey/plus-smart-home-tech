package ru.yandex.practicum.commerce.order.order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.dto.product.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "products", expression = "java(mapCartProductsToProductDtos(order.getProducts()))")
    OrderDto toOrderDto(Order order);

    @Mapping(target = "products", expression = "java(mapProductDtosToCartProducts(dto.products()))")
    Order toOrder(OrderDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    Address toAddress(AddressDto dto);

    AddressDto toAddressDto(Address address);

    default Set<ProductDto> mapCartProductsToProductDtos(Set<CartProduct> cartProducts) {
        if (cartProducts == null) {
            return Set.of();
        }
        return cartProducts.stream()
                .map(cartProduct -> new ProductDto(
                        cartProduct.getProductId(),
                        cartProduct.getQuantity()
                ))
                .collect(Collectors.toSet());
    }

    default Set<CartProduct> mapProductDtosToCartProducts(Set<ProductDto> productDtos) {
        if (productDtos == null) {
            return Set.of();
        }
        return productDtos.stream()
                .map(productDto -> {
                    CartProduct cartProduct = new CartProduct();
                    cartProduct.setProductId(productDto.id());
                    cartProduct.setQuantity(productDto.quantity());
                    return cartProduct;
                })
                .collect(Collectors.toSet());
    }
}
