# Problem: Check If S3 Is Formed By Interleaving S1 And S2

## Source
- Platform: Anki deck seed
- Topic: Dynamic Programming
- Tags: DynamicProgramming, Strings, Day7
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Description

**Interleaving String (LeetCode 97)**

Given three strings `s1`, `s2`, and `s3`, determine whether `s3` is formed by an interleaving of `s1` and `s2`.

An interleaving of two strings preserves the left-to-right order of characters from each string, but they may be interspersed. Return `true` if `s3` can be formed in this way, otherwise return `false`.

**Constraints:**
- `0 <= s1.length, s2.length <= 1000`
- `s3.length == s1.length + s2.length`
- `s1`, `s2`, `s3` contain only lowercase/uppercase letters and digits in this repo's examples.

**Examples:**
- `s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"` → `true`
- `s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"` → `false`

## Recognition Pattern

- **Signal**: Two-sequence interleaving — each position depends on prefixes of `s1` and `s2`.
- **DP pattern**: 2D DP over lengths of `s1` and `s2`: `dp[i][j]` answers whether prefixes `s1[0..i-1]` and `s2[0..j-1]` interleave to form `s3[0..i+j-1]`.
- **Subproblem**: `dp[i][j] = (dp[i-1][j] && s1[i-1]==s3[i+j-1]) || (dp[i][j-1] && s2[j-1]==s3[i+j-1])`.

## Core Insight

Build a DP table where `dp[i][j]` is `true` if `s3[0..i+j-1]` can be formed by interleaving first `i` chars of `s1` and first `j` chars of `s2`.

Transitions:

- If `s1[i-1] == s3[i+j-1]`, `dp[i][j] |= dp[i-1][j]`.
- If `s2[j-1] == s3[i+j-1]`, `dp[i][j] |= dp[i][j-1]`.

Base: `dp[0][0] = true`. Fill first row/column by matching prefixes.

## Solution Approach

1. **State definition**: `dp[i][j]` as defined above.
2. **Initialization**: `dp[0][0] = true`. For `i>0`: `dp[i][0] = dp[i-1][0] && s1[i-1]==s3[i-1]`. For `j>0`: `dp[0][j] = dp[0][j-1] && s2[j-1]==s3[j-1]`.
3. **Fill order**: iterate `i` from `0..len(s1)` and `j` from `0..len(s2)`.
4. **Answer**: `dp[len(s1)][len(s2)]`.

We can optimize space to O(min(len(s1), len(s2))) by rolling a single row.

## Thought Process During Solving

1. **Brute-force** would try all merges of two strings — exponential in lengths.
2. **DP** counts feasible prefix pairings and reuses results for larger prefixes.
3. **Key traps**: mismatched total length, handling of empty prefixes, and early mismatches in prefix fill.
4. **Interview pitch**: Use a 2D DP table where each cell means "can these prefixes interleave to form this prefix of s3?" and update from left/top based on character matches.

## Java Solution

```java
class Solution {
    public boolean isInterleave(String s1, String s2, String s3) {
        if (s1.length() + s2.length() != s3.length()) return false;

        int n = s1.length();
        int m = s2.length();
        boolean[] dp = new boolean[m + 1];

        dp[0] = true;
        // initialize first row (i = 0)
        for (int j = 1; j <= m; j++) {
            dp[j] = dp[j - 1] && s2.charAt(j - 1) == s3.charAt(j - 1);
        }

        for (int i = 1; i <= n; i++) {
            // update dp[0] for current i (j = 0)
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
- **Time**: O(n * m), where n = `s1.length()`, m = `s2.length()`.
- **Space**: O(m) with the optimized 1D DP (or O(n*m) for full 2D table).

## Edge Cases / Traps

1. **Length mismatch**: Immediately return false if `s3.length() != s1.length() + s2.length()`.
2. **Empty strings**: Interleaving with empty string reduces to direct equality check.
3. **Early mismatch**: If prefix characters don't match expected s3 positions, corresponding dp cells remain false and propagate.
4. **Characters repeated in both strings**: DP handles it; ensure correct order of updates when optimizing space.

## Brute-Force vs Optimized

**Brute-Force:** Try all ways to merge `s1` and `s2` (choose positions for characters of `s1`) — exponential combinations.

**Optimized (DP):** Each prefix pair (`i`,`j`) is considered once; DP gives O(n*m) time.

## Test Cases

```java
// True case
assert new Solution().isInterleave("aabcc", "dbbca", "aadbbcbcac") == true;

// False case
assert new Solution().isInterleave("aabcc", "dbbca", "aadbbbaccc") == false;

// Empty components
assert new Solution().isInterleave("", "", "") == true;
assert new Solution().isInterleave("abc", "", "abc") == true;

// Different total length
assert new Solution().isInterleave("a", "b", "abx") == false;
```
