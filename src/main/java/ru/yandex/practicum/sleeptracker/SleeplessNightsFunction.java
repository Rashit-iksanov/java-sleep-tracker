package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleeplessNightsFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", "0");
        }

        // Границы периода логирования
        LocalDateTime firstStart = sessions.stream()
                .map(SleepingSession::getStart)
                .min(Comparator.naturalOrder())
                .orElseThrow();

        LocalDateTime lastEnd = sessions.stream()
                .map(SleepingSession::getEnd)
                .max(Comparator.naturalOrder())
                .orElseThrow();

        // Если началось после 12:00, "следующая ночь" начинается в этот же день вечером.
        LocalDate startDate = firstStart.toLocalTime().isAfter(LocalTime.of(12, 0))
                ? firstStart.toLocalDate()
                : firstStart.toLocalDate().minusDays(1);

        // Если закончилось после 12:00, "следующая ночь" начнется завтра.
        LocalDate endDate = lastEnd.toLocalTime().isAfter(LocalTime.of(12, 0))
                ? lastEnd.toLocalDate().plusDays(1)
                : lastEnd.toLocalDate();

        long totalNights = ChronoUnit.DAYS.between(startDate, endDate);

        if (totalNights <= 0) {
            return new SleepAnalysisResult("Количество бессонных ночей", "0");
        }

        Set<LocalDate> nightsWithSleep = sessions.stream()
                .flatMap(s -> {
                    LocalDate minDate = s.getStart().toLocalDate().minusDays(1);
                    LocalDate maxDate = s.getEnd().toLocalDate();
                    long days = ChronoUnit.DAYS.between(minDate, maxDate) + 1;
                    return Stream.iterate(minDate, d -> d.plusDays(1))
                            .limit(days)
                            .filter(d -> {
                                LocalDateTime nightCheckStart = d.plusDays(1).atStartOfDay();
                                LocalDateTime nightCheckEnd = d.plusDays(1).atTime(6, 0);
                                return s.getStart().isBefore(nightCheckEnd) && s.getEnd().isAfter(nightCheckStart);
                            });
                })
                .collect(Collectors.toSet());

        long sleeplessCount = Stream.iterate(startDate, d -> d.plusDays(1))
                .limit(totalNights)
                .filter(d -> !nightsWithSleep.contains(d))
                .count();

        return new SleepAnalysisResult("Количество бессонных ночей", String.valueOf(sleeplessCount));
    }
}