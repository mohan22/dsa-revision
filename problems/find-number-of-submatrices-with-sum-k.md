# Problem: Find Number Of Submatrices With Sum = K

## Source
- Platform: LeetCode
- Topic: Matrix / Prefix Sum
- Tags: PrefixSum, Matrix, HashMap
- Difficulty: Hard
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given a 2D matrix of integers and an integer k, count the number of non-empty submatrices whose sum equals k.

## Recognition Pattern

- Matrix problem with sum constraint.
- Reduce the 2D problem to repeated 1D subarray sum problems.
- Use prefix sums across rows and a hashmap for subarray counts.
- Common pattern for matrix sum problems: fix a pair of rows and collapse columns.

## Brute Force Thought

Enumerate every possible top-left and bottom-right corner, then compute the sum for each submatrix.

Why it is too slow:
- O(n^4) possible submatrices in an n x n matrix.
- Calculating each sum directly makes the approach infeasible for large inputs.

## Core Insight

Fix two row boundaries and compute the sum of each column between these rows. This collapses the 2D matrix to a 1D array of column sums. Then count subarrays in that 1D array whose sum equals k using a prefix-sum hashmap.

## Solution Approach

1. Iterate over all row pairs `top` and `bottom`.
2. Maintain a `colSum` array where `colSum[c]` is the sum of values in column `c` between rows `top` and `bottom`.
3. For each row pair, find the number of subarrays in `colSum` with sum `k` using the 1D prefix-sum hashmap technique.
4. Accumulate results across all row pairs.

## Java Solution
```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int numSubmatrixSumTarget(int[][] matrix, int target) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int count = 0;

        for (int top = 0; top < rows; top++) {
            int[] colSum = new int[cols];

            for (int bottom = top; bottom < rows; bottom++) {
                for (int c = 0; c < cols; c++) {
                    colSum[c] += matrix[bottom][c];
                }

                count += countSubarraysWithSum(colSum, target);
            }
        }

        return count;
    }

    private int countSubarraysWithSum(int[] nums, int target) {
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1);

        int sum = 0;
        int count = 0;

        for (int num : nums) {
            sum += num;
            count += prefixCount.getOrDefault(sum - target, 0);
            prefixCount.put(sum, prefixCount.getOrDefault(sum, 0) + 1);
        }

        return count;
    }
}
```

## Complexity
- Time: `O(rows^2 * cols)`
- Space: `O(cols)` for the collapsed column sums and prefix-sum hashmap

## Edge Cases / Traps

- Single row or single column cases should still reduce to the same prefix-sum logic.
- Negative values are allowed, so the hashmap must be used rather than two-pointer scanning.
- Empty matrix is invalid per problem constraints, but handle small dimensions carefully.
- Reuse `colSum` across bottom row expansion to avoid recomputing column sums.

## Why This Works

By fixing row boundaries, each submatrix is represented as a contiguous subarray over the collapsed column sums. The prefix-sum hashmap counts how many previous column-sum prefixes lead to the current sum minus target, giving the number of valid subarrays for that row strip.

## Interview Explanation

First, convert the matrix problem into many 1D subarray sum problems by fixing two row boundaries. For each pair of rows, sum columns between them and count how many contiguous column ranges equal k using prefix sums and a hashmap. This avoids examining every submatrix individually and reduces the complexity dramatically.

## Similar Problems

- Submatrix Sum Equals Target
- Count Subarrays With Sum = K
- Maximum Sum Rectangle in a 2D Matrix

## Anki Recall Prompts

- How do row-pair boundaries reduce the matrix problem?
- Why use a hashmap for the collapsed column sums?
- What is the time complexity of the optimized solution?
