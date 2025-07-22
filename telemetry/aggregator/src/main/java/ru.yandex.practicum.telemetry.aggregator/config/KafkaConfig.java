package ru.yandex.practicum.telemetry.aggregator.config;

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
@ConfigurationProperties("aggregator.kafka")
public class KafkaConfig {
    private ProducerConfig producer;
    private ConsumerConfig consumer;

    @Bean
    public KafkaProducer kafkaProducer() {
        return new KafkaProducer() {
            @Override
            public Properties getProducerProperties() {
                return producer.getProperties();
            }

            @Override
            public Map<String, String> getProducerTopics() {
                return producer.getTopics();
            }
        };
    }

    @Bean
    public KafkaConsumer kafkaConsumer() {
        return new KafkaConsumer() {
            @Override
            public Properties getConsumerProperties() {
                return consumer.getProperties();
            }

            @Override
            public Map<String, String> getConsumerTopics() {
                return consumer.getTopics();
            }

        };
    }

    @Getter
    @Setter
    public static class ProducerConfig {
        private Properties properties;
        private Map<String, String> topics;
    }

    @Getter
    @Setter
    public static class ConsumerConfig {
        private Properties properties;
        private Map<String, String> topics;
    }
}
