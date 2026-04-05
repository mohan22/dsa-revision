# Problem: Number of Islands

## Source
- Platform: LeetCode
- Topic: Graph / Matrix
- Tags: Graph, DFS, BFS, Matrix
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Count how many connected groups of land cells (`'1'`) exist in a binary grid.

## Recognition Pattern

- Grid is an implicit graph
- Need connected component count
- Repeatedly find an unvisited land cell and flood-fill its component

## Brute Force Thought

For every land cell, try to check its whole connected region independently.

Why it is too slow:
- repeated traversal of the same cells
- no visited-state ownership

## Core Insight

Each time we discover an unvisited land cell, it starts exactly one new island. A DFS or BFS can mark that entire island so it is never counted again.

## Solution Approach

1. Traverse every cell in the grid.
2. When a cell is land, increment the island count.
3. Run DFS from that cell to mark the whole connected component as visited.
4. Use in-place marking by turning visited land into water.

## Thought Process During Solving

1. Is this shortest path? No, just components.
2. What should the count represent? The number of times I start exploring new land.
3. How do I avoid recounting cells? Mark them visited immediately.
4. DFS or BFS? Either works; DFS is compact here.

## Java Solution
```java
class Solution {
    public int numIslands(char[][] grid) {
        int islands = 0;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == '1') {
                    islands++;
                    dfs(grid, row, col);
                }
            }
        }

        return islands;
    }

    private void dfs(char[][] grid, int row, int col) {
        if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length || grid[row][col] != '1') {
            return;
        }

        grid[row][col] = '0';

        dfs(grid, row + 1, col);
        dfs(grid, row - 1, col);
        dfs(grid, row, col + 1);
        dfs(grid, row, col - 1);
    }
}
```

## Complexity
- Time: `O(m * n)`
- Space: `O(m * n)` in the worst case due to recursion stack

## Edge Cases / Traps

- Empty grid in a generalized implementation
- Forgetting to mark visited before exploring neighbors
- Stack depth on very large dense grids
- Diagonals do not count unless explicitly stated

## Why This Works

An island is exactly a connected component of land cells under 4-direction adjacency. The first unvisited land cell you encounter must belong to a new island, and DFS marks every cell in that same component. Therefore each island is counted once and only once.

## Interview Explanation

I treat the grid as a graph where each land cell is a node connected to its up/down/left/right land neighbors. Then I scan the grid. Whenever I find unvisited land, I increment the answer and run DFS to sink that whole island. That turns the problem into counting connected components.

## Similar Problems

- Max Area of Island
- Surrounded Regions
- Rotting Oranges

## Anki Recall Prompts

- Why does each DFS start correspond to one island?
- When should I mark a cell visited?
- Why is BFS not necessary here?
