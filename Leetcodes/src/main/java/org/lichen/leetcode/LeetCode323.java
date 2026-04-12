package org.lichen.leetcode;


import java.util.stream.IntStream;

public class LeetCode323 {

    public static int countGraphs(int n, int[][] edges) {
        int[] roots = new int[n];
        IntStream.range(0, n).forEach(x -> roots[x] = x);

        int res = n;
        for (int[] pair: edges) {
            int l = pair[0];
            int r = pair[1];
            int rootLeft = findRoot(roots, l);
            int rootRight = findRoot(roots, r);
            if (rootLeft != rootRight) {
                roots[rootLeft] = rootRight;
                res--;
            }
        }
        return res;
    }

    private static int findRoot(int[] roots, int target) {
        if (roots[target] != target) {
            roots[target] = findRoot(roots, roots[target]);
        }
        return roots[target];
    }

    public static void main(String[] args) {
        int n = 5;
        int[][] edges = {{0, 1}, {4, 3}, {2, 3}, {2, 4}};

        System.err.println(countGraphs(n, edges));
    }
}
