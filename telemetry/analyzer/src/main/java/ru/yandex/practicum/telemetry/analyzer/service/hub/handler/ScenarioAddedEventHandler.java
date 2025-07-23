package ru.yandex.practicum.telemetry.analyzer.service.hub.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.analyzer.db.action.Action;
import ru.yandex.practicum.telemetry.analyzer.db.condition.Condition;
import ru.yandex.practicum.telemetry.analyzer.db.scenatio.Scenario;
import ru.yandex.practicum.telemetry.analyzer.db.scenatio.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.db.sensor.Sensor;
import ru.yandex.practicum.telemetry.analyzer.db.sensor.SensorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        Scenario scenario = toScenario(event);
        Set<String> sensors = new HashSet<>();
        sensors.addAll(scenario.getActions().keySet());
        sensors.addAll(scenario.getConditions().keySet());

        List<Sensor> findSensors = sensorRepository.findByIdInAndHubId(sensors, event.getHubId());
        if (findSensors.size() != sensors.size()) {
            return;
        }

        scenarioRepository.findByHubIdAndName(scenario.getHubId(), scenario.getName())
                .ifPresent(s -> scenario.setId(s.getId()));

        scenarioRepository.save(scenario);
    }

    private Scenario toScenario(HubEventAvro event) {
        Map<String, Action> actions;
        Map<String, Condition> conditions;

        ScenarioAddedEventAvro scenarioAdded = (ScenarioAddedEventAvro) event.getPayload();

        actions = scenarioAdded.getActions().stream()
                .collect(Collectors.toMap(DeviceActionAvro::getSensorId, this::toAction));
        conditions = scenarioAdded.getConditions().stream()
                .collect(Collectors.toMap(ScenarioConditionAvro::getSensorId, this::toCondition));

        return Scenario.builder()
                .name(scenarioAdded.getName())
                .hubId(event.getHubId())
                .actions(actions)
                .conditions(conditions)
                .build();
    }

    private Action toAction(DeviceActionAvro deviceAction) {
        return Action.builder()
                .type(deviceAction.getType())
                .value(deviceAction.getValue())
                .build();
    }

    private Condition toCondition(ScenarioConditionAvro scenarioCondition) {
        Integer value = (scenarioCondition.getValue() instanceof Boolean)
                ? ((Boolean) scenarioCondition.getValue()).compareTo(false)
                : (Integer) scenarioCondition.getValue();
        return Condition.builder()
                .type(scenarioCondition.getType())
                .operation(scenarioCondition.getOperation())
                .value(value)
                .build();

    }
}
