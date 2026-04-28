# Problem: Count Subarrays With Sum = K In Binary Array

## Source
- Platform: LeetCode (variation)
- Topic: Arrays / Prefix Sum
- Tags: PrefixSum, Arrays, HashMap
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given a binary array nums and an integer k, return the number of subarrays with sum equal to k.

## Recognition Pattern

- Binary array means elements are only 0 or 1.
- Need to count subarrays where sum of elements equals k.
- Prefix sum technique with hashmap for O(n) time.
- Since binary, sum represents count of 1s in subarray.

## Brute Force Thought

Check every possible subarray and sum its elements.

Why it is too slow:
- O(n^2) subarrays, each sum takes O(n) time.
- Total O(n^3) time complexity.

## Core Insight

Use prefix sums: prefix[i] = sum of first i elements. Then subarray sum from j+1 to i is prefix[i] - prefix[j]. Count occurrences where prefix[i] - prefix[j] = k, i.e., prefix[j] = prefix[i] - k. Use hashmap to store prefix sum frequencies.

## Solution Approach

1. Initialize prefix sum = 0, count = 0, hashmap with {0: 1} (for subarrays starting from index 0).
2. Iterate through array, update prefix sum.
3. For each prefix sum, check if (prefix - k) exists in map, add its count to result.
4. Increment the frequency of current prefix sum in map.

## Thought Process During Solving

1. What makes the brute-force version slow? Checking all subarrays explicitly.
2. Which data structure fixes that? HashMap for prefix sum lookups.
3. What edge case breaks it? k=0 (all subarrays with sum 0), empty array, all 1s.
4. Can I explain in 3-4 sentences? Use prefix sums to compute subarray sums in O(1). Store prefix sum frequencies in a map. For each position, count how many previous prefixes give sum = k when subtracted.

## Java Solution
```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int countSubarrays(int[] nums, int k) {
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1); // For subarrays starting at index 0
        
        int prefixSum = 0;
        int count = 0;
        
        for (int num : nums) {
            prefixSum += num;
            int target = prefixSum - k;
            if (prefixCount.containsKey(target)) {
                count += prefixCount.get(target);
            }
            prefixCount.put(prefixSum, prefixCount.getOrDefault(prefixSum, 0) + 1);
        }
        
        return count;
    }
}
```

## Complexity
- Time: `O(n)`
- Space: `O(n)` for hashmap

## Edge Cases / Traps

- k = 0: Count subarrays with sum 0 (more 0s than 1s in subarray)
- k > n: Impossible, return 0
- All 0s: If k=0, all subarrays valid
- All 1s: Count subarrays with exactly k 1s
- Single element: If nums[0] == k, count 1

## Why This Works

Prefix sum tracks cumulative sum up to each index. The difference prefix[i] - prefix[j] gives sum from j+1 to i. We count how many j have prefix[j] = prefix[i] - k. HashMap stores frequency of each prefix sum seen so far.

## Interview Explanation

Since it's a binary array, we can use prefix sums where each 1 adds 1, 0 adds 0. We maintain a map of prefix sum frequencies. For each position, we look for how many previous prefixes equal current prefix minus k, adding those counts to our result. This gives us all subarrays ending at current position with sum k.

## Similar Problems

- Subarray Sum Equals K
- Number of Subarrays with Bounded Maximum
- Subarray Product Less Than K

## Anki Recall Prompts

- How does prefix sum work for subarray sums?
- Why initialize map with {0: 1}?
- What does the map store in this problem?
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
