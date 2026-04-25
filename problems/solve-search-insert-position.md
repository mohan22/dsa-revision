# Problem: Solve Search Insert Position

## Source
- Platform: Anki deck seed
- Topic: Binary Search
- Tags: BinarySearch, Insert, Day25
- Difficulty: Easy
- Revision Status: Complete
- Tier: Tier 1

## Problem Cue

How to solve Search Insert Position efficiently using binary search?

## Detailed Problem Statement

Given a sorted array of distinct integers `nums` and a target value `target`, return the index if the target is found.
If not, return the index where it would be inserted in order.

You must write an algorithm with O(log n) runtime complexity.

**Example 1:**
- Input: `nums = [1,3,5,6], target = 5`
- Output: `2`

**Example 2:**
- Input: `nums = [1,3,5,6], target = 2`
- Output: `1`

**Example 3:**
- Input: `nums = [1,3,5,6], target = 7`
- Output: `4`

**Constraints:**
- `1 <= nums.length <= 10^4`
- `-10^4 <= nums[i], target <= 10^4`
- All elements in `nums` are distinct
- `nums` is sorted in ascending order

## Recognition Pattern

- Topic signal: Binary Search
- Key phrase: "search insert position"
- The array is sorted and we need a position, not just existence
- This is a classic lower-bound / first-not-less-than pattern

## Core Insight

We need the smallest index such that `nums[index] >= target`.
This is equivalent to a lower-bound binary search.

Algorithmically:
1. Maintain a search window `[left, right]`
2. Compute `mid`
3. If `nums[mid] < target`, move `left = mid + 1`
4. Otherwise, move `right = mid - 1`
5. When the loop ends, `left` is the correct insert position

## Solution Approach

1. Initialize `left = 0`, `right = nums.length - 1`
2. Binary search while `left <= right`
3. If `nums[mid] == target`, return `mid`
4. If `nums[mid] < target`, search right half
5. If `nums[mid] > target`, search left half
6. Return `left` after the loop

This works because the first index where the array value is not less than `target` is the insertion point.

## Brute-Force vs Optimized

**Brute-Force:** Scan the array from left to right and return the first index where `nums[i] >= target`. Time: O(n).

**Optimized:** Binary search on the sorted array to find the lower bound. Time: O(log n).

## Java Implementation

```java
class Solution {
    public int searchInsert(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return left;
    }
}
```

## Complexity Analysis

- Time: O(log n)
  - Binary search over the sorted array of length n
- Space: O(1)
  - Constant extra space for pointers and mid calculation

## Edge Cases / Traps

- `target` smaller than all elements → return `0`
- `target` larger than all elements → return `nums.length`
- `nums` length = 1 → still handled by binary search
- Off-by-one boundary: return `left`, not `right`
- Avoid overflow when computing `mid` by using `left + (right - left) / 2`

## When to Use This Pattern

Use this lower-bound binary search pattern when:
- the array is sorted
- you need an insertion index or first position satisfying a condition
- the answer is the first index where `nums[mid] >= target`

## Key Takeaways

- Search Insert Position is a lower-bound binary search problem
- The correct position after the loop is `left`
- Binary search gives the required O(log n) complexity for sorted input
- Always consider the insert position even when the target is absent
