package ru.yandex.practicum.telemetry.aggregator.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties("aggregator.kafka")
public class KafkaConfig {
    private Map<String, ProducerConfig> producers;
    private Map<String, ConsumerConfig> consumers;

    @Getter
    @Setter
    public static class ProducerConfig {
        private Map<String, String> properties;
        private List<TopicConfig> topics;
    }

    @Getter
    @Setter
    public static class ConsumerConfig {
        private Map<String, String> properties;
        private List<TopicConfig> topics;
    }
}
