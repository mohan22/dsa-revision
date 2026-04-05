# Problem: Find The Next Greater Element For Each Element In Nums1 Relative To Nums2

## Source
- Platform: Anki deck seed
- Topic: Stack
- Tags: MonotonicStack, Arrays, Day4
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 2

## Problem Cue

How to find the next greater element for each element in nums1 relative to nums2

## Recognition Pattern

- Topic signal: Stack
- Pattern hint from tags: MonotonicStack / Arrays
- Use this note to reconstruct the full solution before promoting it to Tier 1.

## Core Insight

Traverse nums2, use stack to store decreasing elements; map each popped element to its next greater.

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
