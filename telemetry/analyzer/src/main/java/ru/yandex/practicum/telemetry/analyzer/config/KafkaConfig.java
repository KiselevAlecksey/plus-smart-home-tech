package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties("analyzer.kafka")
public class KafkaConfig {
    Map<String, ConsumerConfig> consumers;

    @Bean
    public Map<String, KafkaConsumerConfig> consumerConfigs() {
        return consumers.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new KafkaConsumerConfig() {
                            @Override
                            public Properties getProperties() {
                                return entry.getValue().getProperties();
                            }

                            @Override
                            public Map<String, String> getTopics() {
                                return entry.getValue().getTopics();
                            }
                        }
                ));
    }

    @Getter
    @Setter
    public static class ConsumerConfig {
        private Properties properties;
        private Map<String, String> topics;
    }
}
