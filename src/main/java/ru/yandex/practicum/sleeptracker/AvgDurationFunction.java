package ru.yandex.practicum.sleeptracker;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

public class AvgDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        double avg = sessions.stream()
                .mapToLong(s -> ChronoUnit.MINUTES.between(s.getStart(), s.getEnd()))
                .average()
                .orElse(0.0);
        return new SleepAnalysisResult("Средняя продолжительность сессии (мин)", String.format(Locale.US, "%.1f", avg));
    }
}