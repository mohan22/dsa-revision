# Problem: Search A Word In A Grid Using Backtracking

## Source
- Platform: LeetCode / Interview
- Topic: Backtracking
- Tags: Backtracking, Grid, DFS, Day11
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Statement

Given a 2D grid of characters and a word, determine if the word exists in the grid. The word can be constructed from letters of sequentially adjacent cells (horizontal or vertical neighbors), and the same cell may not be used more than once.

The input typically includes:
- `board[][]`: a 2D array of characters
- `word`: the string to search for

The algorithm should:
- start DFS from each cell that matches the first letter of the word,
- explore all four directions (up, down, left, right) recursively,
- mark cells as visited to prevent reuse,
- backtrack by unmarking cells after exploration.

This pattern is essential for grid-based pathfinding problems with constraints on cell reuse.

## Recognition Pattern

- Topic signal: Backtracking
- Pattern hint from tags: Backtracking / Grid / DFS
- Key signal: find a path in a grid that matches a sequence, with no cell reuse
- Tier 1 note: know the DFS + backtracking invariant with visited marking and direction exploration

## Brute Force Thought

A brute-force approach checks every possible path starting from each cell, but without visited tracking, it leads to infinite loops. The optimized DFS + backtracking uses a visited array to prune invalid paths efficiently.

## Core Insight

For each cell matching the word's first letter, perform DFS: at each step, check if the current cell matches the word's current index, mark visited, explore neighbors, then backtrack by unmarking.

## Solution Approach

1. Iterate through each cell in the grid.
2. If the cell matches the first letter of the word, start DFS from there.
3. In DFS: if the current index equals word length, return true.
4. Mark the current cell as visited.
5. Explore all four directions: if neighbor is valid, unvisited, and matches the next letter, recurse.
6. After exploration, unmark the cell (backtrack).
7. If any DFS returns true, the word exists.

## Thought Process During Solving

1. Why is backtracking necessary here?
2. How does the visited array prevent cycles?
3. What happens if the word is longer than the grid?
4. Why explore all starting points?

## Java Skeleton
```java
class Solution {
    public boolean exist(char[][] board, String word) {
        if (board == null || board.length == 0 || word == null) {
            return false;
        }

        int rows = board.length;
        int cols = board[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == word.charAt(0) && dfs(board, word, i, j, 0)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean dfs(char[][] board, String word, int i, int j, int index) {
        if (index == word.length()) {
            return true;
        }

        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != word.charAt(index)) {
            return false;
        }

        char temp = board[i][j];
        board[i][j] = '#'; // mark visited

        boolean found = dfs(board, word, i + 1, j, index + 1) ||
                         dfs(board, word, i - 1, j, index + 1) ||
                         dfs(board, word, i, j + 1, index + 1) ||
                         dfs(board, word, i, j - 1, index + 1);

        board[i][j] = temp; // backtrack

        return found;
    }
}
```

## Complexity
- Time: O(N * 4^L) where N is the number of cells, L is the word length
- Space: O(L) for recursion stack

## Edge Cases / Traps

- Empty grid or word should return false.
- Word longer than grid cells.
- Multiple occurrences of the same letter in the grid.
- Word starts with a letter not in the grid.

## Why This Works

DFS explores paths matching the word prefix, and backtracking ensures cells are reused in other paths. The visited marking prevents cycles and invalid reuse, guaranteeing correct path finding.

## Interview Explanation

Use DFS from each cell matching the first letter. Mark cells as visited during exploration, check neighbors for the next letter, and backtrack to try other paths.

## Similar Problems

- Word Search II (with Trie)
- Number of Islands
- Surrounded Regions

## Anki Recall Prompts

- Why mark cells as visited in the grid itself?
- What are the base cases for the DFS?
- How does backtracking work here?

