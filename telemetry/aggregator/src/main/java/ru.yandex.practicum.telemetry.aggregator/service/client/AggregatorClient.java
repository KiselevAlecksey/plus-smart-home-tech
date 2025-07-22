package ru.yandex.practicum.telemetry.aggregator.service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.KafkaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.aggregator.config.KafkaConfig;

import java.time.Duration;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AggregatorClient implements Client {
    private final KafkaConfig config;
    private Producer<String, SpecificRecordBase> producer;
    private Consumer<String, SpecificRecordBase> consumer;
    @Value("${aggregator.kafka.producer.properties.close-time}")
    private int closeProducerTime;
    @Value("${aggregator.kafka.consumer.properties.close-time}")
    private int closeConsumerTime;
    private volatile String producerStatus = "RUNNING";
    private volatile String consumerStatus= "RUNNING";

    @Override
    public Producer<String, SpecificRecordBase> getProducer() {
        if (producer == null) {
            initProducer();
        }
        return producer;
    }

    @Override
    public Map<String, String> getProducerTopics() {
        return config.getProducer().getTopics();
    }

    @Override
    public Consumer<String, SpecificRecordBase> getConsumer() {
        if (consumer == null) {
            initConsumer();
        }
        return consumer;
    }

    @Override
    public Map<String, String> getConsumerTopics() {
        return config.getConsumer().getTopics();
    }

    @Override
    public void stop() {
        if (producer != null) {
            try {
                log.info("Initiating controlled Kafka producer shutdown");
                producer.flush();
                producer.close(Duration.ofSeconds(closeProducerTime));
                producerStatus = "SHUTDOWN_COMPLETE";
            } catch (KafkaException exception) {
                producerStatus = "SHUTDOWN_FAILED";
                log.error("Failed to close Kafka producer: {}", exception.getMessage(), exception);
            }
        }
        if (consumer != null) {
            try {
                log.info("Initiating controlled Kafka producer shutdown");
                consumer.unsubscribe();
                consumer.close(Duration.ofSeconds(closeConsumerTime));
                consumerStatus = "SHUTDOWN_COMPLETE";
            } catch (KafkaException exception) {
                consumerStatus = "SHUTDOWN_FAILED";
                log.error("Failed to close Kafka producer: {}", exception.getMessage(), exception);
            }
        }
    }

    private void initProducer() {
        producer = new KafkaProducer<>(config.getProducer().getProperties());
    }

    private void initConsumer() {
        consumer = new KafkaConsumer<>(config.getConsumer().getProperties());
    }
}
