package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.event.LightSensorEventProto;
import ru.yandex.practicum.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class LightSensorEventHandler extends BaseSensorEventHandler {
    public LightSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SensorEventProto processSpecificPayload(SensorEventProto.Builder builder, SensorEventProto event) {
        LightSensorEventProto lightSensorEvent = event.getLightSensorEventProto();

        return builder
                .setLightSensorEventProto(
                        LightSensorEventProto.newBuilder(lightSensorEvent)
                                .build()
                )
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT_PROTO;
    }
}
