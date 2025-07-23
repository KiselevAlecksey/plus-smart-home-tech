package ru.yandex.practicum.telemetry.analyzer.service.hub.handler;

import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

public enum HubEventType {
    DEVICE_ADDED(DeviceAddedEventAvro.getClassSchema().getName()),
    DEVICE_REMOVED(DeviceRemovedEventAvro.getClassSchema().getName()),
    SCENARIO_ADDED(ScenarioAddedEventAvro.getClassSchema().getName()),
    SCENARIO_REMOVED(ScenarioRemovedEventAvro.getClassSchema().getName());

    private final String name;

    HubEventType(String name) {
        this.name = name;
    }

    public static HubEventType fromName(String name) {
        for (HubEventType eventType : values()) {
            if (eventType.name.equalsIgnoreCase(name)) {
                return eventType;
            }
        }
        return null;
    }
}
