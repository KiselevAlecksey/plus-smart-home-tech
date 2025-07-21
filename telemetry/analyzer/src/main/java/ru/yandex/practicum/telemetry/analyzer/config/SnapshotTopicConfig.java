package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@ConfigurationProperties("analyzer.kafka.snapshot-consumer.topics")
public class SnapshotTopicConfig extends BaseTopicsConfig {

    public SnapshotTopicConfig() {
        super();
    }

    public SnapshotTopicConfig(Map<String, String> topics) {
        super(topics);
    }
}
