package ru.yandex.practicum.telemetry.collector.service;

import com.google.protobuf.Message;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.cofiguration.KafkaConfig;
import ru.yandex.practicum.telemetry.collector.cofiguration.TopicConfig;

import java.time.Duration;

@Getter
@Slf4j
@Component
public class KafkaEventProducer {
    private final Producer<String, Message> producer;
    private final TopicConfig topicConfig;
    @Value("${collector.kafka.producer.properties.close-time}")
    private int closeTime;

    public KafkaEventProducer(KafkaConfig config, TopicConfig topicConfig) {
        this.producer = new KafkaProducer<>(config.getProperties());
        this.topicConfig = new TopicConfig(topicConfig);
    }

    public void sendHubEvent(HubEventProto eventProto) {
        producer.send(new ProducerRecord<>(topicConfig.getDefaultHubTopic(), eventProto));
    }

    public void sendSensorEvent(SensorEventProto eventProto) {
        producer.send(new ProducerRecord<>(topicConfig.getDefaultSensorTopic(), eventProto));
    }

    //посмотреть как сделать консольную команду
    public void closeKafkaProducer() {
        try {
            producer.flush();
            producer.close(Duration.ofSeconds(closeTime));
        } catch (KafkaException exception) {
            log.error("Failed to close Kafka producer: {}", exception.getMessage(), exception);
        }
    }
}
