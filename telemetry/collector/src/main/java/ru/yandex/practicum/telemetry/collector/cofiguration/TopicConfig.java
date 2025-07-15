package ru.yandex.practicum.telemetry.collector.cofiguration;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigurationProperties("collector.kafka.producer.topics")
public class TopicConfig {
    private final Map<String, String> sensors;
    private final Map<String, String> hubs;
    @Value("${collector.kafka.producer.topics.sensors.default}")
    private String defaultSensorTopic;
    @Value("${collector.kafka.producer.topics.hubs.default}")
    private String defaultHubTopic;

    public TopicConfig() {
        this.sensors = new HashMap<>();
        this.hubs = new HashMap<>();
    }

    public TopicConfig(TopicConfig config) {
        this.sensors = Map.copyOf(config.getSensorsTopics());
        this.hubs = Map.copyOf(config.getHubsTopics());
        this.defaultHubTopic = config.defaultHubTopic;
        this.defaultSensorTopic = config.defaultSensorTopic;
    }

    public TopicConfig(Map<String, String> sensorTopics, Map<String, String> hubTopics) {
        this.sensors = Map.copyOf(sensorTopics);
        this.hubs = Map.copyOf(hubTopics);
    }

    public Map<String, String> getAllTopics() {
        return new HashMap<>() {{
            putAll(sensors);
            putAll(hubs);
        }};
    }

    public Map<String, String> getHubsTopics() {
        return new HashMap<>(hubs);
    }

    public Map<String, String> getSensorsTopics() {
        return new HashMap<>(sensors);
    }
}
