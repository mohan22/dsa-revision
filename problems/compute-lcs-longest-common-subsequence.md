# Problem: Compute LCS (Longest Common Subsequence)

## Source
- Platform: Anki deck seed
- Topic: Dynamic Programming
- Tags: DP, Strings, LCS, Day17
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given two strings, compute the length of their longest common subsequence.

## Recognition Pattern

- Two-string subsequence DP: compare prefixes of both strings.
- State is defined by the lengths of prefixes from `s1` and `s2`.
- Transition chooses whether to match characters or skip one side.

## Brute Force Thought

Try every subsequence of one string and check if it is also a subsequence of the other. This is exponential because there are `2^n` subsequences to consider.

## Core Insight

Define `dp[i][j]` as the length of the longest common subsequence of `s1[0..i-1]` and `s2[0..j-1]`.

Transition:
- If `s1.charAt(i-1) == s2.charAt(j-1)`: `dp[i][j] = dp[i-1][j-1] + 1`
- Otherwise: `dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1])`

Base cases:
- `dp[0][j] = 0`
- `dp[i][0] = 0`

## Solution Approach

- Recursive approach / recurrence reasoning:
  - `lcs(i, j)` returns the longest common subsequence length for prefixes `s1[0..i-1]` and `s2[0..j-1]`.
  - If current chars match, take 1 + `lcs(i-1, j-1)`.
  - Otherwise, take the max of skipping one char from either string.
- DP solution (memoization or bottom-up tabulation):
  - Fill a 2D table over indices `i` and `j`.
- Space-optimized DP (when applicable):
  - Reduce to O(min(n, m)) space by keeping only the previous row and current row.

## Thought Process During Solving

1. Identify the subproblem: longest common subsequence for two prefixes.
2. Recognize that the subproblem depends only on smaller prefix pairs.
3. Use DP to avoid recomputing overlap between `lcs(i-1, j)` and `lcs(i, j-1)`.
4. For space optimization, note that each row depends only on the previous row.

## Java Solution

```java
class Solution {
    public int longestCommonSubsequence(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[n][m];
    }

    public int longestCommonSubsequenceOptimized(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        if (n < m) {
            return longestCommonSubsequenceOptimized(s2, s1);
        }

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    curr[j] = prev[j - 1] + 1;
                } else {
                    curr[j] = Math.max(prev[j], curr[j - 1]);
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
- Space: O(n * m) for the full DP table, or O(min(n, m)) for the optimized version.

## Edge Cases / Traps

- Empty strings produce an LCS length of 0.
- Repeated characters rely on the DP recurrence to choose the best skip.
- Off-by-one errors when mapping string indices to DP table indices.
- When optimizing space, ensure the shorter string is used for the DP row width.

## Why This Works

Each `dp[i][j]` stores the answer for a smaller prefix pair, and the recurrence uses previously computed results to build the full answer without repeated work. The match case extends the subsequence, while the skip case chooses the best prefix alignment.

## Interview Explanation

"This problem is a classic two-string DP. I define state by prefix lengths and either match both chars to extend the subsequence or skip one char to compare smaller prefixes. Filling the DP table yields the answer in O(n*m) time."

## Similar Problems

- Longest Palindromic Subsequence
- Shortest Common Supersequence
- Edit Distance
- Subsequence-related DP

## Anki Recall Prompts

- What is the DP state for longest common subsequence?
- How does the recurrence handle matching vs. non-matching chars?
- When can LCS be space-optimized to one row?
