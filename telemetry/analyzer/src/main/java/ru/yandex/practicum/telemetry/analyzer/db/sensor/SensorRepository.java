package ru.yandex.practicum.telemetry.analyzer.db.sensor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    boolean existsByIdInAndHubId(Collection<String> ids, String hubId);

    Optional<Sensor> findByIdAndHubId(String id, String hubId);

    List<Sensor> findByIdInAndHubId(Collection<String> ids, String hubId);
}
