# Problem: Search in Rotated Sorted Array

## Source
- Platform: LeetCode
- Topic: Binary Search
- Tags: BinarySearch, Arrays, RotatedArray
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Search for a target in a sorted array that has been rotated once at an unknown pivot.

## Recognition Pattern

- Search problem on an array with sorted structure
- Rotation breaks global order but preserves order in one half at every step
- Binary search still works if we identify the sorted half

## Brute Force Thought

Scan the whole array until target is found.

Why it is too slow:
- wastes the sorted structure
- `O(n)` instead of `O(log n)`

## Core Insight

At any midpoint, at least one half is still sorted. Identify that sorted half, then check whether the target lies inside it. If yes, keep that half; otherwise search the other half.

## Solution Approach

1. Run normal binary search with `left` and `right`.
2. Compute `mid`.
3. If `nums[mid] == target`, return `mid`.
4. Check whether left half is sorted using `nums[left] <= nums[mid]`.
5. If left half is sorted, decide whether target lies in `[left, mid)`.
6. Otherwise the right half must be sorted; decide whether target lies in `(mid, right]`.

## Thought Process During Solving

1. Is the array fully sorted? No, but part of it always is.
2. What binary-search property survives rotation? One half remains ordered.
3. How do I know which half is sorted? Compare `nums[left]` and `nums[mid]`.
4. What is the most common bug? Boundary comparisons that accidentally discard the target.

## Java Solution
```java
class Solution {
    public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;
            }

            if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }

        return -1;
    }
}
```

## Complexity
- Time: `O(log n)`
- Space: `O(1)`

## Edge Cases / Traps

- Array size 1
- Target at pivot boundary
- Wrong `<` / `<=` conditions causing missed target
- Forgetting safe `mid` computation

## Why This Works

Rotation destroys full ordering but never destroys ordering in both halves at once. One side of `mid` remains sorted, so we can test whether the target belongs there using endpoint comparisons. That restores the discard-half logic binary search needs.

## Interview Explanation

Even after rotation, one half of the array is still sorted around every midpoint. I first identify the sorted half, then ask whether the target falls inside that range. If it does, I keep that half; otherwise I search the other one. That preserves binary search and gives `O(log n)` time.

## Similar Problems

- Find Minimum in Rotated Sorted Array
- Search Insert Position
- First and Last Position of Element in Sorted Array

## Anki Recall Prompts

- What property still holds in a rotated sorted array?
- How do I detect which half is sorted?
- Where do boundary bugs usually happen?
