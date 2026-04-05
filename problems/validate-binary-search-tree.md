# Problem: Validate Binary Search Tree

## Source
- Platform: LeetCode
- Topic: Trees / BST
- Tags: BST, Validate, DFS
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Check whether a binary tree satisfies the BST property everywhere.

## Recognition Pattern

- BST rules are global constraints, not just local comparisons
- Every node must stay within a valid lower and upper bound inherited from ancestors
- DFS with bounds is the cleanest validation strategy

## Brute Force Thought

For every node, scan its left subtree for smaller values and right subtree for larger values.

Why it is too slow:
- repeated subtree rescans
- clumsy global validation

## Core Insight

A node is valid only if it lies strictly inside an allowed range. As we recurse left, the upper bound becomes the current node value. As we recurse right, the lower bound becomes the current node value.

## Solution Approach

1. Run DFS with `(lower, upper)` bounds.
2. If node is `null`, it is valid.
3. If `node.val` is not strictly between the bounds, return `false`.
4. Recurse left with `(lower, node.val)`.
5. Recurse right with `(node.val, upper)`.

## Thought Process During Solving

1. Can I validate BST using only parent-child checks? No.
2. What extra information from ancestors matters? The full allowed range.
3. Are duplicates allowed? In the classic problem, no, so use strict inequality.
4. Why use `long` bounds? To safely handle `Integer.MIN_VALUE` and `Integer.MAX_VALUE`.

## Java Solution
```java
class Solution {
    public boolean isValidBST(TreeNode root) {
        return dfs(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean dfs(TreeNode node, long lower, long upper) {
        if (node == null) {
            return true;
        }

        if (node.val <= lower || node.val >= upper) {
            return false;
        }

        return dfs(node.left, lower, node.val) && dfs(node.right, node.val, upper);
    }
}
```

## Complexity
- Time: `O(n)`
- Space: `O(h)` recursion stack, where `h` is tree height

## Edge Cases / Traps

- Using only local child comparisons
- Allowing duplicates by mistake
- Overflow when using `int` bounds instead of `long`
- Highly unbalanced tree increases recursion depth

## Why This Works

Each recursive call carries the exact numeric interval a node is allowed to occupy. Those bounds summarize all ancestor constraints. If every node stays within its legal range and both subtrees respect their updated bounds, the whole tree satisfies the BST definition.

## Interview Explanation

The key idea is that BST validation is global. A node in the left subtree of the root must be smaller than the root even if it is larger than its direct parent. So I pass down valid bounds during DFS. Every node must be strictly between those bounds, and the bounds tighten as I go left or right.

## Similar Problems

- Kth Smallest Element in BST
- Lowest Common Ancestor in BST
- Insert into a BST

## Anki Recall Prompts

- Why are local comparisons not enough?
- What bounds do I pass to left and right children?
- Why should the bounds be `long`?
