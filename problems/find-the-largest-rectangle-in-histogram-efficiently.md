# Problem: Find The Largest Rectangle In Histogram Efficiently

## Source
- Platform: LeetCode
- Topic: Stack / Monotonic Stack
- Tags: MonotonicStack, Stack
- Difficulty: Hard
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given an array of integers heights representing the histogram's bar height where the width of each bar is 1, return the area of the largest rectangle in the histogram.

## Recognition Pattern

- Histogram bars with widths 1.
- Need maximum area rectangle formed by bars.
- Use monotonic stack to find for each bar, the largest rectangle with that bar as height.
- Stack maintains indices in increasing height order.

## Brute Force Thought

For each bar, expand left and right until smaller bar found, compute area.

Why it is too slow:
- O(n^2) time, as each expansion can take O(n).

## Core Insight

Use a monotonic increasing stack of indices. For each bar, while stack top has taller bar, pop and compute area: height * (current index - stack top after pop - 1).

## Solution Approach

1. Add a sentinel bar of height 0 at end.
2. Use stack to store indices.
3. For each bar, while stack not empty and current height < stack top height, pop and compute area.
4. Push current index.
5. After processing, pop remaining and compute areas.

## Thought Process During Solving

1. What makes brute-force slow? Repeated expansions for each bar.
2. Which data structure fixes it? Monotonic stack to compute left/right boundaries in O(1) amortized.
3. What edge case breaks it? All increasing, all decreasing, single bar.
4. Can I explain in 3-4 sentences? Use a stack to keep indices of increasing heights. When a smaller height is encountered, pop taller bars and calculate their areas using the width between current and previous smaller bar.

## Java Solution
```java
import java.util.Stack;

class Solution {
    public int largestRectangleArea(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        int n = heights.length;

        for (int i = 0; i <= n; i++) {
            int h = (i == n) ? 0 : heights[i];
            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }

        return maxArea;
    }
}
```

## Complexity
- Time: `O(n)`
- Space: `O(n)`

## Edge Cases / Traps

- All bars same height: Area = height * n
- Strictly increasing: Each bar's area is height * 1
- Strictly decreasing: Last bar's area is height * n
- Single bar: Area = height
- Empty array: 0

## Why This Works

The stack ensures for each popped bar, the left boundary is the previous stack top (or -1), right is current index. This gives the maximum width for that height.

## Interview Explanation

We use a monotonic stack to process bars from left to right. When we encounter a bar shorter than the stack top, we pop the taller bars and calculate their rectangle areas using the width from the new stack top to the current position. This efficiently finds the largest possible rectangle for each bar.

## Similar Problems

- Maximal Rectangle
- Largest Rectangle in Histogram
- Trapping Rain Water

## Anki Recall Prompts

- How does the stack maintain monotonic order?
- What is the width calculation for a popped bar?
- Why add a sentinel at the end?
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
