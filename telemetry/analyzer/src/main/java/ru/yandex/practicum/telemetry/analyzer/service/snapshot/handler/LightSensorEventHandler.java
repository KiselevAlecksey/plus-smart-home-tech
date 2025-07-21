package ru.yandex.practicum.telemetry.analyzer.service.snapshot.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.db.condition.Condition;

import java.util.Collection;

@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {
    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR;
    }

    @Override
    public boolean check(SensorStateAvro sensorState, Collection<Condition> conditions) {
        boolean result = true;
        LightSensorAvro lightSensor = (LightSensorAvro) sensorState.getData();
        for (Condition condition : conditions) {
            result = result && switch (condition.getOperation()) {
                case EQUALS -> switch (condition.getType()) {
                    case LUMINOSITY -> lightSensor.getLuminosity() == condition.getValue();
                    default -> false;
                };
                case GREATER_THAN -> switch (condition.getType()) {
                    case LUMINOSITY -> lightSensor.getLuminosity() > condition.getValue();
                    default -> false;
                };
                case LOWER_THAN -> switch (condition.getType()) {
                    case LUMINOSITY -> lightSensor.getLuminosity() < condition.getValue();
                    default -> false;
                };
            };
        }
        return result;
    }
}
