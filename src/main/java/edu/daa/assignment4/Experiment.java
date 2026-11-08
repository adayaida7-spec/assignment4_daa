package edu.daa.assignment4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.BiFunction;

public class Experiment {
    private static final long SEED = 20261108L;
    private static final Random RANDOM = new Random(SEED);

    private record DistanceRun(int distance, long operations) {}
    private record SearchRun(List<Integer> positions, long operations) {}

    public static void main(String[] args) throws IOException {
        Path resultsFile = Path.of("results", "results.csv");
        Files.createDirectories(resultsFile.getParent());

        List<String> rows = new ArrayList<>();
        rows.add("algorithm,input,n,run,time_ms,operations,matches");

        runEditDistanceExperiments(rows);
        runStringSearchExperiments(rows);

        Files.write(resultsFile, rows);
        System.out.println("Results written to " + resultsFile.toAbsolutePath());
    }

    private static void runEditDistanceExperiments(List<String> rows) {
        int[] sizes = {4, 8, 12, 16, 24, 32};

        for (int n : sizes) {
            String randomA = randomString(n);
            String randomB = randomString(n);

            runDistance(rows, "recursive", "random", n,
                    (a, b) -> EditDistance.recursive(a, b), randomA, randomB,
                    n <= 12);

            runDistance(rows, "table", "random", n,
                    (a, b) -> EditDistance.table(a, b), randomA, randomB, true);

            runDistance(rows, "two_rows", "random", n,
                    (a, b) -> EditDistance.twoRows(a, b), randomA, randomB, true);

            String same = randomString(n);

            runDistance(rows, "recursive", "identical", n,
                    (a, b) -> EditDistance.recursive(a, b), same, same,
                    true);

            runDistance(rows, "table", "identical", n,
                    (a, b) -> EditDistance.table(a, b), same, same, true);

            runDistance(rows, "two_rows", "identical", n,
                    (a, b) -> EditDistance.twoRows(a, b), same, same, true);
        }
    }

    private static void runDistance(
            List<String> rows,
            String algorithm,
            String inputType,
            int n,
            BiFunction<String, String, EditDistance.Result> function,
            String a,
            String b,
            boolean shouldRun
    ) {
        if (!shouldRun) {
            return;
        }

        for (int run = 1; run <= 3; run++) {
            long start = System.nanoTime();
            EditDistance.Result result = function.apply(a, b);
            double timeMs = (System.nanoTime() - start) / 1_000_000.0;

            rows.add(String.format(
                    Locale.ROOT,
                    "%s,%s,%d,%d,%.6f,%d,-1",
                    algorithm, inputType, n, run,
                    timeMs, result.operations()
            ));
        }
    }

    private static void runStringSearchExperiments(List<String> rows) {
        int[] textSizes = {128, 256, 512, 1024, 2048};

        for (int n : textSizes) {
            int patternLength = Math.max(3, n / 16);

            String randomText = randomString(n);
            String randomPattern = randomString(patternLength);

            runSearch(rows, "naive", "random", n, randomText, randomPattern);
            runSearch(rows, "kmp", "random", n, randomText, randomPattern);

            String hardText = "A".repeat(n - 1) + "B";
            String hardPattern = "A".repeat(patternLength - 1) + "B";

            runSearch(rows, "naive", "repeated_near_match",
                    n, hardText, hardPattern);
            runSearch(rows, "kmp", "repeated_near_match",
                    n, hardText, hardPattern);
        }
    }

    private static void runSearch(
            List<String> rows,
            String algorithm,
            String inputType,
            int n,
            String text,
            String pattern
    ) {
        for (int run = 1; run <= 5; run++) {
            long start = System.nanoTime();

            StringSearch.SearchResult result =
                    algorithm.equals("naive")
                            ? StringSearch.naive(text, pattern)
                            : StringSearch.kmp(text, pattern);

            double timeMs = (System.nanoTime() - start) / 1_000_000.0;

            rows.add(String.format(
                    Locale.ROOT,
                    "%s,%s,%d,%d,%.6f,%d,%d",
                    algorithm, inputType, n, run,
                    timeMs, result.comparisons(), result.positions().size()
            ));
        }
    }

    private static String randomString(int length) {
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            result.append((char) ('A' + RANDOM.nextInt(4)));
        }

        return result.toString();
    }
}
