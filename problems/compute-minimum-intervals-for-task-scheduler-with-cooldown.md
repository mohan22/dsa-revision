# Problem: Compute minimum intervals for Task Scheduler with cooldown

## Source
- Platform: Anki deck seed
- Topic: Greedy
- Tags: Greedy, PQ, Scheduling, Day16
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Cue

How to compute minimum intervals for Task Scheduler with cooldown


## Brief Problem Statement

Given the problem setup, find an efficient way to compute minimum intervals for Task Scheduler with cooldown.

## Recognition Pattern

- Topic signal: Greedy
- Pattern hint from tags: Greedy / PQ
- Use this note to reconstruct the full solution before promoting it to Tier 1.

## Core Insight

Greedy formula: max((maxFreq-1)*(n+1) + countMaxFreq, totalTasks). Alternative: PQ-based scheduling.

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

## Java Solution
```java
class Solution {
    public int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        int maxFreq = 0;

        for (char task : tasks) {
            maxFreq = Math.max(maxFreq, ++freq[task - 'A']);
        }

        int countMaxFreq = 0;
        for (int f : freq) {
            if (f == maxFreq) {
                countMaxFreq++;
            }
        }

        int intervals = (maxFreq - 1) * (n + 1) + countMaxFreq;
        return Math.max(tasks.length, intervals);
    }
}
```

## Complexity
- Time: O(T + 26) = O(T), where T is the number of tasks
- Space: O(1), since the frequency array size is fixed

## Brute-force vs Optimized
- Brute force simulates each time slot and tracks cooling status, which can be slow when tasks are many and cooldowns are large.
- Optimized formula uses the most frequent task to bound the minimum schedule length and fills the rest with idle slots or other tasks.

## Edge Cases / Traps

- Check boundary conditions, duplicates, and empty input.
- Verify the invariant or state after each update.
- Confirm whether recursion, heap ordering, or index movement can fail.

## Promotion Checklist

- Add a full Java solution.
- Add exact time and space complexity.
- Add one short brute-force vs optimized comparison.
- Add 2-3 problem-specific traps.
