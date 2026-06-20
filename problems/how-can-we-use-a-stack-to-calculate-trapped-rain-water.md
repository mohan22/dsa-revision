# Problem: How can we use a stack to calculate trapped rain water

## Source
- Platform: Anki deck seed
- Topic: Stack
- Tags: Stack, Arrays, Day4
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Cue

How can we use a stack to calculate trapped rain water


## Brief Problem Statement

Core task: How can we use a stack to calculate trapped rain water.

## Recognition Pattern

- Topic signal: Stack
- Pattern hint from tags: Stack / Arrays
- Use this note to reconstruct the full solution before promoting it to Tier 1.


## Core Insight
Push indices onto the stack. When the current height is greater than the height at the stack's top, pop the stack and calculate trapped water using the width between the current index and the new stack top, and the bounded height (min of left and right minus the popped bar's height).


## Solution Approach

### Objective and Constraints
- Input: An array `height` of non-negative integers representing elevation map bars of width 1.
- Output: The total amount of trapped rain water.
- Constraints: $1 \leq n \leq 10^5$, $0 \leq height[i] \leq 10^5$

### Why Brute Force is Slow
- For each bar, scan left and right to find the first higher bar, then compute trapped water. This is $O(n^2)$.

### Why Stack Works
- The stack keeps track of indices of bars that may bound water. When a bar higher than the stack top is found, it means water can be trapped. The amount is determined by the distance between the current bar and the new stack top, and the bounded height.

### Dry Run Example
Suppose `height = [0,1,0,2,1,0,1,3,2,1,2,1]`

| i | height | stack (top left) | trapped |
|---|--------|------------------|---------|
| 0 |   0    | [0]              | 0       |
| 1 |   1    | [0,1]            | 0       |
| 2 |   0    | [0,1,2]          | 0       |
| 3 |   2    | [0,1]            | 1       |
| 4 |   1    | [0,1,4]          | 1       |
| 5 |   0    | [0,1,4,5]        | 1       |
| 6 |   1    | [0,1,4]          | 2       |
| 7 |   3    | [0,1]            | 5       |
...|

## Detailed Reasoning and Interview Explanation

**Why Brute Force is Slow:**
For each bar, searching for the next higher bar on both sides is $O(n^2)$.

**Why Stack is Efficient:**
The stack allows us to efficiently find boundaries for water trapping. Each bar is pushed and popped at most once, so the algorithm is $O(n)$.

**Interview-Style Explanation:**
"We use a stack to keep track of indices of bars. When we encounter a bar taller than the bar at the top of the stack, we pop the stack and calculate the water trapped above the popped bar, using the distance to the new stack top and the bounded height. This way, we efficiently compute trapped water in one pass."

## Full Java Solution
```java
class Solution {
    public int trap(int[] height) {
        int n = height.length, ans = 0, current = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        while (current < n) {
            while (!stack.isEmpty() && height[current] > height[stack.peek()]) {
                int top = stack.pop();
                if (stack.isEmpty()) break;
                int distance = current - stack.peek() - 1;
                int bounded_height = Math.min(height[current], height[stack.peek()]) - height[top];
                ans += distance * bounded_height;
            }
            stack.push(current++);
        }
        return ans;
    }
}
```

## Complexity
- Time: $O(n)$ — Each index is pushed and popped at most once.
- Space: $O(n)$ — For the stack.

## Brute-force vs Optimized Comparison
- **Brute-force:** For each bar, scan left and right for boundaries. Time: $O(n^2)$.
- **Optimized (Stack):** One pass, stack operations amortized. Time: $O(n)$.

## Additional Problem-Specific Traps
- Input array can be empty — return 0.
- All bars of same height — no water trapped.
- All increasing or all decreasing — no water trapped.
- Single bar or two bars — no water trapped.

## Tier
- Tier 1

## Thought Process During Solving

1. What makes the brute-force version slow here?
2. Which data structure or invariant fixes that repeated work?
3. What edge case is most likely to break the implementation?
4. Can I explain the approach in 3-4 interview sentences?

## Java Skeleton
```java
class Solution {
    public void solve() {
        // Fill in the final Java implementation during promotion to Tier 1.
    }
}
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
