# Problem: Count Ways To Assign +/− To Reach Target Sum

## Source
- Platform: Anki deck seed
- Topic: Dynamic Programming
- Tags: DynamicProgramming, SubsetSum, Day8
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given an integer array `nums` and a target integer `target`, count how many ways to assign either `+` or `-` to each number so that the resulting sum equals `target`.

## Recognition Pattern

- This is a subset-sum counting problem disguised as sign assignment.
- The equation `sum(+) - sum(-) = target` can be rewritten as `sum(all) - 2*sum(negative) = target`.
- The problem reduces to counting subsets whose sum is `(sum(nums) + target) / 2`.

## Brute Force Thought

Try all sign assignments for each number: each element has two choices, so the search tree is exponential in `n`.

## Core Insight

Let `total = sum(nums)`. If `total + target` is odd or negative, the answer is 0. Otherwise:

- We need a subset with sum `subsetSum = (total + target) / 2`.
- Count how many subsets of `nums` can achieve that sum.

This becomes a standard 0/1 subset-sum counting DP.

## Solution Approach

- Recursive approach / recurrence reasoning:
  - `count(i, remaining)` returns the number of ways to form `remaining` using the first `i` numbers.
  - At each step, either skip the number or include it.
- DP solution (memoization or bottom-up tabulation):
  - Use a 1D DP array where `dp[x]` stores the number of ways to make sum `x`.
- Space-optimized DP (when applicable):
  - The counting DP already uses O(target) space, which is the natural optimization for this problem.

## Thought Process During Solving

1. Transform the sign-assignment formulation into a subset-sum counting formulation.
2. Check feasibility before doing DP: `target + total` must be even and non-negative.
3. Use DP over sums, updating counts for each number.
4. Keep the DP as a count array rather than a boolean array because the task is counting ways.

## Java Solution

```java
class Solution {
    public int findTargetSumWays(int[] nums, int target) {
        int total = 0;
        for (int x : nums) {
            total += x;
        }

        int sum = total + target;
        if (sum < 0 || sum % 2 != 0) {
            return 0;
        }

        int subsetSum = sum / 2;
        int[] dp = new int[subsetSum + 1];
        dp[0] = 1;

        for (int num : nums) {
            for (int s = subsetSum; s >= num; s--) {
                dp[s] += dp[s - num];
            }
        }

        return dp[subsetSum];
    }
}
```

## Complexity
- Time: O(n * subsetSum), where `subsetSum = (total + target) / 2`.
- Space: O(subsetSum).

## Edge Cases / Traps

- If `total + target` is odd, the answer is immediately 0.
- If `subsetSum < 0`, the answer is 0.
- The DP must iterate sums backward to avoid reusing the same number more than once.
- Use counting DP, not boolean DP, because the task is to count ways rather than check existence.

## Why This Works

Each sign assignment corresponds to choosing a subset of numbers to receive the negative sign. If the positive sum minus the negative sum equals the target, then the negative subset sum is exactly `(total - target) / 2`. Equivalently, the positive subset sum is `(total + target) / 2`. Counting subsets that reach this sum counts all valid sign assignments.

## Interview Explanation

"I transform the sign-assignment problem into a subset-sum counting problem. Once the numbers are split into two groups, the target condition becomes a single sum constraint. A 1D DP counts how many ways each sum can be formed using the numbers, which gives the final answer in O(n * subsetSum)."

## Similar Problems

- Partition Equal Subset Sum
- Number of Subsets With a Given Sum
- Coin Change II

## Anki Recall Prompts

- How do you transform the sign-assignment problem into subset sum?
- Why is the DP array initialized with `dp[0] = 1`?
- Why do we iterate sums backward in the counting DP?
