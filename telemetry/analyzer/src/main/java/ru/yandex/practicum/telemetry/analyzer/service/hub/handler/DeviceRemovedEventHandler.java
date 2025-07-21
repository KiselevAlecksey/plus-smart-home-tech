package ru.yandex.practicum.telemetry.analyzer.service.hub.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.db.action.Action;
import ru.yandex.practicum.telemetry.analyzer.db.action.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.db.condition.Condition;
import ru.yandex.practicum.telemetry.analyzer.db.condition.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.db.scenatio.Scenario;
import ru.yandex.practicum.telemetry.analyzer.db.scenatio.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.db.sensor.Sensor;
import ru.yandex.practicum.telemetry.analyzer.db.sensor.SensorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {
    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        DeviceRemovedEventAvro deviceRemoved = (DeviceRemovedEventAvro) event.getPayload();
        sensorRepository.findByIdAndHubId(deviceRemoved.getId(), event.getHubId())
                .ifPresent(this::deleteSensor);
    }

    private void deleteSensor(Sensor sensor) {
        List<Action> delActions = new ArrayList<>();
        List<Condition> delConditions = new ArrayList<>();
        List<Scenario> scenarios = scenarioRepository.findByHubId(sensor.getHubId()).stream()
                .peek(s -> s.setActions(this.getActions(s.getActions(), delActions, sensor.getId())))
                .peek(s -> s.setConditions(this.getConditions(s.getConditions(), delConditions, sensor.getId())))
                .toList();
        scenarioRepository.saveAll(scenarios);
        sensorRepository.delete(sensor);
        actionRepository.deleteAll(delActions);
        conditionRepository.deleteAll(delConditions);
    }

    private Map<String, Action> getActions(
            Map<String, Action> actions, List<Action> delActions, String sensorId) {
        return actions.entrySet().stream()
                .peek(e -> {
                    if (e.getKey().equalsIgnoreCase(sensorId)) {
                        delActions.add(e.getValue());
                    }
                })
                .filter(e -> !e.getKey().equalsIgnoreCase(sensorId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, Condition> getConditions(
            Map<String, Condition> conditions, List<Condition> delConditions, String sensorId) {
        return conditions.entrySet().stream()
                .peek(e -> {
                    if (e.getKey().equalsIgnoreCase(sensorId)) {
                        delConditions.add(e.getValue());
                    }
                })
                .filter(e -> !e.getKey().equalsIgnoreCase(sensorId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
