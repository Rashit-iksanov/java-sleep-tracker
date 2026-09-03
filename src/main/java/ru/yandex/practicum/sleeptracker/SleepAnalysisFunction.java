package ru.yandex.practicum.sleeptracker;

import java.util.List;

@FunctionalInterface
interface SleepAnalysisFunction {
    SleepAnalysisResult analyze(List<SleepingSession> sessions);
}