package ru.yandex.practicum.telemetry.collector.cofiguration;

import java.util.Map;
import java.util.Properties;

public interface KafkaProducerConfig {
    Properties getProperties();
    Map<String, String> getTopics();
}
