package ru.yandex.practicum.telemetry.collector.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {
    protected final KafkaEventProducer producer;
    private static final String HUB_PRODUCER_NAME = "hubs";

    protected abstract T toAvro(HubEventProto event);

    @Override
    public abstract HubEventProto.PayloadCase getMessageType();

    @Override
    public void handle(HubEventProto event) {
        if (!event.getPayloadCase().equals(getMessageType())) {
            throw new IllegalArgumentException("Неизвестный тип события: " + event.getPayloadCase());
        }

        T payload = toAvro(event);

        Instant timestamp = event.hasTimestamp()
                ? Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos())
                : Instant.now();
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();

        log.warn("==> avro {}", eventAvro);

        sendHubEvent(eventAvro);
    }

    private void sendHubEvent(HubEventAvro event) {
        producer.sendEvent(HUB_PRODUCER_NAME, event);
    }
}
