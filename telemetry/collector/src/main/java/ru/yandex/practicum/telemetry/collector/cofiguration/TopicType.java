package ru.yandex.practicum.telemetry.collector.cofiguration;

public enum TopicType {
    SENSORS_EVENTS,
    HUBS_EVENTS;

    public static TopicType toTopicsType(String type) {
        for (TopicType value : values()) {
            if (value.name().equalsIgnoreCase(type.replace("-", "_"))) {
                return value;
            }
        }
        return null;
    }
}
