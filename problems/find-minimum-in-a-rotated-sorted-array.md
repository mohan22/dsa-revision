# Problem: Find Minimum In A Rotated Sorted Array

## Source
- Platform: LeetCode / Interview
- Topic: Binary Search
- Tags: BinarySearch, RotatedArray, Min, Day25
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Statement

Given a rotated sorted array of unique integers, find the minimum element in O(log n) time. The array was originally sorted in ascending order and then rotated at an unknown pivot.

The input typically includes:
- `nums[]`: a rotated sorted array of distinct integers

The algorithm should:
- use binary search on the rotated array,
- compare the middle element with the right boundary,
- narrow the search to the unsorted half containing the minimum,
- return the minimum when the search converges.

This pattern is essential for binary-search problems on rotated arrays and boundary-finding in sorted sequences.

## Recognition Pattern

- Topic signal: Binary Search
- Pattern hint from tags: BinarySearch / RotatedArray
- Key signal: rotated sorted array and find minimum element in log time
- Tier 1 note: know the half-selection invariant based on `nums[mid]` vs `nums[right]`

## Brute Force Thought

A brute-force scan takes O(n) time by checking every element. The optimized approach uses binary search to reduce the search space by half each step.

## Core Insight

If `nums[mid] > nums[right]`, the minimum lies to the right of `mid`; otherwise, it lies at `mid` or to the left. Repeat until `left == right`, then return `nums[left]`.

## Solution Approach

1. Initialize `left = 0`, `right = nums.length - 1`.
2. While `left < right`:
   - compute `mid = left + (right - left) / 2`.
   - if `nums[mid] > nums[right]`, set `left = mid + 1`.
   - else set `right = mid`.
3. Return `nums[left]`.

## Thought Process During Solving

1. Why does comparing `nums[mid]` with `nums[right]` identify the unsorted half?
2. How does the rotated pivot affect the binary-search decision?
3. Why does the search converge when `left == right`?
4. What goes wrong if the array contains duplicates?

## Java Skeleton
```java
class Solution {
    public int findMin(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array must be non-empty");
        }

        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return nums[left];
    }
}
```

## Complexity
- Time: O(log n)
- Space: O(1)

## Edge Cases / Traps

- Empty array should be handled explicitly.
- Single-element array returns that element.
- Array not rotated at all should still return the first element.
- Duplicate values break this exact approach; use modified binary search for duplicates.

## Why This Works

The rotated array is made of two sorted halves. Comparing `nums[mid]` with `nums[right]` tells us which half contains the minimum, allowing binary search to narrow down the candidate range.

## Interview Explanation

Use binary search on the rotated array. If the middle element is greater than the rightmost element, the minimum is to the right; otherwise it is at mid or to the left. Continue until the range shrinks to a single element.

## Similar Problems

- Find The Minimum Element In Rotated Sorted Array II
- Search In Rotated Sorted Array
- Find Peak Element

## Anki Recall Prompts

- Why compare `nums[mid]` with `nums[right]` instead of `nums[left]`?
- What is the stopping condition for the binary search?
- How does the pivot affect the search direction?
