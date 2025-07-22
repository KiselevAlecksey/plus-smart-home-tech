package ru.yandex.practicum.telemetry.collector.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.cofiguration.KafkaConfig;
import ru.yandex.practicum.telemetry.collector.service.util.KafkaProducerFactory;
import ru.yandex.practicum.telemetry.collector.cofiguration.TopicConfig;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
@Slf4j
@Component
@ManagedResource(objectName = "ru.yandex.practicum.telemetry.collector:type=Kafka,name=KafkaEventProducer",
        description = "Kafka Event Producer Management")
public class KafkaEventProducer {
    private final Map<String, Producer<String, SpecificRecordBase>> producers;
    private final Map<String, Map<String, String>> topicsMapping;

    @Value("${collector.kafka.producer.properties.close-time}")
    private int closeTime;
    private static final String HUB_PRODUCER_NAME = "hubs";
    private static final String SENSOR_PRODUCER_NAME = "sensors";

    private volatile String status = "RUNNING";

    public KafkaEventProducer(KafkaConfig config, KafkaProducerFactory factory) {
        this.producers = new ConcurrentHashMap<>();
        this.topicsMapping = new ConcurrentHashMap<>();

        config.getProducers().forEach((producerName, producerConfig) -> {
            Producer<String, SpecificRecordBase> producer = factory.createProducer(producerName, producerConfig.getProperties());
            producers.put(producerName, producer);

            Map<String, String> topics = producerConfig.getTopics().stream()
                    .collect(Collectors.toMap(
                            TopicConfig::getName,
                            TopicConfig::getValue));
            topicsMapping.put(producerName, topics);
        });
    }

    public void sendEvent(String producerName, String topicName, SpecificRecordBase event) {
        Producer<String, SpecificRecordBase> producer = producers.get(producerName);
        if (producer == null) {
            throw new IllegalArgumentException("Unknown producer: " + producerName);
        }

        String topic = topicsMapping.get(producerName).get(topicName);
        if (topic == null) {
            throw new IllegalArgumentException("Unknown topic '" + topicName + "' for producer: " + producerName);
        }

        try {
            producer.send(new ProducerRecord<>(topic, event), (metadata, exception) -> {
                if (exception != null) {
                    log.error("Failed to send event to {}:{} - {}", producerName, topicName, exception.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Error sending event to {}:{}", producerName, topicName, e);
            throw e;
        }
    }

    public void sendEvent(String producerName, SpecificRecordBase event) {
        Map<String, String> topics = topicsMapping.get(producerName);
        if (topics == null || topics.isEmpty()) {
            throw new IllegalArgumentException("No topics configured for producer: " + producerName);
        }

        String defaultTopic = topics.keySet().iterator().next();
        sendEvent(producerName, defaultTopic, event);
    }

    public void sendHubEvent(HubEventAvro event) {
        sendEvent(HUB_PRODUCER_NAME, event);
    }

    public void sendSensorEvent(SensorEventAvro event) {
        sendEvent(SENSOR_PRODUCER_NAME, event);
    }

    @ManagedOperation(description = "Gracefully shutdown Kafka producers")
    public void closeKafkaProducers() {
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger totalCount = new AtomicInteger();

        producers.forEach((name, producer) -> {
            totalCount.incrementAndGet();
            if (closeProducer(name, producer)) {
                successCount.incrementAndGet();
            }
        });

        if (successCount.get() == totalCount.get()) {
            status = "SHUTDOWN_COMPLETE";
        } else if (successCount.get() > 0) {
            status = "SHUTDOWN_PARTIAL";
        } else {
            status = "SHUTDOWN_FAILED";
        }
    }

    private boolean closeProducer(String name, Producer<String, SpecificRecordBase> producer) {
        try {
            log.info("Closing {} producer", name);
            producer.flush();
            producer.close(Duration.ofSeconds(closeTime));
            return true;
        } catch (Exception e) {
            log.error("Error closing {} producer", name, e);
            return false;
        }
    }

    @ManagedAttribute(description = "List of available producers")
    public Set<String> getAvailableProducers() {
        return Collections.unmodifiableSet(producers.keySet());
    }

    @ManagedAttribute(description = "List of available topics for producer")
    public Map<String, String> getTopicsForProducer(String producerName) {
        return Collections.unmodifiableMap(topicsMapping.getOrDefault(producerName, Map.of()));
    }

    @ManagedAttribute(description = "Current producer status")
    public String getStatus() {
        return status;
    }

    @ManagedAttribute(description = "Close timeout in seconds")
    public int getCloseTimeout() {
        return closeTime;
    }

    @ManagedAttribute
    public void setCloseTimeout(int seconds) {
        this.closeTime = seconds;
        log.info("Updated close timeout to {} seconds", seconds);
    }
}
