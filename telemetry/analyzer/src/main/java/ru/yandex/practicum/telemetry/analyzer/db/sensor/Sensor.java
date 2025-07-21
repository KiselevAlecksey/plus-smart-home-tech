package ru.yandex.practicum.telemetry.analyzer.db.sensor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "sensors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Sensor {
    @Id
    private String id;

    @Column(name = "hub_id")
    private String hubId;
}
