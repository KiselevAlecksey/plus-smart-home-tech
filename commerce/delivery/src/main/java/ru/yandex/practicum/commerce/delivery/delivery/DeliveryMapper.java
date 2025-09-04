package ru.yandex.practicum.commerce.delivery.delivery;

import org.mapstruct.*;
import ru.yandex.practicum.commerce.interactionapi.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryMapper {

    @Mapping(target = "fromAddress", ignore = true)
    @Mapping(target = "toAddress", ignore = true)
    @Mapping(target = "state", source = "deliveryState")
    Delivery toEntity(DeliveryDto dto);

    @Mapping(target = "fromAddress", source = "fromAddress")
    @Mapping(target = "toAddress", source = "toAddress")
    @Mapping(target = "deliveryState", source = "state")
    DeliveryDto toDto(Delivery entity);

    @Named("toToAddress")
    @Mapping(target = "fromDeliveries", ignore = true)
    @Mapping(target = "toDeliveries", ignore = true)
    @Mapping(target = "id", ignore = true)
    Address toAddressEntity(AddressDto dto);

    AddressDto toAddressDto(Address entity);

}
