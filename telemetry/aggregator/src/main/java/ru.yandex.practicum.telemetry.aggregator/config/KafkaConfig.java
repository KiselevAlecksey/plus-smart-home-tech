package ru.yandex.practicum.telemetry.aggregator.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties("aggregator.kafka")
public class KafkaConfig implements KafkaProducer, KafkaConsumer {
    private ProducerConfig producer;
    private ConsumerConfig consumer;

    @Getter
    @Setter
    public static class ProducerConfig {
        private Properties properties = new Properties();
        private Map<String, String> topics;

        public Properties getProperties() {
            return properties;
        }

        public Map<String, String> getTopics() {
            return topics;
        }
    }

    @Getter
    @Setter
    public static class ConsumerConfig {
        private Properties properties = new Properties();
        private Map<String, String> topics;

        public Properties getProperties() {
            return properties;
        }

        public Map<String, String> getTopics() {
            return topics;
        }
    }

    @Override
    public Properties getProducerProperties() {
        return producer.getProperties();
    }

    @Override
    public Map<String, String> getProducerTopics() {
        return producer.getTopics();
    }

    @Override
    public Properties getConsumerProperties() {
        return consumer.getProperties();
    }

    @Override
    public Map<String, String> getConsumerTopics() {
        return consumer.getTopics();
    }

}
