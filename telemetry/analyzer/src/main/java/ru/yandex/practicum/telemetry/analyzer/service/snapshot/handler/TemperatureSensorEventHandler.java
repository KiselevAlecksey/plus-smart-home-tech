package ru.yandex.practicum.telemetry.analyzer.service.snapshot.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.telemetry.analyzer.db.condition.Condition;

import java.util.Collection;

@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {
    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR;
    }

    @Override
    public boolean check(SensorStateAvro sensorState, Collection<Condition> conditions) {
        boolean result = true;
        TemperatureSensorAvro temperatureSensor = (TemperatureSensorAvro) sensorState.getData();
        for (Condition condition : conditions) {
            result = result && switch (condition.getOperation()) {
                case EQUALS -> switch (condition.getType()) {
                    case TEMPERATURE -> temperatureSensor.getTemperatureC() == condition.getValue();
                    default -> false;
                };
                case GREATER_THAN -> switch (condition.getType()) {
                    case TEMPERATURE -> temperatureSensor.getTemperatureC() > condition.getValue();
                    default -> false;
                };
                case LOWER_THAN -> switch (condition.getType()) {
                    case TEMPERATURE -> temperatureSensor.getTemperatureC() < condition.getValue();
                    default -> false;
                };
            };
        }
        return result;
    }
}
