package ru.yandex.practicum.telemetry.analyzer.config;

import java.util.Map;
import java.util.Properties;

public interface SnapshotsConsumer {
    Properties getSnapshotProperties();

    Map<String, String> getSnapshotTopics();
}
