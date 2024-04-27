package com.exercise.ContactDuplicateDetector.util;

public class StringUtils {
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

                // calculate cost of possibles operations
                int insertionOrElimination = 1 + Math.min(cost[j], cost[j - 1]);
                int substitution = x.charAt(i - 1) == y.charAt(j - 1) ? nw : nw + 1;

                // choose less cost
                int cost_j = Math.min(insertionOrElimination, substitution);

                // update north-west and cost[j]
                nw = cost[j];
                cost[j] = cost_j;
            }
        }

        return cost[y.length()];
    }
}
