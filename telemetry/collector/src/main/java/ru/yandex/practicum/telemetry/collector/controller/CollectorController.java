package ru.yandex.practicum.telemetry.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.sensor.SensorEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
@Tag(name = "events", description = "API для передачи событий от датчиков")
public class CollectorController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlers;
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlers;

    public CollectorController(Set<HubEventHandler> hubEventHandlers, Set<SensorEventHandler> sensorEventHandlers) {
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
    }

    @Override
    public void collectSensorEvent(SensorEventProto eventProto, StreamObserver<Empty> responseObserver) {
        log.warn("==> SensorEventProto {} start", eventProto);
        try {
            SensorEventHandler sensorEventHandler = sensorEventHandlers.get(eventProto.getPayloadCase());
            if (sensorEventHandler == null) {
                throw new IllegalArgumentException("Не найден обработчик для события: " + eventProto.getId());
            }
            sensorEventHandler.handle(eventProto);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception exception) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(exception.getLocalizedMessage())
                            .withCause(exception)
            ));
        }
        log.warn("<== SensorEventProto {} end", eventProto);
    }

    @Override
    public void collectHubEvent(HubEventProto eventProto, StreamObserver<Empty> responseObserver) {
        log.warn("==> HubEventProto {} start", eventProto);
        try {
            HubEventHandler hubEventHandler = hubEventHandlers.get(eventProto.getPayloadCase());
            if (hubEventHandler == null) {
                throw new IllegalArgumentException("Не найден обработчик для события: " + eventProto.getHubId());
            }
            hubEventHandler.handle(eventProto);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception exception) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(exception.getLocalizedMessage())
                            .withCause(exception)
            ));
        }
        log.warn("<== HubEventProto {} end", eventProto);
    }
}
