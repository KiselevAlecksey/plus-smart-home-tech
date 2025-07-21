package ru.yandex.practicum.telemetry.aggregator.config;

import java.util.Map;
import java.util.Properties;

public interface KafkaProducer {
    Properties getProducerProperties();

    Map<String, String> getProducerTopics();
}
