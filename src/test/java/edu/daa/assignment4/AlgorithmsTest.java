package edu.daa.assignment4;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {

    @Test
    void distanceVersionMethodsReturnExpectedValues() {
        EditDistance.RecursiveVersion recursive =
                new EditDistance.RecursiveVersion();
        EditDistance.TableVersion table =
                new EditDistance.TableVersion();
        EditDistance.TwoRowsVersion twoRows =
                new EditDistance.TwoRowsVersion();

        assertEquals(3, recursive.distance("kitten", "sitting"));
        assertEquals(3, table.distance("kitten", "sitting"));
        assertEquals(3, twoRows.distance("kitten", "sitting"));
    }

    @Test
    void searchMethodsReturnPositionLists() {
        assertEquals(
                List.of(0, 1, 2, 3),
                StringSearch.naiveSearch("AAAAA", "AA")
        );

        assertEquals(
                List.of(0, 2, 4),
                StringSearch.kmpSearch("ABABABA", "ABA")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> StringSearch.naiveSearch("abc", "")
        );
    }

    @Test
    void editDistanceReferenceValues() {
        assertDistance("kitten", "sitting", 3);
        assertDistance("flaw", "lawn", 2);
        assertDistance("intention", "execution", 5);
        assertDistance("", "abc", 3);
        assertDistance("abc", "", 3);
        assertDistance("abc", "abc", 0);
    }

    @Test
    void lpsReferenceValues() {
        assertArrayEquals(
                new int[]{0, 1, 2, 0, 1, 2, 3, 3},
                StringSearch.lps("AAACAAAA")
        );

        assertArrayEquals(
                new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4},
                StringSearch.lps("ABABCABAB")
        );
    }

    @Test
    void searchReferenceValuesAndOverlaps() {
        assertEquals(
                List.of(10),
                StringSearch.kmp("ABABDABACDABABCABAB", "ABABCABAB").positions()
        );

        assertEquals(
                List.of(0, 1, 2, 3),
                StringSearch.naive("AAAAA", "AA").positions()
        );

        assertEquals(
                List.of(0, 2, 4),
                StringSearch.kmp("ABABABA", "ABA").positions()
        );
    }

    @Test
    void searchEdgeCases() {
        assertThrows(
                IllegalArgumentException.class,
                () -> StringSearch.kmp("abc", "")
        );

        assertEquals(List.of(), StringSearch.naive("a", "long").positions());
        assertEquals(List.of(), StringSearch.kmp("a", "long").positions());
    }

    @Test
    void randomInputsGiveMatchingAnswers() {
        Random random = new Random(41);

        for (int test = 0; test < 100; test++) {
            String a = randomWord(random, random.nextInt(7));
            String b = randomWord(random, random.nextInt(7));

            int expected = EditDistance.table(a, b).distance();
            assertEquals(expected, EditDistance.recursive(a, b).distance());
            assertEquals(expected, EditDistance.twoRows(a, b).distance());

            String text = randomWord(random, 10);
            String pattern = randomWord(random, 1 + random.nextInt(4));

            assertEquals(
                    StringSearch.naive(text, pattern).positions(),
                    StringSearch.kmp(text, pattern).positions()
            );
        }
    }

    private static void assertDistance(String a, String b, int expected) {
        assertEquals(expected, EditDistance.recursive(a, b).distance());
        assertEquals(expected, EditDistance.table(a, b).distance());
        assertEquals(expected, EditDistance.twoRows(a, b).distance());
    }

    private static String randomWord(Random random, int length) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            result.append((char) ('A' + random.nextInt(3)));
        }

        return result.toString();
    }
}
