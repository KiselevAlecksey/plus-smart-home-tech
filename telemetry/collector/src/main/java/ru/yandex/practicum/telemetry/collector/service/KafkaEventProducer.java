package ru.yandex.practicum.telemetry.collector.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.cofiguration.KafkaConfig;
import ru.yandex.practicum.telemetry.collector.cofiguration.TopicConfig;

import java.time.Duration;

@Getter
@Slf4j
@Component
public class KafkaEventProducer {
    private final Producer<String, SpecificRecordBase> producer;
    private final TopicConfig topicConfig;
    private final int closeTime;

    public KafkaEventProducer(KafkaConfig config, TopicConfig topicConfig) {
        this.producer = new KafkaProducer<>(config.getProperties());
        this.topicConfig = new TopicConfig(topicConfig);
        this.closeTime = 15;
    }

    public void sendHubEvent(HubEventAvro eventAvro) {
        producer.send(new ProducerRecord<>(topicConfig.getDefaultHubTopic(), eventAvro));
    }

    public void sendSensorEvent(SensorEventAvro eventAvro) {
        producer.send(new ProducerRecord<>(topicConfig.getDefaultSensorTopic(), eventAvro));
    }

    public void closeKafkaProducer() {
        try {
            producer.flush();
            producer.close(Duration.ofSeconds(15));
        } catch (KafkaException exception) {
            log.error("Failed to close Kafka producer: {}", exception.getMessage(), exception);
        }
    }
}
