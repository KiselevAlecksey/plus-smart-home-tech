package ru.yandex.practicum.telemetry.collector.service.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

import java.util.List;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {
    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    protected ScenarioAddedEventAvro toAvro(HubEventProto event) {
        ScenarioAddedEventProto scenarioAddedEventProto = event.getScenarioAdded();

        List<ScenarioConditionAvro> conditions = scenarioAddedEventProto.getConditionList().stream()
                .map(s -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(s.getSensorId())
                        .setType(ConditionTypeAvro.valueOf(s.getType().name()))
                        .setOperation(ConditionOperationAvro.valueOf(s.getOperation().name()))
                        .setValue(switch (s.getValueCase()) {
                            case BOOL_VALUE -> s.getBoolValue();
                            case INT_VALUE -> s.getIntValue();
                            default -> null;
                        })
                        .build())
                .toList();
        List<DeviceActionAvro> actions = scenarioAddedEventProto.getActionList().stream()
                .map(a -> DeviceActionAvro.newBuilder()
                        .setSensorId(a.getSensorId())
                        .setType(ActionTypeAvro.valueOf(a.getType().name()))
                        .setValue(a.getValue())
                        .build())
                .toList();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEventProto.getName())
                .setConditions(conditions)
                .setActions(actions)
                .build();
    }
}

