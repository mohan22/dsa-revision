# Problem: Two Sum

## Source
- Platform: LeetCode
- Topic: Arrays / Hashing
- Tags: Arrays, Hashing
- Difficulty: Easy
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given an array and a target, return the two indices whose values add up to the target.

## Recognition Pattern

- We need complement lookup while scanning once.
- Original indices matter, so sorting is awkward.
- HashMap is the natural `value -> index` memory.

## Brute Force Thought

Try every pair and check whether it sums to target.

Why it is too slow:
- `O(n^2)` comparisons
- unnecessary repeated pair checks

## Core Insight

For each number `x`, the only value we care about is `target - x`.
If we already saw that complement earlier, we have the answer immediately.

## Solution Approach

1. Maintain a map from number to its index.
2. Iterate through the array once.
3. For the current number, compute `complement = target - nums[i]`.
4. If the complement is already in the map, return its index and `i`.
5. Otherwise insert the current number into the map and continue.

## Thought Process During Solving

1. Do I need original indices? Yes, so avoid sorting.
2. What repeated work exists in brute force? Re-checking complements.
3. What should I remember from the prefix? Seen values and their indices.
4. Should I insert before checking? No, check first to avoid using the same element twice.
5. If duplicates exist, does this still work? Yes, because earlier index is stored before the later duplicate is processed.

## Java Solution
```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> indexByValue = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (indexByValue.containsKey(complement)) {
                return new int[] {indexByValue.get(complement), i};
            }
            indexByValue.put(nums[i], i);
        }

        return new int[0];
    }
}
```

## Complexity
- Time: `O(n)`
- Space: `O(n)`

## Edge Cases / Traps

- Duplicate values
- Negative numbers
- Do not insert before checking complement
- Only valid because the problem guarantees exactly one answer

## Why This Works

At index `i`, either the needed complement has already appeared or it has not. The map stores exactly the earlier values that can pair with the current one. Since every element is processed once, the first time we find the complement we have a valid pair with correct indices.

## Interview Explanation

The brute force is checking all pairs in `O(n^2)`. We can do better by remembering values we have already seen in a `HashMap`. At each index, I compute the needed complement and ask whether it already exists in the map. If yes, I return the stored index plus the current one. Otherwise I store the current value and continue.

## Similar Problems

- Two Sum II
- Subarray Sum Equals K
- Top K Frequent Elements

## Anki Recall Prompts

- What do I store in the map for Two Sum?
- Why must I check complement before inserting current value?
- Why is sorting not the first choice here?
