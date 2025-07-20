package ru.yandex.practicum.telemetry.collector.service.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler {
    public DeviceRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED_EVENT_PROTO;
    }

    @Override
    protected HubEventProto processSpecificPayload(HubEventProto.Builder builder, HubEventProto event) {
        DeviceRemovedEventProto devicePayload = event.getDeviceRemovedEventProto();

        return builder
                .setDeviceRemovedEventProto(
                        DeviceRemovedEventProto.newBuilder(devicePayload)
                                .setId(devicePayload.getId())
                                .build()
                )
                .build();
    }
}
