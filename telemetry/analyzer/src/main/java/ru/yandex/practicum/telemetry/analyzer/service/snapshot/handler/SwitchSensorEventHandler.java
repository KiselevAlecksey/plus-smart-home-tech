package ru.yandex.practicum.telemetry.analyzer.service.snapshot.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.analyzer.db.condition.Condition;

import java.util.Collection;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {
    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR;
    }

    @Override
    public boolean check(SensorStateAvro sensorState, Collection<Condition> conditions) {
        boolean result = true;
        SwitchSensorAvro switchSensor = (SwitchSensorAvro) sensorState.getData();
        for (Condition condition : conditions) {
            result = result && switch (condition.getOperation()) {
                case EQUALS -> switch (condition.getType()) {
                    case SWITCH -> {
                        boolean stateValue = condition.getValue() != 0;
                        yield switchSensor.getState() == stateValue;
                    }
                    default -> false;
                };
                default -> false;
            };
        }
        return result;
    }
}
