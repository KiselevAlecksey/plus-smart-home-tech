package ru.yandex.practicum.telemetry.analyzer.service.snapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.service.client.Client;
import ru.yandex.practicum.telemetry.analyzer.service.snapshot.handler.SensorSnapshotEventHandler;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.telemetry.analyzer.service.OffsetManager.manageOffsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor {
    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(100);
    private static final int AMOUNT_PART_COMMIT = 10;

    private final Client snapshotClient;
    private final SensorSnapshotEventHandler sensorSnapshotEventHandler;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(snapshotClient.getConsumer()::wakeup));
        try {
            snapshotClient.getConsumer().subscribe(List.of(snapshotClient.getTopics().get("sensors-snapshots")));
            while (true) {
                try {
                    ConsumerRecords<String, SpecificRecordBase> records = snapshotClient.getConsumer().poll(CONSUME_ATTEMPT_TIMEOUT);
                    int count = 0;
                    for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                        SensorsSnapshotAvro snapshot = (SensorsSnapshotAvro) record.value();
                        log.info("{}", snapshot);
                        sensorSnapshotEventHandler.handle(snapshot);
                        manageOffsets(currentOffsets, AMOUNT_PART_COMMIT, record, count, snapshotClient.getConsumer());
                    }
                    snapshotClient.getConsumer().commitAsync();
                } catch (WakeupException e) {
                    throw new WakeupException();
                } catch (Exception e) {
                    log.error("Ошибка ", e);
                }
            }
        } catch (WakeupException ignored) {
        } finally {
            try {
                snapshotClient.getConsumer().commitSync(currentOffsets);
            } finally {
                log.info("Закрываем продюсер и HubConsumer");
                snapshotClient.stop();
            }
        }
    }
}
