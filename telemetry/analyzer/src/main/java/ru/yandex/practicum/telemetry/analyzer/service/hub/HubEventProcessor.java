package ru.yandex.practicum.telemetry.analyzer.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.analyzer.service.client.Client;
import ru.yandex.practicum.telemetry.analyzer.service.hub.handler.HubEventHandler;
import ru.yandex.practicum.telemetry.analyzer.service.hub.handler.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.yandex.practicum.telemetry.analyzer.service.OffsetManager.manageOffsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {
    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(100);
    private static final int AMOUNT_PART_COMMIT = 10;

    private final Client hubClient;
    private final Set<HubEventHandler> hubEventHandlers;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    @Override
    public void run() {
        Map<HubEventType, HubEventHandler> hubHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
        Runtime.getRuntime().addShutdownHook(new Thread(hubClient.getConsumer()::wakeup));
        try {
            consumerSubscribe();
            while (true) {
                try {
                    ConsumerRecords<String, SpecificRecordBase> records =
                            hubClient.getConsumer().poll(CONSUME_ATTEMPT_TIMEOUT);
                    int count = 0;
                    for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                        HubEventAvro event = (HubEventAvro) record.value();
                        log.info("{}", event);
                        HubEventHandler hubEventHandler =
                                hubHandlers.get(HubEventType.fromName(event.getPayload().getClass().getSimpleName()));
                        if (hubEventHandler == null) {
                            throw new IllegalArgumentException("Не найден обработчик: "
                                    + event.getPayload().getClass().getSimpleName());
                        }
                        hubEventHandler.handle(event);
                        manageOffsets(currentOffsets, AMOUNT_PART_COMMIT, record, count, hubClient.getConsumer());
                    }
                    hubClient.getConsumer().commitAsync();
                } catch (WakeupException e) {
                    throw new WakeupException();
                } catch (Exception e) {
                    log.error("Ошибка ", e);
                }
            }
        } catch (WakeupException ignored) {
        } finally {
            try {
                hubClient.getConsumer().commitSync(currentOffsets);
            } finally {
                log.info("Закрываем продюсер и HubConsumer");
                hubClient.stop();
            }
        }
    }

    private void consumerSubscribe() {
        hubClient.getConsumer().subscribe(hubClient.getAllTopics());
    }
}
