# Problem: Find The Longest Subarray With Equal 0S And 1S

## Source
- Platform: LeetCode
- Topic: Arrays / Prefix Sum
- Tags: PrefixSum, Arrays, HashMap
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given a binary array nums containing only 0s and 1s, find the length of the longest contiguous subarray that has an equal number of 0s and 1s.

## Recognition Pattern

- Binary array with balance constraint.
- Need the longest subarray where count(0) == count(1).
- Transform 0s to -1s to make it a sum problem.
- Use prefix sums and hashmap to find subarrays with sum 0.

## Brute Force Thought

Check every possible subarray and count 0s and 1s.

Why it is too slow:
- O(n^2) subarrays, each count takes O(n) time.
- Total O(n^3) complexity.

## Core Insight

Replace 0s with -1s. Now, equal 0s and 1s means sum of subarray is 0. Use prefix sums: track earliest index for each sum, and when sum repeats, the subarray between has sum 0.

## Solution Approach

1. Transform 0s to -1s in the array.
2. Use a hashmap to store the first index of each prefix sum.
3. Initialize map with {0: -1}.
4. Iterate, compute prefix sum, check if sum exists in map; if yes, update max length with current index - map.get(sum).
5. Put current sum in map if not present.

## Thought Process During Solving

1. What makes brute-force slow? Checking all subarrays explicitly.
2. Which data structure fixes it? HashMap for prefix sum lookups.
3. What edge case breaks it? All 1s or all 0s, no valid subarray.
4. Can I explain in 3-4 sentences? Transform 0 to -1, then find longest subarray with sum 0 using prefix sums and a map storing first occurrences.

## Java Solution
```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int findMaxLength(int[] nums) {
        Map<Integer, Integer> firstIndexBySum = new HashMap<>();
        firstIndexBySum.put(0, -1);

        int sum = 0;
        int maxLen = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += (nums[i] == 0 ? -1 : 1);

            if (firstIndexBySum.containsKey(sum)) {
                int candidateLen = i - firstIndexBySum.get(sum);
                if (candidateLen > maxLen) {
                    maxLen = candidateLen;
                }
            } else {
                firstIndexBySum.put(sum, i);
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

- All 0s or all 1s: maxLen = 0
- Single element: 0
- Balanced array: whole array length
- Multiple same sums: keep first index for longest subarray

## Why This Works

By mapping 0 to -1, equal counts mean sum 0. Prefix sum differences give subarray sums. When same sum repeats, the subarray between has sum 0. Using first index maximizes length.

## Interview Explanation

Convert 0s to -1s so equal 0s and 1s means sum 0. Use prefix sums and a map of first index for each sum. When a sum repeats, the subarray from first occurrence to now has sum 0, update max length.

## Similar Problems

- Contiguous Array
- Longest Subarray with Sum 0
- Subarray Sum Equals K

## Anki Recall Prompts

- Why replace 0 with -1?
- How does prefix sum find equal 0s and 1s?
- Why store first index in map?
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
