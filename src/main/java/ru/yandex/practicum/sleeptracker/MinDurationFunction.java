package ru.yandex.practicum.sleeptracker;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class MinDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        long min = sessions.stream()
                .mapToLong(s -> ChronoUnit.MINUTES.between(s.getStart(), s.getEnd()))
                .min()
                .orElse(0);
        return new SleepAnalysisResult("Минимальная продолжительность сессии (мин)", String.valueOf(min));
    }
}