package ru.yandex.practicum.commerce.delivery.delivery;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "delivery_addresses", schema = "shopping_store")
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

    @Column(name = "flat", nullable = false)
    String flat;

    @OneToMany(mappedBy = "fromAddress", fetch = FetchType.LAZY)
    List<Delivery> fromDeliveries;

    @OneToMany(mappedBy = "toAddress", fetch = FetchType.LAZY)
    List<Delivery> toDeliveries;
}
