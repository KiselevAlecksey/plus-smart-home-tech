package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.event.ClimateSensorEventProto;
import ru.yandex.practicum.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler {
    public ClimateSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SensorEventProto processSpecificPayload(SensorEventProto.Builder builder, SensorEventProto event) {
        ClimateSensorEventProto climateSensorEvent = event.getClimateSensorEventProto();

        return builder
                .setClimateSensorEventProto(
                        ClimateSensorEventProto.newBuilder(climateSensorEvent)
                                .setCo2Level(climateSensorEvent.getCo2Level())
                                .setHumidity(climateSensorEvent.getHumidity())
                                .setTemperatureC(climateSensorEvent.getTemperatureC())
                                .build()
                )
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT_PROTO;
    }
}
