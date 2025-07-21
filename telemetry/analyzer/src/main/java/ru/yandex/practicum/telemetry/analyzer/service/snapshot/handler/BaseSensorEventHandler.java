package ru.yandex.practicum.telemetry.analyzer.service.snapshot.handler;

import com.google.protobuf.Timestamp;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.db.condition.Condition;
import ru.yandex.practicum.telemetry.analyzer.db.scenatio.Scenario;
import ru.yandex.practicum.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.telemetry.event.DeviceActionRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {
    @Override
    public abstract SensorEventType getMessageType();

    @Override
    public abstract boolean check(SensorStateAvro sensorState, Collection<Condition> conditions);

    @Override
    public Collection<DeviceActionRequest> getDeviceActionRequest(SensorEventHandlerParams params) {
        Collection<DeviceActionRequest> deviceActionRequests = new ArrayList<>();
        for (Scenario scenario : params.getScenarios()) {
            List<Condition> conditions = scenario.getConditions().entrySet().stream()
                    .filter(e -> e.getKey().equalsIgnoreCase(params.getSensorId()))
                    .map(Map.Entry::getValue)
                    .toList();

            if (!check(params.getSensorState(), conditions)) {
                continue;
            }

            deviceActionRequests.addAll(
                    scenario.getActions().entrySet().stream()
                            .map(a -> {
                                DeviceActionProto deviceActionProto = DeviceActionProto.newBuilder()
                                        .setSensorId(a.getKey())
                                        .setType(ActionTypeProto.valueOf(a.getValue().getType().name()))
                                        .setValue(a.getValue().getValue())
                                        .build();
                                return DeviceActionRequest.newBuilder()
                                        .setHubId(params.getHubId())
                                        .setScenarioName(scenario.getName())
                                        .setTimestamp(Timestamp.newBuilder()
                                                .setSeconds(params.getSensorState().getTimestamp().getEpochSecond())
                                                .setNanos(params.getSensorState().getTimestamp().getNano())
                                                .build())
                                        .setAction(deviceActionProto)
                                        .build();
                            })
                            .toList());

        }
        return deviceActionRequests;
    }
}
