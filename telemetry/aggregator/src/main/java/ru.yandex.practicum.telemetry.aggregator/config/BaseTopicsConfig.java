package ru.yandex.practicum.telemetry.aggregator.config;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class BaseTopicsConfig {
    protected final Map<String, String> topics;

    protected BaseTopicsConfig() {
        this.topics = new HashMap<>();
    }

    protected BaseTopicsConfig(BaseTopicsConfig config) {
        this.topics = Map.copyOf(config.getTopics());
    }

    protected BaseTopicsConfig(Map<String, String> topics) {
        this.topics = Map.copyOf(topics);
    }

    public String resolveTopic(String topicKey) {
        return topics.getOrDefault(topicKey, null);
    }
}
