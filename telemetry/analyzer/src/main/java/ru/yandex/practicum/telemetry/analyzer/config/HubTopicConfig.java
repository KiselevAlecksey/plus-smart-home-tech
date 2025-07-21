package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@ConfigurationProperties("analyzer.kafka.hub-consumer.topics")
public class HubTopicConfig extends BaseTopicsConfig {

    public HubTopicConfig() {
        super();
    }

    public HubTopicConfig(Map<String, String> topics) {
        super(topics);
    }
}
