package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SleepingSession {

    private final LocalDateTime start;
    private final LocalDateTime end;
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime start, LocalDateTime end, SleepQuality quality) {
        if (start == null || end == null || quality == null) {
            throw new IllegalArgumentException("Все параметры сессии сна должны быть заданы");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Время пробуждения не может быть раньше засыпания");
        }
        this.start = start;
        this.end = end;
        this.quality = quality;
    }

    public static SleepingSession parse(String line) {
        String[] parts = line.split(";");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        LocalDateTime start = LocalDateTime.parse(parts[0], formatter);
        LocalDateTime end = LocalDateTime.parse(parts[1], formatter);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim());
        return new SleepingSession(start, end, quality);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public SleepQuality getQuality() {
        return quality;
    }
}