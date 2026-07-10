# Problem: Compute Min Edits (Insert, Delete, Replace) To Convert String A→B

## Source
- Platform: Anki deck seed
- Topic: Dynamic Programming
- Tags: DynamicProgramming, Strings, Day7
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given two strings, compute the minimum number of single-character edits (insert, delete, replace) needed to transform one string into the other.

## Recognition Pattern

- Edit Distance (Levenshtein Distance): two-sequence comparison where each position depends on prior prefix alignments.
- DP state over string lengths: `dp[i][j]` answers the min edits to transform `s1[0..i-1]` to `s2[0..j-1]`.
- Recurrence considers three operations: replace, insert, delete.

## Brute Force Thought

Try all possible sequences of edits: at each char, choose to match, replace, insert, or delete. This branches exponentially and is infeasible for long strings.

## Core Insight

Define `dp[i][j]` as the minimum edits to transform the first `i` chars of `s1` into the first `j` chars of `s2`.

Transition:
- If `s1[i-1] == s2[j-1]`, no edit needed: `dp[i][j] = dp[i-1][j-1]`
- Otherwise, take the minimum of:
  - Replace: `dp[i-1][j-1] + 1`
  - Delete from `s1`: `dp[i-1][j] + 1`
  - Insert into `s1`: `dp[i][j-1] + 1`

Base cases:
- `dp[0][j] = j` (insert `j` chars)
- `dp[i][0] = i` (delete `i` chars)

## Solution Approach

- Recursive approach / recurrence reasoning:
  - `minEdits(i, j)` returns the minimum edits to match `s1[0..i-1]` to `s2[0..j-1]`.
  - Base: if either string is empty, edits = length of the other.
  - Recurse on the three operations (replace, delete, insert).
- DP solution (memoization or bottom-up tabulation):
  - Use a 2D table indexed by positions in both strings to store computed min edits.
- Space-optimized DP (when applicable):
  - Reduce memory from `O(n*m)` to `O(m)` by storing only the previous row and updating the current row left-to-right.

## Thought Process During Solving

1. Recognize that this is a classic two-string alignment problem.
2. Define state as the minimum cost to match two prefixes.
3. Build the table from base cases (empty strings) upward.
4. When optimizing space, ensure you can compute the current row from the previous row and the left neighbor.

## Java Solution

```java
class Solution {
    // Full 2D DP solution
    public int minDistance(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        // Base cases
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        // Fill the table
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                        Math.min(dp[i - 1][j], dp[i][j - 1]),  // delete or insert
                        dp[i - 1][j - 1]                        // replace
                    );
                }
            }
        }

        return dp[n][m];
    }

    // Space-optimized DP solution (O(m) space)
    public int minDistanceOptimized(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        // Initialize first row
        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(
                        Math.min(prev[j], curr[j - 1]),
                        prev[j - 1]
                    );
                }
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[m];
    }
}
```

## Complexity
- Time: O(n * m), where `n = s1.length()` and `m = s2.length()`.
- Space: O(n * m) for the 2D table, or O(m) for the space-optimized version.

## Edge Cases / Traps

- Empty strings: if either string is empty, the edit distance is the length of the other.
- Identical strings: edit distance is 0.
- Single-character strings: edit distance is 0 if equal, 1 if different.
- Off-by-one errors: ensure indices map correctly (`s1[i-1]` for position `i` in the DP table).
- When optimizing space, swap `prev` and `curr` carefully to avoid losing needed values.

## Why This Works

Each `dp[i][j]` value represents the optimal solution for a smaller subproblem (shorter prefixes). By filling the table in order of increasing prefix lengths, we build up the solution for the full strings. The recurrence considers all ways to achieve alignment and picks the minimum-cost option.

## Interview Explanation

"This is the classic edit distance (Levenshtein distance) problem. I define the state as the minimum edits to match two prefixes, then build a table where each cell considers three operations: replace, insert, or delete. If characters match, no edit is needed. I fill the table bottom-up in O(n*m) time."

## Similar Problems

- Longest Common Subsequence
- Longest Palindromic Subsequence
- Regular Expression Matching
- Distinct Subsequences

## Anki Recall Prompts

- What is the DP state for edit distance?
- How do you handle the case when characters match vs. don't match?
- What are the three edit operations in the recurrence?
- Can edit distance DP be space-optimized? How?
