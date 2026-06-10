package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {

    private SleepingSession session(int startDay, int startHour, int startMin, int endDay, int endHour, int endMin,
                                    SleepQuality quality) {
        return new SleepingSession(
                LocalDateTime.of(2025, 10, startDay, startHour, startMin),
                LocalDateTime.of(2025, 10, endDay, endHour, endMin), quality);
    }

    //Тесты для TotalSessionsFunction
    @Test
    public void testTotalSessionsEmpty() {
        assertEquals("0", new TotalSessionsFunction().analyze(Collections.emptyList()).getValue());
    }

    @Test
    public void testTotalSessionsMultiple() {
        List<SleepingSession> sessions = Arrays.asList(session(1, 22, 0, 2, 8,
                0, SleepQuality.GOOD), session(2, 23, 0, 3, 7,
                0, SleepQuality.NORMAL));
        assertEquals("2", new TotalSessionsFunction().analyze(sessions).getValue());
    }

    // Тесты для MinDurationFunction
    @Test
    public void testMinDuration() {
        List<SleepingSession> sessions = Arrays.asList(session(1, 22, 0, 2, 8,
                0, SleepQuality.GOOD), session(2, 14, 0, 2, 15,
                0, SleepQuality.NORMAL)); // 600 мин и 60 мин
        assertEquals("60", new MinDurationFunction().analyze(sessions).getValue());
    }

    @Test
    public void testMinDurationSingle() {
        List<SleepingSession> sessions = Collections.singletonList(session(1, 22, 0, 2,
                8, 0, SleepQuality.GOOD));
        assertEquals("600", new MinDurationFunction().analyze(sessions).getValue());
    }

    // Тесты для MaxDurationFunction
    @Test
    public void testMaxDuration() {
        List<SleepingSession> sessions = Arrays.asList(session(1, 22, 0, 2, 8,
                0, SleepQuality.GOOD), session(2, 14, 0, 2, 15,
                0, SleepQuality.NORMAL));
        assertEquals("600", new MaxDurationFunction().analyze(sessions).getValue());
    }

    @Test
    public void testMaxDurationSingle() {
        List<SleepingSession> sessions = Collections.singletonList(session(1, 22, 0, 2,
                8, 0, SleepQuality.GOOD));
        assertEquals("600", new MaxDurationFunction().analyze(sessions).getValue());
    }

    // Тесты для AvgDurationFunction
    @Test
    public void testAvgDuration() {
        List<SleepingSession> sessions = Arrays.asList(session(1, 22, 0, 2, 8,
                0, SleepQuality.GOOD), session(2, 22, 0, 3, 8,
                0, SleepQuality.NORMAL)); // 600 и 600
        assertEquals("600.0", new AvgDurationFunction().analyze(sessions).getValue());
    }

    @Test
    public void testAvgDurationDifferent() {
        List<SleepingSession> sessions = Arrays.asList(session(1, 22, 0, 2, 8,
                0, SleepQuality.GOOD), session(2, 14, 0, 2, 15,
                0, SleepQuality.NORMAL)); // 600 и 60
        assertEquals("330.0", new AvgDurationFunction().analyze(sessions).getValue());
    }

    // Тесты для BadQualityCountFunction
    @Test
    public void testBadQualityCount() {
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 22, 0, 2, 8, 0, SleepQuality.GOOD),
                session(2, 22, 0, 3, 8, 0, SleepQuality.BAD),
                session(3, 22, 0, 4, 8, 0, SleepQuality.BAD)
        );
        assertEquals("2", new BadQualityCountFunction().analyze(sessions).getValue());
    }

    @Test
    public void testBadQualityCountZero() {
        List<SleepingSession> sessions = Arrays.asList(session(1, 22, 0, 2, 8,
                0, SleepQuality.GOOD), session(2, 22, 0, 3, 8,
                0, SleepQuality.NORMAL));
        assertEquals("0", new BadQualityCountFunction().analyze(sessions).getValue());
    }

    // Тесты для SleeplessNightsFunction
    @Test
    public void testSleeplessNightsNormal() {
        // Нормальный сон с 23:00 до 08:00 (пересекает 00:00-06:00)
        List<SleepingSession> sessions = Collections.singletonList(session(1, 23, 0, 2,
                8, 0, SleepQuality.GOOD));
        assertEquals("0", new SleeplessNightsFunction().analyze(sessions).getValue());
    }

    @Test
    public void testSleeplessNightsLateSleepEarlyWake() {
        // Сон с 02:00 до 07:00 (пересекает 00:00-06:00)
        List<SleepingSession> sessions = Collections.singletonList(session(1, 2, 0, 1,
                7, 0, SleepQuality.GOOD));
        assertEquals("0", new SleeplessNightsFunction().analyze(sessions).getValue());
    }

    @Test
    public void testSleeplessNightsDaytimeNapOnly() {
        // Дневной сон с 07:00 до 11:00 (НЕ пересекает 00:00-06:00) -> 1 бессонная ночь
        List<SleepingSession> sessions = Collections.singletonList(session(1, 7, 0, 1,
                11, 0, SleepQuality.NORMAL));
        assertEquals("1", new SleeplessNightsFunction().analyze(sessions).getValue());
    }

    @Test
    public void testSleeplessNightsMultipleDaysWithGap() {
        // Сон 1-го числа и 3-го числа. 2-е число - бессонная ночь.
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 23, 0, 2, 8, 0, SleepQuality.GOOD),
                session(3, 23, 0, 4, 8, 0, SleepQuality.GOOD)
        );
        assertEquals("1", new SleeplessNightsFunction().analyze(sessions).getValue());
    }

    @Test
    public void testSleeplessNightsEdgeCaseMidnight() {
        // Сон с 23:00 до 05:00 (строго пересекает 00:00-06:00)
        List<SleepingSession> sessions = Collections.singletonList(session(1, 23, 0, 2,
                5, 0, SleepQuality.GOOD));
        assertEquals("0", new SleeplessNightsFunction().analyze(sessions).getValue());
    }

    @Test
    public void testSleeplessNightsOverOneMonth() {
        // Сессия 1: 01.10 23:00 - 02.10 08:00 (спал ночь с 01 на 02)
        // Сессия 2: 04.11 23:00 - 05.11 08:00 (спал ночь с 04 на 05)
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 8, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 11, 4, 23, 0),
                        LocalDateTime.of(2025, 11, 5, 8, 0),
                        SleepQuality.GOOD
                )
        );

        assertEquals("33", new SleeplessNightsFunction().analyze(sessions).getValue());
    }

    // Тесты для ChronotypeFunction
    @Test
    public void testChronotypeOwl() {
        // Засыпание после 23:00, пробуждение после 9:00
        List<SleepingSession> sessions = Collections.singletonList(session(1, 23, 30,
                2, 9, 30, SleepQuality.GOOD));
        assertEquals("Сова", new ChronotypeFunction().analyze(sessions).getValue());
    }

    @Test
    public void testChronotypeTieBreaker() {
        // 1 Сова, 1 Жаворонок -> должен быть Голубь по правилам
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 23, 30, 2, 9, 30, SleepQuality.GOOD), // Сова
                session(2, 21, 0, 3, 6, 0, SleepQuality.GOOD)    // Жаворонок
        );
        assertEquals("Голубь", new ChronotypeFunction().analyze(sessions).getValue());
    }

    @Test
    public void testChronotypeIgnoresDaytime() {
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 14, 0, 1, 15, 0, SleepQuality.NORMAL), // Дневной, игнорируется
                session(1, 23, 30, 2, 9, 30, SleepQuality.GOOD)   // Сова
        );
        assertEquals("Сова", new ChronotypeFunction().analyze(sessions).getValue());
    }

    @Test
    public void testChronotypeLark() {
        List<SleepingSession> sessions = Collections.singletonList(session(1, 21, 0, 2,
                6, 0, SleepQuality.GOOD));
        assertEquals("Жаворонок", new ChronotypeFunction().analyze(sessions).getValue());
    }
}