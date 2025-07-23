package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.aggregator.service.client.AggregatorClient;
import ru.yandex.practicum.telemetry.aggregator.config.KafkaConfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorStarter {
    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(100);
    private static final int AMOUNT_PART_COMMIT = 10;
    private static final String CONSUMER_NAME = "sensors2";
    private static final String PRODUCER_NAME = "sensors1";
    public static final String SENSORS_EVENTS = "sensors-events";
    public static final String SENSORS_SNAPSHOTS = "sensors-snapshots";

    private final AggregatorClient client;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(client.getConsumer(CONSUMER_NAME)::wakeup));
        try {
            consumerSubscribe();
            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = client.getConsumer(CONSUMER_NAME).poll(CONSUME_ATTEMPT_TIMEOUT);
                int count = 0;
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    log.info("{}", record);
                    SensorEventAvro event = (SensorEventAvro) record.value();
                    Optional<SensorsSnapshotAvro> snapshot = updateState(event);
                    snapshot.ifPresent(s -> {
                        snapshots.put(event.getHubId(), s);
                        sendProducerEvent(s);
                        log.info("{}", s);
                    });
                    manageOffsets(record, count, client.getConsumer(CONSUMER_NAME));
                }
                client.getConsumer(CONSUMER_NAME).commitAsync();
            }
        } catch (WakeupException ignored) {
        } finally {
            try {
                client.getConsumer(CONSUMER_NAME).commitSync(currentOffsets);
            } finally {
                log.info("Закрываем продюсер и консьюмер");
                client.closeConsumerAndProducer(CONSUMER_NAME, PRODUCER_NAME);
            }
        }
    }

    private void sendProducerEvent(SensorsSnapshotAvro event) {

        String topic = client.getProducerTopics(PRODUCER_NAME).get(SENSORS_SNAPSHOTS);
        client.getProducer(PRODUCER_NAME)
                .send(new ProducerRecord<>(topic, event));
    }

    private void consumerSubscribe() {
        client.getConsumer(CONSUMER_NAME)
                .subscribe(List.of(client.getConsumerTopics(CONSUMER_NAME).get(SENSORS_EVENTS)));
    }

    private void manageOffsets(
            ConsumerRecord<String, SpecificRecordBase> record,
            int count,
            Consumer<String, SpecificRecordBase> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % AMOUNT_PART_COMMIT == 0) {
            consumer.commitAsync(currentOffsets, (offsets, e) -> {
                if (e != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, e);
                }
            });
        }
    }

    private Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshot = snapshots.get(event.getHubId());
        if (snapshot == null) {
            snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(event.getHubId())
                    .setTimestamp(event.getTimestamp())
                    .setSensorsState(new HashMap<>())
                    .build();
        }

        SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
        if (oldState != null && (oldState.getTimestamp().isAfter(event.getTimestamp()) || oldState.getData().equals(event.getPayload()))) {
            return Optional.empty();
        }

        SensorStateAvro state = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
        snapshot.getSensorsState().put(event.getId(), state);
        snapshot.setTimestamp(event.getTimestamp());

        return Optional.of(snapshot);
    }
}
