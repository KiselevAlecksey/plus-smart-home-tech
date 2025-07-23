package ru.yandex.practicum.telemetry.analyzer.service.hub.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.analyzer.db.action.Action;
import ru.yandex.practicum.telemetry.analyzer.db.action.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.db.condition.Condition;
import ru.yandex.practicum.telemetry.analyzer.db.condition.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.db.scenatio.ScenarioRepository;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {
    private final ScenarioRepository scenarioRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        ScenarioRemovedEventAvro scenarioRemoved = (ScenarioRemovedEventAvro) event.getPayload();
        scenarioRepository.findByHubIdAndName(event.getHubId(), scenarioRemoved.getName())
                .ifPresent(s -> {
                    Collection<Action> delActions = s.getActions().values();
                    Collection<Condition> delConditions = s.getConditions().values();
                    scenarioRepository.delete(s);
                    actionRepository.deleteAll(delActions);
                    conditionRepository.deleteAll(delConditions);
                });
    }
}
