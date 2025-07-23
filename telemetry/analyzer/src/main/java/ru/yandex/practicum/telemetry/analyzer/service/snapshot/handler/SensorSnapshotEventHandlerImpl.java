package ru.yandex.practicum.telemetry.analyzer.service.snapshot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.db.scenatio.Scenario;
import ru.yandex.practicum.telemetry.analyzer.db.scenatio.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.service.snapshot.SendDeviceAction;
import ru.yandex.practicum.telemetry.event.DeviceActionRequest;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorSnapshotEventHandlerImpl implements SensorSnapshotEventHandler {
    private final ScenarioRepository scenarioRepository;
    private final SendDeviceAction sendDeviceAction;
    private final Set<SensorEventHandler> sensorEventHandlers;

    @Override
    public void handle(SensorsSnapshotAvro snapshot) {
        Map<SensorEventType, SensorEventHandler> sensorHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));

        List<Scenario> scenarios = scenarioRepository.findByHubId(snapshot.getHubId());
        for (Map.Entry<String, SensorStateAvro> entry : snapshot.getSensorsState().entrySet()) {
            SensorEventHandler sensorEventHandler =
                    sensorHandlers.get(SensorEventType.fromName(entry.getValue().getData().getClass().getSimpleName()));
            if (sensorEventHandler == null) {
                log.error("Не найден обработчик: {}", entry.getValue().getData().getClass().getSimpleName());
                continue;
            }
            SensorEventHandlerParams params = SensorEventHandlerParams.builder()
                    .hubId(snapshot.getHubId())
                    .sensorId(entry.getKey())
                    .sensorState(entry.getValue())
                    .scenarios(scenarios)
                    .build();
            Collection<DeviceActionRequest> deviceActionRequests = sensorEventHandler.getDeviceActionRequest(params);
            for (DeviceActionRequest deviceActionRequest : deviceActionRequests) {
                sendDeviceAction.send(deviceActionRequest);
            }
        }
    }
}
