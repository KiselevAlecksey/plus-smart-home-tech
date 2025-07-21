package ru.yandex.practicum.telemetry.analyzer.service.hub.handler;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {
    @Override
    public abstract HubEventType getMessageType();

    @Override
    public abstract void handle(HubEventAvro event);
}
