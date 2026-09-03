package ru.yandex.practicum.sleeptracker;

public class SleepAnalysisResult {
    private String description;
    private Object result;

    public SleepAnalysisResult(String description, Object value) {
        this.description = description;
        this.result = value;
    }

    public String getDescription() {
        return description;
    }

    public Object getValue() {
        return result;
    }

    @Override
    public String toString() {
        return description + ": " + result;
    }
}