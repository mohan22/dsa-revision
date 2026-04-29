# Problem: Find The Longest Subarray Whose Sum Equals K

## Source
- Platform: LeetCode
- Topic: Arrays / Prefix Sum
- Tags: PrefixSum, HashMap, Arrays
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given an integer array nums and an integer k, find the length of the longest continuous subarray whose sum equals k.

## Recognition Pattern

- We are looking for the longest subarray, not just whether one exists.
- Use prefix sums and a hashmap to remember the earliest index for each prefix sum.
- When `currentSum - k` was seen before, a candidate subarray ending at the current index is found.
- Keep the first index for each prefix sum to maximize length.

## Brute Force Thought

Enumerate all subarrays and compute their sums, tracking the maximum length when sum equals k.

Why it is too slow:
- O(n^2) subarrays to consider.
- Summing each subarray directly makes the approach O(n^3), or O(n^2) even with precomputed prefix sums.

## Core Insight

Use a running prefix sum and store the earliest index at which each prefix occurred. For each position, if `(sum - k)` has been seen, the subarray from its earliest occurrence + 1 to the current index has sum k. Since earlier occurrences maximize length, do not overwrite existing entries.

## Solution Approach

1. Initialize `sum = 0`, `maxLen = 0`, and a map with `0 -> -1`.
2. Iterate through `nums`, adding each value to `sum`.
3. If `sum` is not already in the map, record the current index.
4. If `(sum - k)` exists in the map, compute candidate length and update `maxLen`.

## Java Solution
```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int longestSubarray(int[] nums, int k) {
        Map<Integer, Integer> firstIndexBySum = new HashMap<>();
        firstIndexBySum.put(0, -1);

        int sum = 0;
        int maxLen = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];

            firstIndexBySum.putIfAbsent(sum, i);

            int target = sum - k;
            if (firstIndexBySum.containsKey(target)) {
                int candidateLen = i - firstIndexBySum.get(target);
                if (candidateLen > maxLen) {
                    maxLen = candidateLen;
                }
            }
        }

        return maxLen;
    }
}
```

## Complexity
- Time: `O(n)`
- Space: `O(n)`

## Edge Cases / Traps

- `k = 0`: longest subarray of zeros or balanced sums.
- Negative numbers: still works since prefix sums can decrease.
- All positive numbers and no valid subarray: return `0`.
- Multiple same prefix sum values: keep only the earliest index to maximize subarray length.

## Why This Works

The prefix sum at index `i` equals the sum of `nums[0..i]`. If a previous prefix sum equals `sum - k`, the subarray after that previous index through `i` sums to k. Keeping the earliest index for each prefix ensures the longest possible subarray is measured.

## Interview Explanation

Maintain a running sum and a map of the first index where each sum occurred. For each element, check if `sum - k` has occurred before. If it has, the distance from that earliest occurrence to the current index is a valid subarray length. Update the maximum length accordingly.

## Similar Problems

- Subarray Sum Equals K
- Count Subarrays Whose Sum Equals K
- Longest Subarray with Sum at Most K

## Anki Recall Prompts

- Why do we store the first index of each prefix sum?
- What does `sum - k` represent in this solution?
- How does this method avoid checking every subarray?
