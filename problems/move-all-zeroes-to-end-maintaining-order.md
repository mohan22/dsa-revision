# Problem: Move All Zeroes To End Maintaining Order

## Source
- Platform: LeetCode
- Topic: Arrays / Two Pointers
- Tags: TwoPointers, Arrays
- Difficulty: Easy
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given an array nums, write a function to move all 0's to the end of it while maintaining the relative order of the non-zero elements.

## Recognition Pattern

- We need to modify the array in-place.
- Relative order of non-zeros must be preserved.
- Zeros can be moved to the end without order constraint.
- Two pointers pattern for in-place rearrangement.

## Brute Force Thought

Create a new array, iterate through the original, add non-zeros first, then zeros.

Why it is too slow:
- Uses extra space O(n)
- Not in-place as required

## Core Insight

Use two pointers: `slow` tracks where to place next non-zero, `fast` iterates through array. Swap non-zeros to front, leaving zeros at end.

## Solution Approach

1. Initialize `slow` pointer at 0.
2. Iterate `fast` from 0 to n-1.
3. When `nums[fast]` is non-zero, swap with `nums[slow]`, increment `slow`.
4. After iteration, all non-zeros are at the beginning, zeros at the end.

## Thought Process During Solving

1. What makes the brute-force version use extra space? Creating a new array.
2. Which data structure or invariant fixes that? Two pointers for in-place modification.
3. What edge case is most likely to break? All zeros, all non-zeros, single element.
4. Can I explain the approach in 3-4 interview sentences? Use two pointers: slow for placement, fast for scanning. Swap non-zeros forward, zeros bubble to end.

## Java Solution
```java
class Solution {
    public void moveZeroes(int[] nums) {
        int slow = 0;
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != 0) {
                // Swap nums[slow] and nums[fast]
                int temp = nums[slow];
                nums[slow] = nums[fast];
                nums[fast] = temp;
                slow++;
            }
        }
    }
}
```

## Complexity
- Time: `O(n)`
- Space: `O(1)`

## Edge Cases / Traps

- Array with all zeros: remains unchanged
- Array with all non-zeros: remains unchanged
- Array with one element: no change needed
- Empty array: no operation
- Zeros interspersed: correctly moves all to end

## Why This Works

The `slow` pointer always points to the next position where a non-zero should be placed. When a non-zero is found at `fast`, it's swapped with the element at `slow`, and `slow` advances. Zeros are effectively pushed to the right as non-zeros move left.

## Interview Explanation

We use two pointers: `slow` starts at 0 and tracks where the next non-zero should go, `fast` scans the array. When `fast` finds a non-zero, we swap it with the element at `slow` and increment `slow`. This moves all non-zeros to the front while preserving their order, and zeros end up at the back.

## Similar Problems

- Remove Element
- Sort Colors
- Move Zeroes (this one)

## Anki Recall Prompts

- How do two pointers work for moving zeros?
- Why does swapping preserve order?
- What happens to slow pointer?
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
