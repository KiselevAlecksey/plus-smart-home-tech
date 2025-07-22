package ru.yandex.practicum.telemetry.analyzer.config;

import java.util.Map;
import java.util.Properties;

public interface KafkaConsumerConfig {
    Properties getProperties();
    Map<String, String> getTopics();
}
