# Problem: Count Connected Components Using DSU

## Source
- Platform: LeetCode / Pattern
- Topic: DSU
- Tags: DSU, Components
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Use DSU when an undirected graph has many edges and you want to count how many disconnected groups of nodes remain after processing all unions.

## Brief Problem Statement

You are given `n` nodes labeled `0` to `n - 1` and a list of undirected edges. Count how many connected components exist in the graph.

## Recognition Pattern

- Undirected graph
- Need number of connected components, not explicit traversal order
- Many union operations as edges are processed
- DSU fits naturally because each successful union reduces the component count by one

## Brute Force Thought

Build the graph and run DFS or BFS from every unvisited node to count components.

Why it is too slow or less convenient here:
- still linear-time, but requires explicit adjacency construction and traversal
- DSU is often cleaner when the graph arrives as edge unions or when we want an online connectivity view

## Core Insight

Start with `n` separate components. Every time an edge connects two previously separate roots, merge them and decrement the component count by one. If the endpoints are already in the same set, the number of components does not change.

## Solution Approach

1. Initialize DSU with `n` nodes.
2. Set `components = n`.
3. For each edge `[u, v]`:
   - if `union(u, v)` succeeds, decrement `components`
   - otherwise do nothing because the edge stays inside an existing component
4. Return `components` at the end.

## Thought Process During Solving

1. What is the graph quantity I need to maintain? Number of connected groups.
2. What is the initial count before reading edges? `n`, because every node starts alone.
3. When should the count decrease? Only when an edge merges two different components.
4. How do I detect that efficiently? Compare roots with DSU.
5. Why is this a good DSU problem? The graph is described directly by union operations.

## Java Solution
```java
class Solution {
    public int countComponents(int n, int[][] edges) {
        DSU dsu = new DSU(n);
        int components = n;

        for (int[] edge : edges) {
            if (dsu.union(edge[0], edge[1])) {
                components--;
            }
        }

        return components;
    }

    private static class DSU {
        private final int[] parent;
        private final int[] rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return false;
            }

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }

            return true;
        }
    }
}
```

## Complexity
- Time: Near `O((n + m) * alpha(n))`, where `m` is number of edges
- Space: `O(n)`

## Edge Cases / Traps

- Isolated nodes must still count as components
- Only decrement the counter when union succeeds
- Do not build component count from raw parent array without final root compression
- Node labels may be `0`-indexed here, unlike some DSU problems that are `1`-indexed

## Why This Works

Initially, every node forms its own component. A successful union means two separate components became one, so the count decreases by exactly one. Failed unions happen when an edge stays within an existing component, so the count stays unchanged. Repeating this across all edges yields the final component total.

## Interview Explanation

I start with `n` components because every node is initially isolated. Then I process each edge with DSU. If the edge connects two different roots, I merge them and decrement the component count. If both endpoints already share a root, that edge does not reduce the number of components. The final count is the answer.

## Similar Problems

- Union-Find Basics
- DSU Method for Redundant Connection
- Number of Provinces

## Anki Recall Prompts

- What is the initial component count in DSU?
- When exactly do we decrement the count?
- Why should failed unions not change the answer?
