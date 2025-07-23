package ru.yandex.practicum.telemetry.analyzer.service.snapshot.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.db.condition.Condition;

import java.util.Collection;

@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {
    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR;
    }

    @Override
    public boolean check(SensorStateAvro sensorState, Collection<Condition> conditions) {
        boolean result = true;
        MotionSensorAvro motionSensor = (MotionSensorAvro) sensorState.getData();
        for (Condition condition : conditions) {
            result = result && switch (condition.getOperation()) {
                case EQUALS -> switch (condition.getType()) {
                    case MOTION -> {
                        boolean motion = condition.getValue() != 0;
                        yield motionSensor.getMotion() == motion;
                    }
                    default -> false;
                };
                default -> false;
            };
        }
        return result;
    }
}
