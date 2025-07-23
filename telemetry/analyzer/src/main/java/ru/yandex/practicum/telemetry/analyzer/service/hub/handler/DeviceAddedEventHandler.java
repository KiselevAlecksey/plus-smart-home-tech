package ru.yandex.practicum.telemetry.analyzer.service.hub.handler;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.db.sensor.Sensor;
import ru.yandex.practicum.telemetry.analyzer.db.sensor.SensorRepository;

@Component
@RequiredArgsConstructor
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {
    private final SensorRepository sensorRepository;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Transactional
    @Override
    public void handle(HubEventAvro event) {
        Sensor sensor = toSensor(event);
        sensorRepository.findByIdAndHubId(sensor.getId(), sensor.getHubId())
                .ifPresent(s -> sensor.setId(s.getId()));
        sensorRepository.save(sensor);
    }

    private Sensor toSensor(HubEventAvro event) {
        DeviceAddedEventAvro deviceAdded = (DeviceAddedEventAvro) event.getPayload();
        return Sensor.builder()
                .id(deviceAdded.getId())
                .hubId(event.getHubId())
                .build();
    }
}
