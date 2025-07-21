package ru.yandex.practicum.telemetry.analyzer.service.snapshot.handler;

import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.db.condition.Condition;
import ru.yandex.practicum.telemetry.event.DeviceActionRequest;

import java.util.Collection;

public interface SensorEventHandler {
    SensorEventType getMessageType();

    boolean check(SensorStateAvro sensorState, Collection<Condition> conditions);

    Collection<DeviceActionRequest> getDeviceActionRequest(SensorEventHandlerParams params);
}
