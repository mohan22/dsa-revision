# Problem: Generate subsets with duplicates (Subsets II)

## Source
- Platform: Anki deck seed
- Topic: Backtracking
- Tags: Backtracking, Subsets, Duplicates, Day24
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Statement

Given an integer array that may contain duplicates, generate all unique subsets (the power set). Each subset should appear only once, and the order of elements in a subset does not matter.

The input typically includes:
- `nums[]`: array of integers (may contain duplicates)

The algorithm should:
- sort the array to identify and skip duplicate elements,
- use backtracking to build all subsets,
- skip duplicate values at the same recursion level to avoid duplicate subsets,
- include each number in the array at most as many times as it appears.

This pattern is essential for generating unique combinations and subsets when duplicates are present.

## Recognition Pattern

- Topic signal: Backtracking
- Pattern hint from tags: Backtracking / Subsets
- Key signal: generate all unique subsets with duplicate elements in input
- Tier 1 note: know the sorting + duplicate skipping invariant at each recursion level

## Brute Force Thought

A brute-force approach generates all subsets naively, leading to exponential duplicates. The optimized backtracking with sorting and skipping duplicate values at each level produces only unique subsets.

## Core Insight

Sort the array. In backtracking, for each position, skip over duplicate values at the same level by checking `i > start && nums[i] == nums[i-1]`. This prevents generating duplicate subsets.

## Solution Approach

1. Sort the input array.
2. Use backtracking with parameters: current subset, start index.
3. Add the current subset to result.
4. For i from start to end:
   - Skip if i > start and nums[i] == nums[i-1].
   - Add nums[i] to subset, recurse with i+1.
   - Backtrack by removing the last added number.
5. Return all subsets collected.

## Thought Process During Solving

1. Why does sorting help identify duplicates?
2. How does the skip condition `i > start && nums[i] == nums[i-1]` prevent duplicates?
3. Why use i+1 in the recursion instead of considering all remaining elements?
4. What is the base case and when do we add a subset to the result?

## Java Skeleton
```java
class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), nums, 0);
        return result;
    }

    private void backtrack(List<List<Integer>> result, List<Integer> temp, int[] nums, int start) {
        result.add(new ArrayList<>(temp));
        for (int i = start; i < nums.length; i++) {
            if (i > start && nums[i] == nums[i - 1]) continue;
            temp.add(nums[i]);
            backtrack(result, temp, nums, i + 1);
            temp.remove(temp.size() - 1);
        }
    }
}
```

## Complexity
- Time: O(2^n)
- Space: O(n) for recursion stack and O(2^n) for result

## Edge Cases / Traps

- Empty array should return a list with one empty subset.
- All elements are the same, e.g., [1,1,1].
- Array with mixed duplicates and unique elements.
- Single element array.

## Why This Works

Sorting allows adjacent duplicates to be identified. The skip condition ensures that at each recursion level, only the first occurrence of a duplicate value is processed. This guarantees all and only unique subsets are generated.

## Interview Explanation

Sort the array, then use backtracking to build subsets. Skip duplicate values at each level to avoid generating duplicate subsets.

## Similar Problems

- Subsets (without duplicates)
- Combination Sum II (avoid duplicates)
- Permutations II (unique permutations)

## Anki Recall Prompts

- Why must the array be sorted first?
- What does the skip condition `i > start && nums[i] == nums[i-1]` do?
- When is a subset added to the result?

