# Problem: Two-Pointers Pattern For Sorted Arrays

## Source
- Platform: Anki deck seed
- Topic: Two Pointers / Sliding Window
- Tags: TwoPointers, Sorted, Day23
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Statement

Given a sorted array and a target value, the objective is to find pairs or triples that satisfy a condition, such as summing to the target or finding the closest sum. The two-pointers pattern uses one pointer at the start and one at the end, moving them based on the current sum compared to the target.

The input typically includes:
- `nums[]`: a sorted array of integers
- `target`: the target sum or value

The algorithm should:
- initialize two pointers, left at 0 and right at n-1,
- while left < right, compute the current sum,
- if sum < target, increment left; if sum > target, decrement right,
- if sum == target, record the pair and adjust pointers,
- handle duplicates or multiple solutions as needed.

This pattern is essential for problems involving sorted arrays where you need to find combinations without nested loops.

## Recognition Pattern

- Topic signal: Two Pointers / Sliding Window
- Pattern hint from tags: TwoPointers / Sorted
- Key signal: sorted array, find pairs/triples with sum condition
- Tier 1 note: know the invariant that pointers move based on sum comparison, and how to handle duplicates

## Brute Force Thought

A brute-force approach uses nested loops to check all pairs, resulting in O(n^2) time. The optimized two-pointers solution avoids this by leveraging the sorted order, reducing to O(n).

## Core Insight

With the array sorted, start with left=0 and right=n-1. Compute sum = nums[left] + nums[right]. If sum < target, increment left; if sum > target, decrement right; if equal, record and adjust both pointers.

## Solution Approach

1. Sort the array if not already sorted.
2. Initialize left = 0, right = n - 1.
3. While left < right:
   - Compute current sum.
   - If sum == target, add to result, and move both pointers (skip duplicates if needed).
   - If sum < target, increment left.
   - If sum > target, decrement right.
4. Return the result list.

## Thought Process During Solving

1. Why does sorting enable the two-pointer approach?
2. How do you avoid duplicates in the result?
3. What happens if the array has negative numbers?
4. Why is this better than hashing for sorted arrays?

## Java Skeleton
```java
class Solution {
    public List<List<Integer>> twoSumSorted(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == target) {
                result.add(Arrays.asList(nums[left], nums[right]));
                left++;
                right--;
                // Skip duplicates
                while (left < right && nums[left] == nums[left - 1]) left++;
                while (left < right && nums[right] == nums[right + 1]) right--;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }

        return result;
    }
}
```

## Complexity
- Time: O(n log n) for sorting + O(n) for two pointers
- Space: O(1) extra space, O(n) for result

## Edge Cases / Traps

- Empty array or single element.
- All elements are the same.
- Target is not achievable.
- Negative numbers in the array.

## Why This Works

The sorted order allows the pointers to converge efficiently. Moving left increases the sum, moving right decreases it, guaranteeing the optimal path to the target.

## Interview Explanation

Sort the array, then use two pointers from start and end. Move them based on whether the current sum is less or greater than the target.

## Similar Problems

- 3Sum and 4Sum
- Two sum II (input array is sorted)
- Closest sum to target

## Anki Recall Prompts

- When should you increment left vs decrement right?
- How do you handle duplicates in the result?
- Why is sorting necessary for this pattern?

