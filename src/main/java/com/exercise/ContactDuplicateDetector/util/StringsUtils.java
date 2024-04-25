package com.exercise.ContactDuplicateDetector.util;

public class StringsUtils {
    public static int levenshteinDistance(String x, String y) {
        x = x.toLowerCase();
        y = y.toLowerCase();
        int[] cost = new int[y.length() + 1];

        for (int j = 0; j < cost.length; j++) {
            cost[j] = j;
        }

        for (int i = 1; i <= x.length(); i++) {
            cost[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= y.length(); j++) {
                int cj = Math.min(1 + Math.min(cost[j], cost[j - 1]), x.charAt(i - 1) == y.charAt(j - 1) ? nw : nw + 1);
                nw = cost[j];
                cost[j] = cj;
            }
        }

        return cost[y.length()];
    }
}
