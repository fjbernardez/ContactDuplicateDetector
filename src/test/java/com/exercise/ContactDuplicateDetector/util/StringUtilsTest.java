package com.exercise.ContactDuplicateDetector.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StringUtilsTest {
    @Test
    void levenshteinDistanceTest_isZero() {
        final String x = "lj++#4dncjkndcDsksjd''_kjndcEADkdkdjE";
        final String y = "lj++#4dncjkndcDsksjd''_kjndcEADkdkdjE";
        // No differences between the strings, therefore no operations are performed.
        assertEquals(0, StringUtils.levenshteinDistance(x, y), "The distance should be zero when both strings are identical.");
    }

    @Test
    void levenshteinDistanceTest_withInsertions() {
        final String x = "hello";
        final String y = "hello world";
        // Characters " world" are inserted at the end of "hello".
        assertEquals(6, StringUtils.levenshteinDistance(x, y), "The distance should be 6 due to six insertions.");
    }

    @Test
    void levenshteinDistanceTest_withDeletions() {
        final String x = "hello world";
        final String y = "hello";
        // Characters " world" are deleted from the end of "hello world".
        assertEquals(6, StringUtils.levenshteinDistance(x, y), "The distance should be 6 due to six deletions.");
    }

    @Test
    void levenshteinDistanceTest_withSubstitutions() {
        final String x = "kitten";
        final String y = "sitting";
        // Characters "k" is substituted by "s" and "e" by "i", plus an insertion of "g" at the end.
        assertEquals(3, StringUtils.levenshteinDistance(x, y), "The distance should be 3 due to two substitutions and one insertion.");
    }

    @Test
    void levenshteinDistanceTest_withMixedOperations() {
        final String x = "Sunday";
        final String y = "Saturday";
        // "s" remains the same, "un" is substituted by "atur" requiring two substitutions, and "a" is inserted before "t".
        assertEquals(3, StringUtils.levenshteinDistance(x, y), "The distance should be 3 due to one insertion and two substitutions.");
    }
}
