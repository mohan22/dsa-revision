# Problem: Calculate Trapped Rain Water Efficiently

## Source
- Platform: LeetCode
- Topic: Two Pointers / Arrays
- Tags: TwoPointers, Arrays
- Difficulty: Hard
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.

## Recognition Pattern

- Elevation map with bars of width 1.
- Water trapped between bars depends on min of max heights on left and right.
- Two pointers approach: start from both ends, move inward.
- Maintain maxLeft and maxRight as you go.

## Brute Force Thought

For each bar, find max height to left and right, trapped = min(maxLeft, maxRight) - height[i].

Why it is too slow:
- O(n^2) time, as finding max left/right for each bar takes O(n).

## Core Insight

Use two pointers: left=0, right=n-1. Track maxLeft and maxRight. Move the pointer with smaller max height inward, adding trapped water when current height < max.

## Solution Approach

1. Initialize left=0, right=n-1, maxLeft=0, maxRight=0, trapped=0.
2. While left < right:
   - If height[left] < height[right], process left pointer.
   - Else, process right pointer.
   - For left: if height[left] > maxLeft, update maxLeft; else add maxLeft - height[left] to trapped.
   - Similarly for right.
3. Return trapped.

## Thought Process During Solving

1. What makes brute-force slow? Repeated max computations.
2. Which data structure fixes it? Two pointers with running maxes.
3. What edge case breaks it? All increasing, all decreasing, flat.
4. Can I explain in 3-4 sentences? Use two pointers from ends. Track max heights seen so far. Move the pointer with smaller max height, adding trapped water based on the difference.

## Java Solution
```java
class Solution {
    public int trap(int[] height) {
        if (height == null || height.length == 0) return 0;

        int left = 0, right = height.length - 1;
        int maxLeft = 0, maxRight = 0;
        int trapped = 0;

        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= maxLeft) {
                    maxLeft = height[left];
                } else {
                    trapped += maxLeft - height[left];
                }
                left++;
            } else {
                if (height[right] >= maxRight) {
                    maxRight = height[right];
                } else {
                    trapped += maxRight - height[right];
                }
                right--;
            }
        }

        return trapped;
    }
}
```

## Complexity
- Time: `O(n)`
- Space: `O(1)`

## Edge Cases / Traps

- No water trapped: strictly increasing or decreasing.
- All same height: no water.
- Single bar: 0 water.
- Empty array: 0.
- Water at ends: only trapped between higher bars.

## Why This Works

By moving the pointer with the smaller max height, we ensure we're always processing the limiting factor. The trapped water is correctly calculated as the difference between the current max and the bar height.

## Interview Explanation

We use two pointers starting from both ends of the array. We maintain the maximum heights seen from left and right. At each step, we move the pointer that has the smaller maximum height, and if the current bar is shorter than that maximum, we add the difference to the trapped water.

## Similar Problems

- Container With Most Water
- Trapping Rain Water II
- Largest Rectangle in Histogram

## Anki Recall Prompts

- Why move the pointer with smaller max height?
- How is trapped water calculated at each step?
- What happens when heights are equal?
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
