# Problem: Combination Sum II avoid duplicates

## Source
- Platform: Anki deck seed
- Topic: Backtracking
- Tags: Backtracking, CombinationSum, Duplicates, Day24
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Statement

Given an array of candidates and a target sum, find all unique combinations where the candidate numbers sum to the target. Each number in candidates may only be used once in the combination, and the solution set must not contain duplicate combinations.

The input typically includes:
- `candidates[]`: array of integers (may contain duplicates)
- `target`: the target sum

The algorithm should:
- sort the candidates to handle duplicates,
- use backtracking to build combinations,
- skip duplicate numbers at the same level to avoid duplicate combinations,
- prune branches where the current number exceeds the remaining target.

This pattern is essential for backtracking problems involving combinations with uniqueness constraints.

## Recognition Pattern

- Topic signal: Backtracking
- Pattern hint from tags: Backtracking / CombinationSum
- Key signal: find all unique combinations summing to target, no duplicates, each number used at most once
- Tier 1 note: know the sorting + skipping duplicates invariant and the pruning condition

## Brute Force Thought

A brute-force approach tries all subsets, leading to exponential time with duplicates causing redundant work. The optimized backtracking with sorting and skipping reduces to unique combinations efficiently.

## Core Insight

Sort candidates. In backtracking, for each position, skip over duplicate values at the same level. Use each number at most once by incrementing index. Prune if current candidate > remaining target.

## Solution Approach

1. Sort the candidates array.
2. Use backtracking with parameters: current combination, start index, remaining target.
3. At each step, for i from start to end:
   - Skip if i > start and candidates[i] == candidates[i-1].
   - If candidates[i] > remaining, break.
   - Add candidates[i] to combination, recurse with i+1 and remaining - candidates[i].
   - Backtrack by removing the last added number.
4. When remaining == 0, add the combination to result.

## Thought Process During Solving

1. Why does sorting help with duplicates?
2. How does skipping duplicates at the same level work?
3. Why prune when candidate > remaining target?
4. What is the difference from Combination Sum I?

## Java Skeleton
```java
class Solution {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), candidates, target, 0);
        return result;
    }

    private void backtrack(List<List<Integer>> result, List<Integer> temp, int[] candidates, int remain, int start) {
        if (remain < 0) return;
        if (remain == 0) {
            result.add(new ArrayList<>(temp));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (i > start && candidates[i] == candidates[i - 1]) continue;
            if (candidates[i] > remain) break;
            temp.add(candidates[i]);
            backtrack(result, temp, candidates, remain - candidates[i], i + 1);
            temp.remove(temp.size() - 1);
        }
    }
}
```

## Complexity
- Time: O(2^n) worst case, but pruning and duplicates reduce it
- Space: O(n) for recursion stack and result

## Edge Cases / Traps

- Empty candidates or target = 0.
- All candidates are duplicates.
- Target is negative or larger than sum of candidates.
- Candidates contain zeros.

## Why This Works

Sorting allows skipping duplicates by checking adjacent elements. Using i+1 ensures each number is used at most once. Pruning stops early when no more valid combinations are possible.

## Interview Explanation

Sort the array, then use backtracking to build combinations. Skip duplicates at each level and prune when the current number exceeds the remaining target.

## Similar Problems

- Combination Sum (unlimited use)
- Subsets II (unique subsets)
- Permutations II (unique permutations)

## Anki Recall Prompts

- Why sort the candidates?
- How do you skip duplicates in backtracking?
- When do you prune the search?

