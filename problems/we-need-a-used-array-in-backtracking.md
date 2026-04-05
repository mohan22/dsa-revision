# Problem: We Need A Used[] Array In Backtracking

## Source
- Platform: Anki deck seed
- Topic: Backtracking
- Tags: Backtracking, Permutations, Day11
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 2

## Problem Cue

When do we need a used[] array in backtracking

## Recognition Pattern

- Topic signal: Backtracking
- Pattern hint from tags: Backtracking / Permutations
- Use this note to reconstruct the full solution before promoting it to Tier 1.

## Core Insight

For permutations where order matters. Template: if used[i]: skip; choose; used[i]=True; dfs(); unchoose; used[i]=False.

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
