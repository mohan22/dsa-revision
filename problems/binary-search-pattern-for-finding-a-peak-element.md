# Problem: Binary Search Pattern For Finding A Peak Element

## Source
- Platform: LeetCode / Interview
- Topic: Binary Search
- Tags: BinarySearch, Peak, Day25
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Statement

Given an integer array `nums`, find a peak element and return its index. A peak element is an element that is strictly greater than its neighbors. The array may contain multiple peaks, and you can return any one. The array is guaranteed to have a peak (e.g., the first or last element is considered a peak if it satisfies the condition).

The input typically includes:
- `nums[]`: an array of integers

The algorithm should:
- use binary search to find a peak in O(log n) time,
- compare the middle element with its right neighbor,
- move towards the side with the potential peak,
- return the index of any peak found.

This pattern is essential for binary-search problems on unsorted arrays where a local maximum is sought.

## Recognition Pattern

- Topic signal: Binary Search
- Pattern hint from tags: BinarySearch / Peak
- Key signal: find any peak element in an array using log-time search
- Tier 1 note: know the decision rule based on `nums[mid]` vs `nums[mid+1]`

## Brute Force Thought

A brute-force scan checks each element in O(n) time. The optimized binary search reduces this to O(log n) by exploiting the peak property.

## Core Insight

If `nums[mid] < nums[mid+1]`, a peak must exist to the right of `mid`; otherwise, a peak is at `mid` or to the left. This guarantees finding a peak in log steps.

## Solution Approach

1. Initialize `left = 0`, `right = nums.length - 1`.
2. While `left < right`:
   - compute `mid = left + (right - left) / 2`.
   - if `nums[mid] < nums[mid + 1]`, set `left = mid + 1`.
   - else set `right = mid`.
3. Return `left` (or `right`, as they converge).

## Thought Process During Solving

1. Why does comparing `nums[mid]` with `nums[mid+1]` identify the peak side?
2. How does this work even if the array is not sorted?
3. What ensures a peak exists?
4. Why is the time complexity O(log n)?

## Java Skeleton
```java
class Solution {
    public int findPeakElement(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array must be non-empty");
        }

        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < nums[mid + 1]) {
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

- Single-element array returns index 0.
- Two elements: return the larger one.
- Array with all increasing elements: last element is peak.
- Array with all decreasing elements: first element is peak.

## Why This Works

The algorithm always moves towards a peak by choosing the side where the value is increasing or staying high. Since a peak is guaranteed, it converges to one.

## Interview Explanation

Use binary search: if the middle element is less than the next, search right; otherwise, search left. This finds a peak in log time.

## Similar Problems

- Find Peak Element II (2D array)
- Maximum Element in Bitonic Array
- Local Maximum in Array

## Anki Recall Prompts

- Why compare `nums[mid]` with `nums[mid+1]`?
- What guarantees a peak exists?
- How does the search converge?

