package ru.yandex.practicum.telemetry.collector.cofiguration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@Getter
@ConfigurationProperties("collector.kafka.producer")
public class KafkaConfig {
    private final Properties properties;

    public KafkaConfig(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return (Properties) properties.clone();
    }
}


