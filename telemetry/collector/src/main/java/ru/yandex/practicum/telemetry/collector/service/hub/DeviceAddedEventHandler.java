package ru.yandex.practicum.telemetry.collector.service.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class DeviceAddedEventHandler extends BaseHubEventHandler {
    public DeviceAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    protected HubEventProto processSpecificPayload(HubEventProto.Builder builder, HubEventProto event) {
        DeviceAddedEventProto devicePayload = event.getDeviceAdded();

        return builder
                .setDeviceAdded(
                        DeviceAddedEventProto.newBuilder(devicePayload)
                                .setId(devicePayload.getId())
                                .setType(devicePayload.getType())
                                .build()
                )
                .build();
    }
}
