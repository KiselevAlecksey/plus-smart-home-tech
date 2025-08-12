package ru.yandex.practicum.commerce.delivery.delivery;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "addresses", schema = "shopping_store")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    UUID id;

    @Column(name = "country", nullable = false)
    String country;

    @Column(name = "city", nullable = false)
    String city;

    @Column(name = "street", nullable = false)
    String street;

    @Column(name = "house", nullable = false)
    String house;

    @Column(name = "flat")
    String flat;

    @Column(name = "is_warehouse")
    Boolean isWarehouse;

    @OneToMany(mappedBy = "fromAddress")
    List<Delivery> fromDeliveries;

    @OneToMany(mappedBy = "toAddress")
    List<Delivery> toDeliveries;
}
