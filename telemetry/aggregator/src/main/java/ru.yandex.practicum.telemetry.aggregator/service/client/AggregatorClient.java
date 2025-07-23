package ru.yandex.practicum.telemetry.aggregator.service.client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.aggregator.config.KafkaConfig;
import ru.yandex.practicum.telemetry.aggregator.manager.KafkaConsumerManagerImpl;
import ru.yandex.practicum.telemetry.aggregator.manager.KafkaProducerManagerImpl;
import ru.yandex.practicum.telemetry.aggregator.service.util.KafkaConsumerFactory;
import ru.yandex.practicum.telemetry.aggregator.service.util.KafkaProducerFactory;
import ru.yandex.practicum.telemetry.aggregator.config.TopicConfig;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
@Component
@Slf4j
@ManagedResource(objectName = "ru.yandex.practicum.telemetry.aggregator:type=Kafka,name=KafkaEventConsumerManager",
        description = "Kafka Event Consumers Management")
public class AggregatorClient implements Client {
    private final Map<String, Map<String, String>> topicsMapping;
    private final KafkaProducerManagerImpl producerManager;
    private final KafkaConsumerManagerImpl consumerManager;

    public AggregatorClient(KafkaConfig config, KafkaProducerFactory producerFactory, KafkaConsumerFactory consumerFactory) {
        this.topicsMapping = new ConcurrentHashMap<>();

        config.getProducers().forEach((producerName, producerConfig) -> {
            Map<String, String> topics = producerConfig.getTopics().stream()
                    .collect(Collectors.toMap(
                            TopicConfig::getName,
                            TopicConfig::getValue));
            topicsMapping.put(producerName, topics);
        });

        config.getConsumers().forEach((consumerName, consumerConfig) -> {
            Map<String, String> topics = consumerConfig.getTopics().stream()
                    .collect(Collectors.toMap(
                            TopicConfig::getName,
                            TopicConfig::getValue));
            topicsMapping.put(consumerName, topics);
        });

        this.producerManager = new KafkaProducerManagerImpl(config, producerFactory);
        this.consumerManager = new KafkaConsumerManagerImpl(config, consumerFactory);
    }

    @Override
    public Producer<String, SpecificRecordBase> getProducer(String producerName) {
        return producerManager.getActiveProducer(producerName);
    }

    @Override
    public Map<String, String> getProducerTopics(String producerName) {
        Map<String, String> topicMap = topicsMapping.get(producerName);

        return topicsMapping.get(producerName);
    }

    @Override
    public Consumer<String, SpecificRecordBase> getConsumer(String consumerName) {
        return consumerManager.getActiveConsumer(consumerName);
    }

    @Override
    public Map<String, String> getConsumerTopics(String consumerName) {
        return topicsMapping.get(consumerName);
    }

    @Override
    public void closeConsumerAndProducer(String consumerName, String producerName) {
        consumerManager.closeConsumer(consumerName);
        producerManager.closeProducer(producerName);
    }
}
