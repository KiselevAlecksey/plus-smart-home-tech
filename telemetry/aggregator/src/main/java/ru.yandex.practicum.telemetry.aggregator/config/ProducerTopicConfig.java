package ru.yandex.practicum.telemetry.aggregator.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@ConfigurationProperties("aggregator.kafka.producer.topics")
public class ProducerTopicConfig extends BaseTopicsConfig {

    public ProducerTopicConfig() {
        super();
    }

    public ProducerTopicConfig(Map<String, String> topics) {
        super(topics);
    }
}
