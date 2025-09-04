package ru.yandex.practicum.commerce.delivery.delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findByCountryAndCityAndStreetAndHouseAndFlat(
            String country,
            String city,
            String street,
            String house,
            String flat
    );
}
