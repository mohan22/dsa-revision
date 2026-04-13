# Problem: Sliding window for Fruit Into Baskets (2 types)

## Source
- Platform: Anki deck seed
- Topic: Two Pointers / Sliding Window
- Tags: SlidingWindow, KDistinct, Day23
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Statement

Given an array of fruit types represented by integers, the objective is to find the length of the longest contiguous subarray that contains at most two distinct fruit types. This is the classic "Fruit Into Baskets" sliding-window problem where the window represents two baskets and each basket can hold only one type of fruit.

The input typically includes:
- `fruits[]`: array of integers representing fruit types.

The algorithm should:
- expand a sliding window across the array,
- track the count of each fruit type in the window,
- shrink the window when more than two distinct types are present,
- maintain the maximum window size seen while the window contains at most two distinct types.

This pattern is essential for problems that ask for the longest subarray with at most `k` distinct values, and for understanding how to manage dynamic window constraints efficiently.

## Recognition Pattern

- Topic signal: Two Pointers / Sliding Window
- Pattern hint from tags: SlidingWindow / KDistinct
- Key signal: maximum-length subarray with at most 2 distinct values
- Tier 1 note: know the invariant that the window always contains at most two distinct fruit types and is adjusted by counts

## Brute Force Thought

A brute-force approach considers each starting index and computes the maximum valid window from there, resulting in O(n^2) time. The optimized sliding-window solution avoids repeated scanning by maintaining counts and moving both ends of the window in a single pass.

## Core Insight

Use a frequency map for fruit types in the current window. Expand the window by adding the next fruit, and if the number of distinct types exceeds two, shrink the window from the left until only two distinct types remain. Track the maximum valid window length throughout.

## Solution Approach

1. Initialize `left = 0`, `maxLen = 0`, and a map for fruit counts.
2. Iterate `right` from `0` to `n - 1`.
3. Add `fruits[right]` to the map and increment its count.
4. While map size exceeds 2, decrement the count of `fruits[left]`, remove it from the map if count becomes 0, and increment `left`.
5. Update `maxLen = max(maxLen, right - left + 1)`.
6. Return `maxLen` after processing all fruits.

## Thought Process During Solving

1. Why is the fixed number of baskets the key constraint?
2. How does the window shrink when too many fruit types are present?
3. Why is it safe to move the left pointer only when the map size exceeds 2?
4. What invariant allows the algorithm to compute the maximum length in one pass?

## Java Skeleton
```java
class Solution {
    public int totalFruit(int[] fruits) {
        if (fruits == null || fruits.length == 0) {
            return 0;
        }

        Map<Integer, Integer> count = new HashMap<>();
        int left = 0;
        int maxLen = 0;

        for (int right = 0; right < fruits.length; right++) {
            count.put(fruits[right], count.getOrDefault(fruits[right], 0) + 1);

            while (count.size() > 2) {
                int leftFruit = fruits[left];
                count.put(leftFruit, count.get(leftFruit) - 1);
                if (count.get(leftFruit) == 0) {
                    count.remove(leftFruit);
                }
                left++;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }
}
```

## Complexity
- Time: O(n)
- Space: O(1) amortized, because the map stores at most 2 keys

## Edge Cases / Traps

- Empty input should return 0.
- All fruits are the same type and the window extends across the full array.
- The answer may be 1 when every fruit is distinct.
- If `fruits.length` is less than 2, the result is the array length.

## Why This Works

Because the baskets can hold only two types, the window is always maintained so it contains at most two distinct fruits. Expanding the window adds new fruits, and shrinking it removes the leftmost fruits until the constraint is restored. This guarantees the longest valid window is found.

## Interview Explanation

Use a sliding window with a count map for fruit types. Expand the right end, and whenever more than two types are present, move the left end until only two remain; the largest window observed is the answer.

## Similar Problems

- Longest substring with at most two distinct characters
- Longest subarray with at most K distinct integers
- Smallest subarray with all distinct types

## Anki Recall Prompts

- Why can the fruit count map have at most two keys?
- When should the left pointer move in this sliding-window solution?
- How do you track the maximum valid window length?