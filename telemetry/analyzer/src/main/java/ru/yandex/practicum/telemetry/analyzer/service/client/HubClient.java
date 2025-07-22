package ru.yandex.practicum.telemetry.analyzer.service.client;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("hubClient")
@RequiredArgsConstructor
public class HubClient implements Client {
    private final KafkaConfig config;
    private Consumer<String, SpecificRecordBase> consumer;
    private static final String CONSUMER_HUB_NAME = "hubs";

    @Override
    public Consumer<String, SpecificRecordBase> getConsumer() {
        if (consumer == null) {
            init();
        }
        return consumer;
    }

    @Override
    public Map<String, String> getTopics() {
        return config.getConsumers().get(CONSUMER_HUB_NAME).getTopics();
    }

    @Override
    public void stop() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Override
    public List<String> getAllTopics() {
        return new ArrayList<>(config.getConsumers().get(CONSUMER_HUB_NAME).getTopics().values());
    }

    private void init() {
        consumer = new KafkaConsumer<>(config.getConsumers().get(CONSUMER_HUB_NAME).getProperties());
    }
}
