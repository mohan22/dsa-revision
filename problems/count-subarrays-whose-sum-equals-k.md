# Problem: Count Subarrays Whose Sum Equals K

## Source
- Platform: LeetCode
- Topic: Arrays / Prefix Sum
- Tags: PrefixSum, HashMap, Arrays
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given an integer array nums and an integer k, count the number of continuous subarrays whose sum equals k.

## Recognition Pattern

- We need the number of subarrays with sum exactly k.
- Use a prefix sum and frequency map to avoid checking all subarrays.
- Suitable for arrays with positive, negative, or zero values.
- HashMap stores previous prefix sums seen so far.

## Brute Force Thought

Enumerate all subarrays and compute their sums.

Why it is too slow:
- There are O(n^2) subarrays.
- Summing each subarray from scratch can lead to O(n^3) total work.
- Even with prefix sum lookup, checking every pair is O(n^2).

## Core Insight

Compute the cumulative prefix sum while iterating. For each current prefix sum `sum`, any previous prefix sum equal to `sum - k` defines a subarray ending at the current index with sum k. Track frequencies of prefix sums in a HashMap.

## Solution Approach

1. Initialize `sum = 0`, `count = 0`, and `prefixCount` map with `{0: 1}`.
2. Iterate through `nums`, adding each value to `sum`.
3. Check how many previous prefix sums equal `sum - k`; add their count to `count`.
4. Update the map frequency for the current prefix sum.

## Java Solution
```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1);

        int sum = 0;
        int count = 0;

        for (int num : nums) {
            sum += num;
            int needed = sum - k;
            count += prefixCount.getOrDefault(needed, 0);
            prefixCount.put(sum, prefixCount.getOrDefault(sum, 0) + 1);
        }

        return count;
    }
}
```

## Complexity
- Time: `O(n)`
- Space: `O(n)`

## Edge Cases / Traps

- `k = 0`: Count subarrays where the sum is zero, including sequences of zeros.
- Empty array: result should be `0`.
- Negative numbers: prefix sum differences still work.
- Multiple matching prefix sums: accumulate frequencies, not just existence.

## Why This Works

The prefix sum at index `i` is the total of `nums[0..i]`. A subarray from `j+1` to `i` sums to `k` exactly when `prefix[i] - prefix[j] == k`. The hashmap stores how many times each prefix sum has occurred, so every valid `prefix[j]` contributes to the answer instantly.

## Interview Explanation

Use a running prefix sum and a hashmap of prefix sum frequencies. For each new element, compute `sum - k` and add the number of times that value has appeared before. Then update the current prefix sum frequency. This counts all subarrays whose sum equals k in one pass.

## Similar Problems

- Subarray Sum Equals K
- Count Number of Nice Subarrays
- Number of Subarrays with Sum K

## Anki Recall Prompts

- Why do we store prefix sum frequencies?
- Why initialize the map with `{0: 1}`?
- How does `sum - k` identify valid subarrays?
