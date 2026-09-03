package ru.yandex.practicum.sleeptracker;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class MaxDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        long max = sessions.stream()
                .mapToLong(s -> ChronoUnit.MINUTES.between(s.getStart(), s.getEnd()))
                .max()
                .orElse(0);
        return new SleepAnalysisResult("Максимальная продолжительность сессии (мин)", String.valueOf(max));
    }
}