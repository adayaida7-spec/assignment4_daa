package edu.daa.assignment4;

public final class EditDistance {
    private EditDistance() {}

    public record Result(int distance, long operations) {}

    public static Result recursive(String a, String b) {
        Counter counter = new Counter();
        int answer = recursiveHelper(a, b, a.length(), b.length(), counter);
        return new Result(answer, counter.operations);
    }

    private static int recursiveHelper(
            String a, String b, int i, int j, Counter counter) {
        counter.operations++;

        if (i == 0) return j;
        if (j == 0) return i;

        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return recursiveHelper(a, b, i - 1, j - 1, counter);
        }

        int delete = recursiveHelper(a, b, i - 1, j, counter);
        int insert = recursiveHelper(a, b, i, j - 1, counter);
        int replace = recursiveHelper(a, b, i - 1, j - 1, counter);

        return 1 + Math.min(delete, Math.min(insert, replace));
    }

    public static Result table(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        long operations = 0;

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                operations++;

                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j],
                            Math.min(dp[i][j - 1], dp[i - 1][j - 1])
                    );
                }
            }
        }

        return new Result(dp[n][m], operations);
    }

    public static Result twoRows(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[] previous = new int[m + 1];
        int[] current = new int[m + 1];
        long operations = 0;

        for (int j = 0; j <= m; j++) previous[j] = j;

        for (int i = 1; i <= n; i++) {
            current[0] = i;

            for (int j = 1; j <= m; j++) {
                operations++;

                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    current[j] = previous[j - 1];
                } else {
                    current[j] = 1 + Math.min(
                            previous[j],
                            Math.min(current[j - 1], previous[j - 1])
                    );
                }
            }

            int[] temporary = previous;
            previous = current;
            current = temporary;
        }

        return new Result(previous[m], operations);
    }

    public static final class RecursiveVersion {
        public int distance(String a, String b) {
            return EditDistance.recursive(a, b).distance();
        }
    }

    public static final class TableVersion {
        public int distance(String a, String b) {
            return EditDistance.table(a, b).distance();
        }
    }

    public static final class TwoRowsVersion {
        public int distance(String a, String b) {
            return EditDistance.twoRows(a, b).distance();
        }
    }

    private static final class Counter {
        private long operations;
    }
}