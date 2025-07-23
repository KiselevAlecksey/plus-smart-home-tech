package ru.yandex.practicum.telemetry.analyzer.db.action;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.telemetry.analyzer.db.action.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
