# Graph

## Recognition Cues

- Connectivity, components, reachability
- Shortest path in unweighted graph
- Cycle detection or traversal over neighbors
- Grid treated as implicit graph

## DFS vs BFS

- DFS: components, flood fill, backtracking-style exploration
- BFS: shortest path in unweighted graphs, layer-by-layer spread

## Grid DFS Template

```java
private void dfs(char[][] grid, int row, int col) {
    if (row < 0 || col < 0 || row == grid.length || col == grid[0].length || grid[row][col] != '1') {
        return;
    }

    grid[row][col] = '0';
    dfs(grid, row + 1, col);
    dfs(grid, row - 1, col);
    dfs(grid, row, col + 1);
    dfs(grid, row, col - 1);
}
```

## Common Mistakes

- Revisiting nodes because visited state is delayed
- Using DFS when shortest path is needed in unweighted graph
- Forgetting bounds checks in grid traversal
