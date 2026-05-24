# Problem: Find How Many Days Until A Warmer Temperature

## Source
- Platform: Anki deck seed
- Topic: Stack
- Tags: MonotonicStack, Arrays, Day4
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Cue

How to find how many days until a warmer temperature


## Brief Problem Statement

Given the problem setup, find an efficient way to find how many days until a warmer temperature.

## Recognition Pattern

- Topic signal: Stack
- Pattern hint from tags: MonotonicStack / Arrays
- Use this note to reconstruct the full solution before promoting it to Tier 1.

## Core Insight

Use a decreasing monotonic stack storing indices. For each new temp, pop smaller ones and calculate distance.

## Solution Approach

1. Restate the exact objective and input constraints.
2. Identify the main pattern suggested by the tags and cue.
3. Rebuild the optimized steps from the core insight above.
4. Dry run the logic on one small example before coding.

## Detailed Reasoning and Dry Run

### Objective and Constraints
- Input: An array `temperatures` where `temperatures[i]` is the temperature on day `i`.
- Output: For each day, return how many days you would have to wait until a warmer temperature. If there is no future day for which this is possible, put 0 instead.
- Constraints: $1 \leq n \leq 10^5$, $30 \leq temperatures[i] \leq 100$

### Why Brute Force is Slow
- For each day, you would need to scan forward until you find a warmer day. This leads to $O(n^2)$ time in the worst case.

### Why Monotonic Stack Works
- The stack keeps track of indices of days with unresolved warmer temperatures, in decreasing order of temperature.
- When a new temperature is higher than the top of the stack, it means we've found the next warmer day for all those popped indices.
- Each index is pushed and popped at most once, so the algorithm is $O(n)$.

### Dry Run Example
Suppose `temperatures = [73, 74, 75, 71, 69, 72, 76, 73]`

| i | temp | stack (top left) | answer |
|---|------|------------------|--------|
| 0 |  73  | [0]              | [0,0,0,0,0,0,0,0] |
| 1 |  74  | [1]              | [1,0,0,0,0,0,0,0] |
| 2 |  75  | [2]              | [1,1,0,0,0,0,0,0] |
| 3 |  71  | [2,3]            | [1,1,0,0,0,0,0,0] |
| 4 |  69  | [2,3,4]          | [1,1,0,0,0,0,0,0] |
| 5 |  72  | [2,3,5]          | [1,1,0,2,1,0,0,0] |
| 6 |  76  | [6]              | [1,1,4,2,1,1,0,0] |
| 7 |  73  | [6,7]            | [1,1,4,2,1,1,0,0] |

- For day 0 (73), the next warmer day is day 1 (74), so answer[0] = 1.
- For day 1 (74), the next warmer day is day 2 (75), so answer[1] = 1.
- For day 2 (75), the next warmer day is day 6 (76), so answer[2] = 4.
- ...and so on.

### Interview-Style Explanation
- "We use a monotonic decreasing stack to keep track of unresolved days. For each day, we pop from the stack until we find a day with a higher temperature, and record the difference in indices. This ensures each day is processed efficiently, and we avoid redundant comparisons."

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

## Full Java Solution
```java
class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prev = stack.pop();
                answer[prev] = i - prev;
            }
            stack.push(i);
        }
        return answer;
    }
}
```

## Complexity
- Time: $O(n)$ — Each index is pushed and popped at most once.
- Space: $O(n)$ — For the stack and output array.

## Brute-force vs Optimized Comparison
- **Brute-force:** For each day, scan forward until a warmer day is found. Time: $O(n^2)$.
- **Optimized (Monotonic Stack):** Each day is processed once, and stack operations are amortized. Time: $O(n)$.

## Additional Problem-Specific Traps
- Input array can be empty — return an empty array.
- All temperatures decreasing — all answers should be 0.
- Multiple days with the same temperature — ensure stack logic only pops for strictly warmer days.

## Tier
- Tier 1
