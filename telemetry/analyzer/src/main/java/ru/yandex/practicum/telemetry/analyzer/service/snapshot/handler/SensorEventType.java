package ru.yandex.practicum.telemetry.analyzer.service.snapshot.handler;

import ru.yandex.practicum.kafka.telemetry.event.*;

public enum SensorEventType {
    CLIMATE_SENSOR(ClimateSensorAvro.getClassSchema().getName()),
    LIGHT_SENSOR(LightSensorAvro.getClassSchema().getName()),
    MOTION_SENSOR(MotionSensorAvro.getClassSchema().getName()),
    SWITCH_SENSOR(SwitchSensorAvro.getClassSchema().getName()),
    TEMPERATURE_SENSOR(TemperatureSensorAvro.getClassSchema().getName());

    private final String name;

    SensorEventType(String name) {
        this.name = name;
    }

    public static SensorEventType fromName(String name) {
        for (SensorEventType eventType : values()) {
            if (eventType.name.equalsIgnoreCase(name)) {
                return eventType;
            }
        }
        return null;
    }
}
