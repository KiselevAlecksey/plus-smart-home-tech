package ru.yandex.practicum.telemetry.analyzer.config;

import java.util.Map;
import java.util.Properties;

public interface HubEventConsumer {
    Properties getHubProperties();

    Map<String, String> getHubTopics();
}
