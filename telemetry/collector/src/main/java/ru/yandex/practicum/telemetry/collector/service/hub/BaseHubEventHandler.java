package ru.yandex.practicum.telemetry.collector.service.hub;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler implements HubEventHandler {
    protected final KafkaEventProducer producer;

    @Override
    public abstract HubEventProto.PayloadCase getMessageType();

    @Override
    public void handle(HubEventProto event) {
        HubEventProto.PayloadCase messageType = event.getPayloadCase();

        if (messageType != getMessageType()) {
            throw new IllegalArgumentException("Неизвестный тип события: "
                    + event.getHubId() + " ожидаемый тип: " + getMessageType());
        }

        HubEventProto updatedEvent = updateEventWithNewTimestamp(event);

        log.info("Отправка события в Kafka: {}", updatedEvent);
        producer.sendHubEvent(updatedEvent);
    }

    protected HubEventProto updateEventWithNewTimestamp(HubEventProto event) {
        Timestamp timestamp = event.hasTimestamp()
                ? event.getTimestamp()
                : Timestamp.newBuilder()
                        .setSeconds(Instant.now().getEpochSecond())
                        .setNanos(Instant.now().getNano())
                        .build();

        HubEventProto.Builder builder = HubEventProto.newBuilder(event)
                .setHubId(event.getHubId())
                .setTimestamp(timestamp);

        return processSpecificPayload(builder, event);
    }

    protected abstract HubEventProto processSpecificPayload(
            HubEventProto.Builder builder, HubEventProto event);
}
