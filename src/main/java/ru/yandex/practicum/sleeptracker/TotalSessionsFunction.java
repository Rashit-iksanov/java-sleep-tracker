package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class TotalSessionsFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        return new SleepAnalysisResult("Всего сессий сна", String.valueOf(sessions.size()));
    }
}