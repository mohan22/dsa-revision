# Problem: Count ways to decode a numeric string (A=1,...,Z=26)

## Source
- Platform: Anki deck seed
- Topic: Dynamic Programming
- Tags: DynamicProgramming, Strings, Day6
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Description

**Decode Ways (LeetCode 91)**

Given a numeric string `s` containing only digits, count the number of ways to decode it into letters where `'A' = 1`, `'B' = 2`, ..., `'Z' = 26`.

Return `0` if the string cannot be decoded. Leading zeros are invalid, and `'0'` can only appear as part of a valid two-digit code `10` or `20`.

**Constraints:**
- `1 <= s.length <= 1000`
- `s` contains only digits.

**Examples:**
- Input: `s = "12"` → Output: `2` (`"AB"`, `"L"`)
- Input: `s = "226"` → Output: `3` (`"BZ"`, `"VF"`, `"BBF"`)
- Input: `s = "06"` → Output: `0` (invalid leading zero)

## Recognition Pattern

- **Signal**: Count the number of valid ways to map digits to letters.
- **DP pattern**: Each position depends on the previous one or two positions.
- **Subproblem**: DP[i] = number of ways to decode prefix `s[0..i-1]`.
- **Key trap**: Handle `'0'` only as part of `10` or `20`.

## Core Insight

**Bottom-up DP**: For each index, count ways using a valid single digit and, if valid, a two-digit number.

- If `s[i-1]` is nonzero, add `DP[i-1]`.
- If `s[i-2..i-1]` is between `10` and `26`, add `DP[i-2]`.

This handles the decoding decisions and invalid zeros gracefully.

## Solution Approach

1. **State definition**: Let `dp[i]` be the number of ways to decode the prefix `s[0..i-1]`.
2. **Base cases**: `dp[0] = 1` (empty string has one way), `dp[1] = 1` if first digit is nonzero, otherwise `0`.
3. **Transition**: For each position `i` from 2 to `n`:
   - If `s[i-1] != '0'`, then `dp[i] += dp[i-1]`.
   - If the two-digit substring `s[i-2..i-1]` is between `"10"` and `"26"`, then `dp[i] += dp[i-2]`.
4. **Answer**: `dp[n]` gives the number of decoding ways for the full string.

## Thought Process During Solving

1. **Brute-force** would try every partition of the string into 1- and 2-digit codes and fail on repeated prefixes.
2. **DP fixes repeated work** by reusing the count of valid decodings for each prefix.
3. **Critical edge case**: `'0'` cannot be decoded alone; it must pair with `1` or `2`.
4. **Interview summary**: Count decode ways using prefix-based DP; add one-digit contributions and valid two-digit contributions, and treat zeros as invalid unless part of `10` or `20`.

## Java Solution

```java
class Solution {
    public int numDecodings(String s) {
        if (s == null || s.isEmpty() || s.charAt(0) == '0') {
            return 0;
        }

        int n = s.length();
        int[] dp = new int[n + 1];
        dp[0] = 1; // empty string
        dp[1] = 1; // first char is valid because it is not '0'

        for (int i = 2; i <= n; i++) {
            char one = s.charAt(i - 1);
            char two = s.charAt(i - 2);

            // Single-digit decode is valid if current char is not '0'
            if (one != '0') {
                dp[i] += dp[i - 1];
            }

            // Two-digit decode is valid if value is between 10 and 26
            int value = (two - '0') * 10 + (one - '0');
            if (value >= 10 && value <= 26) {
                dp[i] += dp[i - 2];
            }
        }

        return dp[n];
    }
}
```

## Complexity
- **Time**: O(n), where n is the length of `s`.
- **Space**: O(n) for the DP array. This can be reduced to O(1) if only the last two values are kept.

## Edge Cases / Traps

1. **String starts with '0'**: Immediately return 0 because no valid decoding begins with zero.
2. **Isolated '0'**: `"30"`, `"40"`, etc. are invalid because only `10` and `20` are allowed.
3. **Trailing zero**: `"10"` and `"20"` are valid, but `"210"` should count the last `10` as a pair.
4. **Long valid prefix**: DP handles large strings efficiently by building prefix counts.
5. **All ones**: e.g. `"1111"` results in Fibonacci-like growth in decode ways.

## Brute-Force vs Optimized

**Brute-Force:**
```java
int count(String s, int i) {
    if (i == s.length()) return 1;
    if (s.charAt(i) == '0') return 0;

    int ways = count(s, i + 1);
    if (i + 1 < s.length()) {
        int value = Integer.parseInt(s.substring(i, i + 2));
        if (value >= 10 && value <= 26) {
            ways += count(s, i + 2);
        }
    }
    return ways;
}
```
**Problem**: Exponential time due to repeated decoding of the same suffixes.

**Optimized (DP)**: Each prefix is computed once, giving O(n) time and O(n) space.

## Test Cases

```java
assert new Solution().numDecodings("12") == 2;
assert new Solution().numDecodings("226") == 3;
assert new Solution().numDecodings("06") == 0;
assert new Solution().numDecodings("10") == 1;
assert new Solution().numDecodings("2101") == 1; // 21|01 invalid, only 2|10|1
```