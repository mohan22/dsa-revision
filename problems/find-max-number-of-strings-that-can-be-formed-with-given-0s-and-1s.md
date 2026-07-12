# Problem: Find Max Number Of Strings That Can Be Formed With Given 0S And 1S

## Source
- Platform: Anki deck seed
- Topic: Dynamic Programming
- Tags: DynamicProgramming, Knapsack, Day8
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given an array of binary strings (each consisting of 0s and 1s), a maximum count of 0s (`m`), and a maximum count of 1s (`n`), find the maximum number of strings from the array that can be formed such that the total number of 0s and 1s does not exceed `m` and `n` respectively.

## Recognition Pattern

- Two-dimensional bounded knapsack: each string consumes a certain number of 0s and 1s.
- State space is defined by remaining 0s and 1s: `dp[i][j]` = max strings using up to `i` zeros and `j` ones.
- Recurrence chooses to include or exclude each string.

## Brute Force Thought

Try all subsets of strings and check if each subset respects the 0 and 1 constraints. This branches exponentially on the number of strings.

## Core Insight

Define `dp[i][j]` as the maximum number of strings that can be formed using at most `i` zeros and `j` ones.

Transition for each string with `zeroes` 0s and `ones` 1s:
- If we have enough budget, we can include this string: `dp[i][j] = Math.max(dp[i][j], 1 + dp[i - zeroes][j - ones])`

Base case:
- `dp[0][0] = 0`

## Solution Approach

- Recursive approach / recurrence reasoning:
  - `maxStrings(i, j, index)` returns the max strings using at most `i` zeros and `j` ones from index onwards.
  - For each string, either take it (if budget allows) or skip it.
- DP solution (memoization or bottom-up tabulation):
  - Use a 2D DP table where rows represent 0s budget and columns represent 1s budget.
  - Iterate through each string and update the DP table backwards (to avoid reusing the same string).
- Space-optimized DP (when applicable):
  - The 2D table is already space-efficient given the constraints on `m` and `n`.

## Thought Process During Solving

1. Recognize this as a variant of the 0/1 knapsack with two constraints (0s and 1s).
2. Precompute the number of 0s and 1s in each string.
3. Use a 2D DP table indexed by (zero count, one count).
4. Iterate backward through both dimensions to ensure each string is used at most once.

## Java Solution

```java
class Solution {
    public int findMaxForm(String[] strs, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];

        for (String str : strs) {
            int zeros = 0, ones = 0;
            for (char c : str.toCharArray()) {
                if (c == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }

            // Iterate backward to avoid reusing the same string
            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = Math.max(dp[i][j], 1 + dp[i - zeros][j - ones]);
                }
            }
        }

        return dp[m][n];
    }
}
```

## Complexity
- Time: O(k * m * n + l), where `k = strs.length`, `m` and `n` are the limits on 0s and 1s, and `l` is the total length of all strings (to count 0s and 1s).
- Space: O(m * n) for the 2D DP table.

## Edge Cases / Traps

- Empty string array: the answer is 0.
- Strings with only 0s or only 1s: they can only use one dimension of the budget.
- Off-by-one in backward iteration: iterating backward is crucial to prevent using the same string twice.
- Iterating backward means `i >= zeros` and `j >= ones`, not `i > zeros` and `j > ones`.

## Why This Works

The 2D DP table tracks the best achievable count for each (zero budget, one budget) pair. By iterating through strings and updating backward, we ensure the 0/1 knapsack property: each string is used at most once. The maximum value at `dp[m][n]` is the answer.

## Interview Explanation

"This is a two-constraint knapsack problem. Each string has a weight in two dimensions: the number of 0s and the number of 1s. I use a 2D DP table where `dp[i][j]` tracks the max strings using at most `i` zeros and `j` ones. By iterating strings and updating the table backward, I solve it in O(k*m*n) time."

## Similar Problems

- 0/1 Knapsack
- Partition Equal Subset Sum
- Coin Change

## Anki Recall Prompts

- What is the DP state for the max number of strings problem?
- Why must we iterate the DP table backward?
- How do you handle the zero and one counts from each string?
