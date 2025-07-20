package ru.yandex.practicum.telemetry.collector.service.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler {

    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED_EVENT_PROTO;
    }

    @Override
    protected HubEventProto processSpecificPayload(HubEventProto.Builder builder, HubEventProto event) {
        ScenarioAddedEventProto scenarioPayload = event.getScenarioAddedEventProto();

        /*List<ScenarioConditionProto> conditions = scenarioPayload.getConditionList().stream()
                .map(s -> {
                    ScenarioConditionProto.Builder conditionBuilder  = ScenarioConditionProto.newBuilder()
                            .setSensorId(s.getSensorId())
                            .setType(s.getType())
                            .setOperation(s.getOperation());

                    switch (s.getValueCase()) {
                        case BOOL_VALUE:
                            conditionBuilder .setBoolValue(s.getBoolValue());
                            break;
                        case INT_VALUE:
                            conditionBuilder .setIntValue(s.getIntValue());
                            break;
                        case VALUE_NOT_SET:
                            throw new IllegalArgumentException("Недопустимый тип переменной: "
                                    + s.getValueCase() + " ожидаемый тип: " + BOOL_VALUE + ", или " + INT_VALUE);
                    }
                    return conditionBuilder .build();
                })
                .toList();

        List<DeviceActionProto> actions = scenarioPayload.getActionList().stream()
                .map(a -> DeviceActionProto.newBuilder()
                        .setSensorId(a.getSensorId())
                        .setType(a.getType())
                        .setValue(a.getValue())
                        .build())
                .toList();*/

        ScenarioAddedEventProto updatedPayload = ScenarioAddedEventProto.newBuilder(scenarioPayload)
                .setId(scenarioPayload.getId())
                .addAllCondition(scenarioPayload.getConditionList())
                .addAllAction(scenarioPayload.getActionList())
                .build();

        return builder.setScenarioAddedEventProto(updatedPayload)
                .build();
    }
}

