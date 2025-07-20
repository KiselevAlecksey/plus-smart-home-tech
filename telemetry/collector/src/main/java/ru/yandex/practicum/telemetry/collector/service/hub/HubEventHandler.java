package ru.yandex.practicum.telemetry.collector.service.hub;

import ru.yandex.practicum.telemetry.event.HubEventProto;

public interface HubEventHandler {
    HubEventProto.PayloadCase getMessageType();

    void handle(HubEventProto event);
}
