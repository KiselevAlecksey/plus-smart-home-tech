package ru.yandex.practicum.telemetry.collector.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.cofiguration.KafkaConfig;
import ru.yandex.practicum.telemetry.collector.cofiguration.TopicConfig;

import java.time.Duration;

@Getter
@Slf4j
@Component
@ManagedResource(objectName = "ru.yandex.practicum.telemetry.collector:type=Kafka,name=KafkaEventProducer",
        description = "Kafka Event Producer Management")
public class KafkaEventProducer {
    private final Producer<String, SpecificRecordBase> producer;
    private final TopicConfig topicConfig;
    @Value("${collector.kafka.producer.properties.close-time}")
    private int closeTime;
    private volatile String status = "RUNNING";

    public KafkaEventProducer(KafkaConfig config, TopicConfig topicConfig) {
        this.producer = new KafkaProducer<>(config.getProperties());
        this.topicConfig = new TopicConfig(topicConfig);
    }

    public void sendHubEvent(HubEventAvro event) {
        producer.send(new ProducerRecord<>(topicConfig.getDefaultHubTopic(), event));
    }

    public void sendSensorEvent(SensorEventAvro event) {
        producer.send(new ProducerRecord<>(topicConfig.getDefaultSensorTopic(), event));
    }

    @ManagedOperation(description = "Gracefully shutdown Kafka producer")
    public void closeKafkaProducer() {
        if (producer == null) {
            status = "SHUTDOWN_FAILED";
            log.warn("Kafka producer is already null");
            return;
        }
        try {
            log.info("Initiating controlled Kafka producer shutdown");
            producer.flush();
            producer.close(Duration.ofSeconds(closeTime));
            status = "SHUTDOWN_COMPLETE";
        } catch (KafkaException exception) {
            status = "SHUTDOWN_FAILED";
            log.error("Failed to close Kafka producer: {}", exception.getMessage(), exception);
        }
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
