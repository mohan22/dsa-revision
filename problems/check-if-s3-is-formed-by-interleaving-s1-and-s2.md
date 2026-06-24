# Problem: Check If S3 Is Formed By Interleaving S1 And S2

## Source
- Platform: Anki deck seed
- Topic: Dynamic Programming
- Tags: DynamicProgramming, Strings, Day7
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given strings `s1`, `s2`, and `s3`, determine whether `s3` can be formed by interleaving `s1` and `s2` while preserving the relative order of characters from each source string.

## Recognition Pattern

- Two-sequence interleaving: each position in `s3` depends on how far we have consumed `s1` and `s2`.
- DP state is defined over prefix lengths of `s1` and `s2`.
- Recurrence chooses the next char from `s1` or `s2` if it matches the next char from `s3`.

## Brute Force Thought

Try all ways to merge `s1` and `s2` into `s3`: choose whether each next `s3` char comes from `s1` or `s2`. This branches exponentially and is too slow for large strings.

## Core Insight

Define `dp[i][j]` as `true` when `s3[0..i+j-1]` can be formed by interleaving the first `i` chars of `s1` and the first `j` chars of `s2`.

Transition:
- `dp[i][j]` is true if either:
  - `dp[i-1][j]` is true and `s1.charAt(i-1) == s3.charAt(i+j-1)`
  - `dp[i][j-1]` is true and `s2.charAt(j-1) == s3.charAt(i+j-1)`

Base cases:
- `dp[0][0] = true`
- `dp[i][0] = dp[i-1][0] && s1.charAt(i-1) == s3.charAt(i-1)`
- `dp[0][j] = dp[0][j-1] && s2.charAt(j-1) == s3.charAt(j-1)`

## Solution Approach

- Recursive approach / recurrence reasoning:
  - `isInterleave(i, j)` means `s3[0..i+j-1]` can be formed from `s1[0..i-1]` and `s2[0..j-1]`.
  - Recurse by consuming the next char from `s1` or `s2` when it matches the next `s3` char.
- DP solution (memoization or bottom-up tabulation):
  - Use a 2D table or a 1D rolling row to record which prefix pairs are feasible.
- Space-optimized DP (when applicable):
  - Reduce memory from `O(n*m)` to `O(m)` by storing only the current row and updating left-to-right.

## Thought Process During Solving

1. Validate that `s1.length() + s2.length() == s3.length()`.
2. Recognize that the problem is about prefix interleaving, not substring matching.
3. Use DP to record whether each combination of prefix lengths can reach the corresponding prefix of `s3`.
4. Optimize space only after the logic is correct: the current row depends only on the prior row and the immediate left cell.

## Java Solution

```java
class Solution {
    public boolean isInterleave(String s1, String s2, String s3) {
        if (s1.length() + s2.length() != s3.length()) {
            return false;
        }

        int n = s1.length();
        int m = s2.length();
        boolean[] dp = new boolean[m + 1];

        dp[0] = true;
        for (int j = 1; j <= m; j++) {
            dp[j] = dp[j - 1] && s2.charAt(j - 1) == s3.charAt(j - 1);
        }

        for (int i = 1; i <= n; i++) {
            dp[0] = dp[0] && s1.charAt(i - 1) == s3.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                boolean fromS1 = dp[j] && s1.charAt(i - 1) == s3.charAt(i + j - 1);
                boolean fromS2 = dp[j - 1] && s2.charAt(j - 1) == s3.charAt(i + j - 1);
                dp[j] = fromS1 || fromS2;
            }
        }

        return dp[m];
    }
}
```

## Complexity
- Time: O(n * m), where `n = s1.length()` and `m = s2.length()`.
- Space: O(m) with the optimized 1D DP, or O(n*m) with a full 2D table.

## Edge Cases / Traps

- Length mismatch between `s3` and `s1 + s2`.
- Empty `s1` or `s2` reduces to a direct equality check with `s3`.
- Repeated characters in both strings mean there may be multiple valid interleavings; DP handles this by exploring both sources without duplication.
- When optimizing space, update the row left-to-right so the left neighbor is still from the current row while the top neighbor is from the previous row.

## Why This Works

Each `dp[i][j]` records whether a specific prefix pairing is feasible. The recurrence follows the natural choice between consuming the next char from `s1` or `s2`, and the table reuses prefix results to avoid exponential branching.

## Interview Explanation

"Model the state as how many chars we have taken from each string, then check if the next target char can come from `s1` or `s2`. With DP, we avoid repeating prefix checks and solve the interleaving decision in O(n*m)."

## Similar Problems

- Unique Paths
- Subsequence DP with two sources
- Classic 2D DP on prefix pairs

## Anki Recall Prompts

- What is the DP state for the interleaving string problem?
- How do you initialize the first row and column for `s1` and `s2`?
- When can the 2D interleaving DP be reduced to one row?
