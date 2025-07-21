package ru.yandex.practicum.telemetry.aggregator.service.client;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.aggregator.config.KafkaConfig;
import ru.yandex.practicum.telemetry.aggregator.service.KafkaClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AggregatorClient implements Client {
    private final KafkaConfig config;
    private Producer<String, SpecificRecordBase> producer;
    private Consumer<String, SpecificRecordBase> consumer;

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
            producer.close();
        }
        if (consumer != null) {
            consumer.close();
        }
    }

    private void initProducer() {
        producer = new KafkaProducer<>(config.getProducer().getProperties());
    }

    private void initConsumer() {
        consumer = new KafkaConsumer<>(config.getConsumer().getProperties());
    }
}
