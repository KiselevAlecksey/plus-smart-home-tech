package ru.yandex.practicum.telemetry.collector.service;

import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.collector.cofiguration.KafkaConfig;
import ru.yandex.practicum.telemetry.collector.cofiguration.TopicType;

import java.util.EnumMap;

@Getter
@Component
public class KafkaEventProducer {
    private final Producer<String, SpecificRecordBase> producer;
    private final EnumMap<TopicType, String> topics;

    public KafkaEventProducer(KafkaConfig config) {
        producer = new KafkaProducer<>(config.getProperties());
        topics = config.getTopics();
    }
}
