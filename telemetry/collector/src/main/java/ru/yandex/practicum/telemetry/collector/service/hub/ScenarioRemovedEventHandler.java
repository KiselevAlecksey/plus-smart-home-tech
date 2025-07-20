package ru.yandex.practicum.telemetry.collector.service.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler {
    public ScenarioRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected HubEventProto processSpecificPayload(HubEventProto.Builder builder, HubEventProto event) {
        ScenarioRemovedEventProto scenarioPayload = event.getScenarioRemovedEventProto();

        return builder
                .setScenarioRemovedEventProto(
                        ScenarioRemovedEventProto.newBuilder(scenarioPayload)
                                .setId(scenarioPayload.getId())
                                .build()
                )
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED_EVENT_PROTO;
    }
}
