package ru.yandex.practicum.telemetry.analyzer.service.snapshot.handler;

import lombok.Builder;
import lombok.Getter;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.db.scenatio.Scenario;

import java.util.Collection;

@Getter
@Builder(toBuilder = true)
public class SensorEventHandlerParams {
    private String hubId;
    private String sensorId;
    private SensorStateAvro sensorState;
    private Collection<Scenario> scenarios;
}