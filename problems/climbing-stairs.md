# Problem: Climbing Stairs

## Source
- Platform: LeetCode
- Topic: Dynamic Programming
- Tags: DP, Fibonacci
- Difficulty: Easy
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Count the number of distinct ways to reach stair `n` when each move is either 1 step or 2 steps.

## Recognition Pattern

- Counting problem
- Current answer depends only on smaller states
- Natural recurrence from the last move

## Brute Force Thought

Recursively try taking 1 step and 2 steps from every position.

Why it is too slow:
- repeated subproblems
- exponential recursion tree

## Core Insight

To land on step `n`, the last move must come from either `n - 1` or `n - 2`. So:

`ways[n] = ways[n - 1] + ways[n - 2]`

## Solution Approach

1. Use DP with the Fibonacci-style recurrence.
2. Base cases: `n = 1` gives 1 way, `n = 2` gives 2 ways.
3. Since each state depends only on the previous two, store only two variables.

## Thought Process During Solving

1. Is this optimization, feasibility, or counting? Counting.
2. What is the last decision? Take 1 step or 2 steps.
3. Does recursion repeat work? Yes, heavily.
4. Can I shrink memory? Yes, only previous two states are needed.

## Java Solution
```java
class Solution {
    public int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }

        int prevTwo = 1;
        int prevOne = 2;

        for (int stair = 3; stair <= n; stair++) {
            int current = prevOne + prevTwo;
            prevTwo = prevOne;
            prevOne = current;
        }

        return prevOne;
    }
}
```

## Complexity
- Time: `O(n)`
- Space: `O(1)`

## Edge Cases / Traps

- `n = 1`
- Incorrect base cases
- Writing the recurrence correctly but returning the wrong variable

## Why This Works

Every valid path to stair `n` ends with either a 1-step jump from `n - 1` or a 2-step jump from `n - 2`, and those two sets of paths do not overlap. So the total is the sum of the two smaller counts.

## Interview Explanation

This is a simple 1D DP. The number of ways to reach stair `n` depends on the ways to reach `n - 1` and `n - 2`, because the last move must be 1 or 2 steps. That gives a Fibonacci recurrence, and we can compute it in `O(n)` time with constant space.

## Similar Problems

- House Robber
- Min Cost Climbing Stairs
- Decode Ways

## Anki Recall Prompts

- What is the state transition?
- Why is this Fibonacci-like?
- Which previous states do I actually need to keep?
