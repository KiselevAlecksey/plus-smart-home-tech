package ru.yandex.practicum.telemetry.collector.service.sensor;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseSensorEventHandler implements SensorEventHandler {
    protected final KafkaEventProducer producer;

    @Override
    public abstract SensorEventProto.PayloadCase getMessageType();

    @Override
    public void handle(SensorEventProto event) {
        SensorEventProto.PayloadCase messageType = event.getPayloadCase();

        if (messageType != getMessageType()) {
            throw new IllegalArgumentException("Неизвестный тип события: "
                    + event.getHubId() + " ожидаемый тип: " + getMessageType());
        }

        SensorEventProto updatedEvent = updateEventWithNewTimestamp(event);

        log.info("Отправка события в Kafka: {}", updatedEvent);
        producer.sendSensorEvent(updatedEvent);
    }

    protected SensorEventProto updateEventWithNewTimestamp(SensorEventProto event) {
        Timestamp timestamp = event.hasTimestamp()
                ? event.getTimestamp()
                : Timestamp.newBuilder()
                .setSeconds(Instant.now().getEpochSecond())
                .setNanos(Instant.now().getNano())
                .build();

        SensorEventProto.Builder builder = SensorEventProto.newBuilder(event)
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(timestamp);


        return processSpecificPayload(builder, event);
    }

    protected abstract SensorEventProto processSpecificPayload(
            SensorEventProto.Builder builder, SensorEventProto event);
}
