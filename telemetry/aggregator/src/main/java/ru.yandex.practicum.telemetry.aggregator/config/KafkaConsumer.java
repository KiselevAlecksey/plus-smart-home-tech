package ru.yandex.practicum.telemetry.aggregator.config;

import java.util.Map;
import java.util.Properties;

public interface KafkaConsumer {
    Properties getConsumerProperties();

    Map<String, String> getConsumerTopics();
}
