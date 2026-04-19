# Problem: Why do First/Last Position require two binary searches

## Source
- Platform: LeetCode / Interview
- Topic: Binary Search
- Tags: BinarySearch, Boundaries, Day25
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Statement

Given a sorted array of integers and a target value, find the starting and ending position of the target in the array. If the target is not found, return [-1, -1]. The array may contain duplicates, and the solution must run in O(log n) time.

The input typically includes:
- `nums[]`: a sorted array of integers
- `target`: the value to search for

The algorithm should:
- perform a binary search to find the leftmost (first) occurrence,
- perform another binary search to find the rightmost (last) occurrence,
- return the indices or [-1, -1] if not found.

This pattern is essential for boundary-finding problems in sorted arrays with duplicates.

## Recognition Pattern

- Topic signal: Binary Search
- Pattern hint from tags: BinarySearch / Boundaries
- Key signal: find range of target in sorted array with duplicates
- Tier 1 note: know why two separate binary searches are needed for first and last positions

## Brute Force Thought

A brute-force linear scan finds the first and last positions in O(n) time. The optimized binary search approach uses two O(log n) searches to achieve the required time complexity.

## Core Insight

Use binary search for lower bound (first position) and upper bound (last position + 1). The lower bound finds the leftmost index where nums[i] >= target, and upper bound finds the leftmost index where nums[i] > target.

## Solution Approach

1. Implement a helper function for lower bound: while left < right, mid = (left + right) / 2, if nums[mid] < target, left = mid + 1, else right = mid.
2. First position = lower bound of target.
3. Last position = upper bound of target - 1.
4. If first > last or nums[first] != target, return [-1, -1].
5. Else return [first, last].

## Thought Process During Solving

1. Why can't one binary search find both positions?
2. How does the lower bound differ from standard binary search?
3. What happens if the target appears multiple times?
4. Why is the time complexity O(log n) for each search?

## Java Skeleton
```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return new int[]{-1, -1};
        }

        int first = findLowerBound(nums, target);
        int last = findUpperBound(nums, target) - 1;

        if (first <= last && nums[first] == target && nums[last] == target) {
            return new int[]{first, last};
        } else {
            return new int[]{-1, -1};
        }
    }

    private int findLowerBound(int[] nums, int target) {
        int left = 0;
        int right = nums.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    private int findUpperBound(int[] nums, int target) {
        int left = 0;
        int right = nums.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
```

## Complexity
- Time: O(log n)
- Space: O(1)

## Edge Cases / Traps

- Empty array.
- Target not in array.
- Target at the beginning or end.
- All elements are the target.
- Array with one element.

## Why This Works

Two binary searches isolate the range: lower bound finds the start, upper bound finds the end + 1. This handles duplicates correctly while maintaining log time.

## Interview Explanation

Perform two binary searches: one for the first occurrence (lower bound) and one for the last (upper bound - 1). This ensures O(log n) time even with duplicates.

## Similar Problems

- Find First and Last Position of Element in Sorted Array
- Search Insert Position
- Count occurrences of a number in sorted array

## Anki Recall Prompts

- What is the difference between lower and upper bound?
- Why can't we use one search for both positions?
- How do we handle the case where target is not found?

