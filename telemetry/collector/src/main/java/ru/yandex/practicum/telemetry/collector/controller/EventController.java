package ru.yandex.practicum.telemetry.collector.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.sensor.SensorEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "events", description = "API для передачи событий от датчиков")
public class EventController {
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;

    public EventController(Set<HubEventHandler> hubEventHandlers, Set<SensorEventHandler> sensorEventHandlers) {
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
    }

    @PostMapping("/sensors")
    public void sensorEvent(@Valid @RequestBody SensorEvent event) {
        log.warn("==> SensorEvent {} start", event);
        SensorEventHandler sensorEventHandler = sensorEventHandlers.get(event.getType());
        if (sensorEventHandler == null) {
            throw new IllegalArgumentException("Не найден обработчик для события: " + event.getType());
        }
        sensorEventHandler.handle(event);
        log.warn("<== SensorEvent {} end", event);
    }

    @PostMapping("/hubs")
    public void hubEvent(@Valid @RequestBody HubEvent event) {
        log.warn("==> HubEvent {} start", event);
        HubEventHandler hubEventHandler = hubEventHandlers.get(event.getType());
        if (hubEventHandler == null) {
            throw new IllegalArgumentException("Не найден обработчик для события: " + event.getType());
        }
        hubEventHandler.handle(event);
        log.warn("<== HubEvent {} end", event);
    }
}
