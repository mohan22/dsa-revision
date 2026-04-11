# Problem: Why is 0-1 BFS used for 01 Matrix

## Source
- Platform: Anki deck seed
- Topic: Graph / Matrix
- Tags: Graph, ZeroOne, BFS, Grid, Day21
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 2

## Problem Cue

Why is 0-1 BFS used for 01 Matrix


## Brief Problem Statement

Explain why is 0-1 BFS used for 01 Matrix and how it affects the algorithm choice.

## Recognition Pattern

- Topic signal: Graph / Matrix
- Pattern hint from tags: Graph / ZeroOne
- Use this note to reconstruct the full solution before promoting it to Tier 1.

## Core Insight

Moves have cost 0 or 1. Use deque: weight 0 → appendleft weight 1 → append Alt: multi-source BFS from all zeros.

## Solution Approach

1. Restate the exact objective and input constraints.
2. Identify the main pattern suggested by the tags and cue.
3. Rebuild the optimized steps from the core insight above.
4. Dry run the logic on one small example before coding.

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
