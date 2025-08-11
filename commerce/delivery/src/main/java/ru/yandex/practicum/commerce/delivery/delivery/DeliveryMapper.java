package ru.yandex.practicum.commerce.delivery.delivery;

import org.mapstruct.*;
import ru.yandex.practicum.commerce.interactionapi.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryMapper {

    @Mapping(target = "fromAddress", source = "fromAddress")
    @Mapping(target = "toAddress", source = "toAddress")
    Delivery toEntity(DeliveryDto dto);

    @Mapping(target = "fromAddress", source = "fromAddress")
    @Mapping(target = "toAddress", source = "toAddress")
    DeliveryDto toDto(Delivery entity);

    @Mapping(target = "fromDeliveries", ignore = true)
    @Mapping(target = "toDeliveries", ignore = true)
    @Mapping(target = "isWarehouse", constant = "true")
    Address toFromAddressEntity(AddressDto dto);

    @Mapping(target = "fromDeliveries", ignore = true)
    @Mapping(target = "toDeliveries", ignore = true)
    @Mapping(target = "isWarehouse", ignore = true)
    Address toToAddressEntity(AddressDto dto);

    AddressDto toAddressDto(Address entity);

    @AfterMapping
    default void afterDeliveryMapping(@MappingTarget Delivery entity) {
        if (entity.getFromAddress() != null) {
            entity.getFromAddress().getFromDeliveries().add(entity);
        }
        if (entity.getToAddress() != null) {
            entity.getToAddress().getToDeliveries().add(entity);
        }
    }
}
