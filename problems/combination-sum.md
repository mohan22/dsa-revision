# Problem: Combination Sum

## Source
- Platform: LeetCode
- Topic: Backtracking
- Tags: Backtracking, Combinations
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Return all combinations of numbers that sum to the target. Each candidate can be reused unlimited times.

## Recognition Pattern

- Need all valid combinations, not just one answer
- Choices are built incrementally
- Reuse is allowed, so the same index can stay available after choosing it

## Brute Force Thought

Generate all possible sequences of choices and filter those whose sum equals target.

Why it is too slow:
- huge search space
- ignores pruning and combination structure

## Core Insight

This is backtracking with pruning. At each step, choose a candidate, recurse on the reduced target, then undo the choice. Because reuse is allowed, recurse with the same index instead of `i + 1`.

## Solution Approach

1. Use DFS with a `start` index to avoid permutation duplicates.
2. Sort candidates so we can break early when a candidate exceeds the remaining target.
3. If remaining target becomes `0`, record the current path.
4. For each candidate from `start` onward:
   - choose it
   - recurse with reduced target and same index
   - unchoose it

## Thought Process During Solving

1. Do I need all answers or one answer? All answers, so think backtracking.
2. Does order matter? No, combinations not permutations.
3. Can I reuse a chosen candidate? Yes, so recurse with `i`, not `i + 1`.
4. What pruning helps? If candidates are sorted and current value exceeds remaining target, stop the loop.

## Java Solution
```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> answer = new ArrayList<>();
        backtrack(candidates, target, 0, new ArrayList<>(), answer);
        return answer;
    }

    private void backtrack(int[] candidates, int remaining, int start,
            List<Integer> path, List<List<Integer>> answer) {
        if (remaining == 0) {
            answer.add(new ArrayList<>(path));
            return;
        }

        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remaining) {
                break;
            }

            path.add(candidates[i]);
            backtrack(candidates, remaining - candidates[i], i, path, answer);
            path.remove(path.size() - 1);
        }
    }
}
```

## Complexity
- Time: Exponential in the number of valid search branches
- Space: `O(target)` recursion depth in the worst case, plus output

## Edge Cases / Traps

- Reuse allowed means recurse with `i`, not `i + 1`
- Need to copy `path` before adding to answer
- Sorting is for pruning, not for correctness alone
- This problem asks combinations, so avoid permutation duplicates

## Why This Works

Backtracking systematically explores every valid combination prefix. The `start` index prevents reordering duplicates like `[2,3]` and `[3,2]`, while reusing `i` allows unlimited use of the same candidate. Sorting lets us prune impossible branches early once values become too large.

## Interview Explanation

I use backtracking because I need to generate all valid combinations. Since order does not matter, I keep a `start` index so I only build combinations in non-decreasing candidate order. Because a number may be reused, when I take `candidates[i]` I recurse again from `i`, not `i + 1`. Sorting lets me stop early when the remaining target is too small.

## Similar Problems

- Combination Sum II
- Subsets
- Permutations

## Anki Recall Prompts

- Why do we recurse with the same index here?
- Why is sorting useful?
- How do we avoid permutation duplicates?
