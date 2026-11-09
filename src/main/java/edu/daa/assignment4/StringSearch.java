package edu.daa.assignment4;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StringSearch {
    private StringSearch() {}

    public record SearchResult(List<Integer> positions, long comparisons) {}

    public static int[] lps(String pattern) {
        Objects.requireNonNull(pattern);

        int[] lps = new int[pattern.length()];
        int length = 0;
        int i = 1;

        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else if (length > 0) {
                length = lps[length - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }

        return lps;
    }

    public static SearchResult naive(String text, String pattern) {
        checkPattern(pattern);

        List<Integer> positions = new ArrayList<>();
        long comparisons = 0;

        if (pattern.length() > text.length()) {
            return new SearchResult(List.of(), comparisons);
        }

        for (int start = 0; start <= text.length() - pattern.length(); start++) {
            int j = 0;

            while (j < pattern.length()) {
                comparisons++;

                if (text.charAt(start + j) != pattern.charAt(j)) {
                    break;
                }

                j++;
            }

            if (j == pattern.length()) {
                positions.add(start);
            }
        }

        return new SearchResult(List.copyOf(positions), comparisons);
    }

    public static SearchResult kmp(String text, String pattern) {
        checkPattern(pattern);

        List<Integer> positions = new ArrayList<>();
        long comparisons = 0;

        if (pattern.length() > text.length()) {
            return new SearchResult(List.of(), comparisons);
        }

        int[] lps = lps(pattern);
        int i = 0;
        int j = 0;

        while (i < text.length()) {
            comparisons++;

            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                if (j == pattern.length()) {
                    positions.add(i - j);
                    j = lps[j - 1];
                }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }

        return new SearchResult(List.copyOf(positions), comparisons);
    }

    public static List<Integer> naiveSearch(String text, String pattern) {
        return naive(text, pattern).positions();
    }

    public static List<Integer> kmpSearch(String text, String pattern) {
        return kmp(text, pattern).positions();
    }

    private static void checkPattern(String pattern) {
        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern must not be empty");
        }
    }
}
