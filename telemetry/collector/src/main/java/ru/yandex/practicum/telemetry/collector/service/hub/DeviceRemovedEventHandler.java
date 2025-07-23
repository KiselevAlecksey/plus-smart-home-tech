package ru.yandex.practicum.telemetry.collector.service.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {
    public DeviceRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    protected DeviceRemovedEventAvro toAvro(HubEventProto event) {
        DeviceRemovedEventProto deviceRemovedEventProto = event.getDeviceRemoved();

        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEventProto.getId())
                .build();
    }
}
