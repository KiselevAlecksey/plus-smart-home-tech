package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.event.MotionSensorEventProto;
import ru.yandex.practicum.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler {
    public MotionSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SensorEventProto processSpecificPayload(SensorEventProto.Builder builder, SensorEventProto event) {
        MotionSensorEventProto motionSensorEventProto = event.getMotionSensorEventProto();

        return builder
                .setMotionSensorEventProto(
                        MotionSensorEventProto.newBuilder(motionSensorEventProto)
                        .build()
                )
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT_PROTO;
    }
}
