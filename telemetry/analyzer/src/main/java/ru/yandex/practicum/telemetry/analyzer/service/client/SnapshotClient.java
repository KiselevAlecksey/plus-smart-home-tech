package ru.yandex.practicum.telemetry.analyzer.service.client;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;

import java.util.Map;

@Component("snapshotClient")
@RequiredArgsConstructor
public class SnapshotClient implements Client {
    private final KafkaConfig config;
    private Consumer<String, SpecificRecordBase> consumer;

    @Override
    public Consumer<String, SpecificRecordBase> getConsumer() {
        if (consumer == null) {
            init();
        }
        return consumer;
    }

    @Override
    public Map<String, String> getTopics() {
        return config.getSnapshot().getTopics();
    }

    @Override
    public void stop() {
        if (consumer != null) {
            consumer.close();
        }
    }

    private void init() {
        consumer = new KafkaConsumer<>(config.getSnapshot().getProperties());
    }
}
