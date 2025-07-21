package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties("analyzer.kafka")
public class KafkaConfig {
    private HubConfig hub;
    private SnapshotConfig snapshot;

    @Bean
    public HubEventConsumer hubEventConsumer() {
        return new HubEventConsumer() {
            @Override
            public Properties getHubProperties() {
                return hub.getProperties();
            }

            @Override
            public Map<String, String> getHubTopics() {
                return hub.getTopics();
            }
        };
    }

    @Bean
    public SnapshotsConsumer snapshotsConsumer() {
        return new SnapshotsConsumer() {
            @Override
            public Properties getSnapshotProperties() {
                return snapshot.getProperties();
            }

            @Override
            public Map<String, String> getSnapshotTopics() {
                return snapshot.getTopics();
            }
        };
    }

    @Getter
    @Setter
    public static class HubConfig {
        private Properties properties;
        private Map<String, String> topics;
    }

    @Getter
    @Setter
    public static class SnapshotConfig {
        private Properties properties;
        private Map<String, String> topics;
    }
}
