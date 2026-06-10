package ru.yandex.practicum.sleeptracker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleepTrackerApp {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Ошибка: укажите путь к файлу лога сна в качестве аргумента.");
            return;
        }

        Path filePath = Paths.get(args[0]);

        try (Stream<String> lines = Files.lines(filePath)) {
            List<SleepingSession> sessions = lines
                    .filter(line -> !line.trim().isEmpty())
                    .map(SleepingSession::parse)
                    .collect(Collectors.toList());

            List<SleepAnalysisFunction> analysisFunctions = Arrays.asList(
                    new TotalSessionsFunction(),
                    new MinDurationFunction(),
                    new MaxDurationFunction(),
                    new AvgDurationFunction(),
                    new BadQualityCountFunction(),
                    new SleeplessNightsFunction(),
                    new ChronotypeFunction()
            );

            System.out.println("=== Результаты анализа сна ===");
            analysisFunctions.forEach(func -> {
                SleepAnalysisResult result = func.analyze(sessions);
                System.out.println(result.getDescription() + ": " + result.getValue());
            });

        } catch (Exception e) {
            System.err.println("Ошибка при обработке файла: " + e.getMessage());
        }
    }
}