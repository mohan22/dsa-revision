# Problem: Check If There Exists Subarray Of Size ≥ 2 With Sum Multiple Of K

## Source
- Platform: LeetCode
- Topic: Arrays / Prefix Sum
- Tags: PrefixSum, Modulo, HashMap
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given an integer array nums and an integer k, check if there exists a contiguous subarray of size at least 2 whose sum is a multiple of k.

## Recognition Pattern

- Need to find if any subarray sum % k == 0 and length >=2.
- Use prefix sums modulo k.
- HashMap to store first index of each remainder.
- If same remainder appears at indices i and j with j - i >=2, then subarray from i+1 to j has sum multiple of k.

## Brute Force Thought

Check every subarray of length >=2 and compute sum % k.

Why it is too slow:
- O(n^2) subarrays, each sum takes O(n) time.
- Total O(n^3) complexity.

## Core Insight

Compute prefix sums modulo k. Use a map to store the first index where each remainder occurred. If the same remainder appears again at a later index with difference >=2, then there's a valid subarray.

## Solution Approach

1. If k == 0, check for any subarray sum ==0 with length >=2 (e.g., two zeros).
2. Else, compute prefix sums modulo k.
3. Use a map: remainder -> first index.
4. For each index, if remainder seen before and current index - first index >=2, return true.
5. Also, if remainder 0 appears at index >=1, subarray from 0 to that index has sum multiple of k and length >=2.

## Thought Process During Solving

1. What makes brute-force slow? Checking all subarrays.
2. Which data structure fixes it? HashMap for remainder lookups.
3. What edge case breaks it? k=0, negative numbers, k negative.
4. Can I explain in 3-4 sentences? Use prefix sums modulo k. Store first index of each remainder. If same remainder at indices differing by >=2, valid subarray exists.

## Java Solution
```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public boolean checkSubarraySum(int[] nums, int k) {
        if (k == 0) {
            for (int i = 0; i < nums.length - 1; i++) {
                if (nums[i] == 0 && nums[i + 1] == 0) {
                    return true;
                }
            }
            return false;
        }

        Map<Integer, Integer> remainderToIndex = new HashMap<>();
        remainderToIndex.put(0, -1);

        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            int remainder = sum % k;
            if (remainder < 0) remainder += k; // Handle negative remainders

            if (remainderToIndex.containsKey(remainder)) {
                if (i - remainderToIndex.get(remainder) >= 2) {
                    return true;
                }
            } else {
                remainderToIndex.put(remainder, i);
            }
        }

        return false;
    }
}
```

## Complexity
- Time: `O(n)`
- Space: `O(min(n, k))`

## Edge Cases / Traps

- k = 0: Check for two consecutive zeros.
- k negative: Take absolute value or handle negative remainders.
- All elements multiple of k: Whole array if length >=2.
- Single element: Never valid.
- Empty array: False.

## Why This Works

Prefix sum modulo k tracks cumulative sum remainders. Same remainder means subarray sum is multiple of k. Ensuring index difference >=2 guarantees length >=2.

## Interview Explanation

Compute running sum modulo k. Use a map to record the first index of each remainder. If you encounter the same remainder again and the indices are at least 2 apart, there's a subarray of length at least 2 with sum multiple of k.

## Similar Problems

- Continuous Subarray Sum
- Subarray Sum Equals K
- Check Subarray Sum

## Anki Recall Prompts

- How does modulo help with sum multiples?
- Why check index difference >=2?
- What to do when k=0?
```

## Complexity
- Time: Derive during promotion
- Space: Derive during promotion

## Edge Cases / Traps

- Check boundary conditions, duplicates, and empty input.
- Verify the invariant or state after each update.
- Confirm whether recursion, heap ordering, or index movement can fail.

## Promotion Checklist

- Add a full Java solution.
- Add exact time and space complexity.
- Add one short brute-force vs optimized comparison.
- Add 2-3 problem-specific traps.
