package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {

        List<SleepingSession> nightSessions = sessions.stream()
                .filter(s -> !s.getStart().toLocalDate().equals(s.getEnd().toLocalDate()))
                .collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", "Голубь (недостаточно данных)");
        }

        Map<String, Long> counts = nightSessions.stream()
                .map(s -> {
                    boolean isOwl = s.getStart().toLocalTime().isAfter(LocalTime.of(23, 0))
                            && s.getEnd().toLocalTime().isAfter(LocalTime.of(9, 0));
                    boolean isLark = s.getStart().toLocalTime().isBefore(LocalTime.of(22, 0))
                            && s.getEnd().toLocalTime().isBefore(LocalTime.of(7, 0));

                    if (isOwl) return "Сова";
                    if (isLark) return "Жаворонок";
                    return "Голубь";
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long maxVal = counts.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);


        long typesWithMax = counts.values().stream()
                .filter(v -> v.equals(maxVal))
                .count();

        String dominant;
        if (typesWithMax > 1) {
            dominant = "Голубь";
        } else {
            dominant = counts.entrySet().stream()
                    .filter(e -> e.getValue() == maxVal)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("Голубь");
        }

        return new SleepAnalysisResult("Хронотип пользователя", dominant);
    }
}