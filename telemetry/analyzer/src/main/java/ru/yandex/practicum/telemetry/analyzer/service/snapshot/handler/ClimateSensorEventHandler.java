package ru.yandex.practicum.telemetry.analyzer.service.snapshot.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.db.condition.Condition;

import java.util.Collection;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {
    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR;
    }

    @Override
    public boolean check(SensorStateAvro sensorState, Collection<Condition> conditions) {
        boolean result = true;
        ClimateSensorAvro climateSensor = (ClimateSensorAvro) sensorState.getData();
        for (Condition condition : conditions) {
            result = result && switch (condition.getOperation()) {
                case EQUALS -> switch (condition.getType()) {
                    case TEMPERATURE -> climateSensor.getTemperatureC() == condition.getValue();
                    case CO2LEVEL -> climateSensor.getCo2Level() == condition.getValue();
                    case HUMIDITY -> climateSensor.getHumidity() == condition.getValue();
                    default -> false;
                };
                case GREATER_THAN -> switch (condition.getType()) {
                    case TEMPERATURE -> climateSensor.getTemperatureC() > condition.getValue();
                    case CO2LEVEL -> climateSensor.getCo2Level() > condition.getValue();
                    case HUMIDITY -> climateSensor.getHumidity() > condition.getValue();
                    default -> false;
                };
                case LOWER_THAN -> switch (condition.getType()) {
                    case TEMPERATURE -> climateSensor.getTemperatureC() < condition.getValue();
                    case CO2LEVEL -> climateSensor.getCo2Level() < condition.getValue();
                    case HUMIDITY -> climateSensor.getHumidity() < condition.getValue();
                    default -> false;
                };
            };
        }
        return result;
    }
}
