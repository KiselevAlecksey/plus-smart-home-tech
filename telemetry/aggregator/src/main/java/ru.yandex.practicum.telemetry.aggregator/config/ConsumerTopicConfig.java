package ru.yandex.practicum.telemetry.aggregator.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@ConfigurationProperties("aggregator.kafka.consumer.topics")
public class ConsumerTopicConfig extends BaseTopicsConfig {

    public ConsumerTopicConfig() {
        super();
    }

    public ConsumerTopicConfig(Map<String, String> topics) {
        super(topics);
    }
}
