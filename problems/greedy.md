# Problem: Greedy

## Source
- Platform: Anki deck seed
- Topic: Greedy
- Tags: Greedy, LocalOpt, Day16
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 2

## Problem Cue

What is the local optimality pattern in greedy


## Brief Problem Statement

Identify the core idea behind local optimality pattern in greedy and know when to apply it.

## Recognition Pattern

- Topic signal: Greedy
- Pattern hint from tags: Greedy / LocalOpt
- Use this note to reconstruct the full solution before promoting it to Tier 1.

## Core Insight

Identify local best choice (smallest cost, earliest end, largest gain). Examples: candy distribution, partition labels.

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
