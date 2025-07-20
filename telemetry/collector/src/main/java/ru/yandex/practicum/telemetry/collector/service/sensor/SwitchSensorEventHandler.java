package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.event.SwitchSensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler {
    public SwitchSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SensorEventProto processSpecificPayload(SensorEventProto.Builder builder, SensorEventProto event) {
        SwitchSensorEventProto switchSensorEventProto = event.getSwitchSensorEventProto();

        return builder
                .setSwitchSensorEventProto(
                        SwitchSensorEventProto.newBuilder(switchSensorEventProto)
                                .build()
                )
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT_PROTO;
    }
}
