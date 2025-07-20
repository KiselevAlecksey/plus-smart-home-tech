package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.event.TemperatureSensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler {
    public TemperatureSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SensorEventProto processSpecificPayload(SensorEventProto.Builder builder, SensorEventProto event) {
        TemperatureSensorEventProto switchSensorEventProto = event.getTemperatureSensorEventProto();

        return builder
                .setTemperatureSensorEventProto(
                        TemperatureSensorEventProto.newBuilder(switchSensorEventProto)
                                .build()
                )
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT_PROTO;
    }
}
