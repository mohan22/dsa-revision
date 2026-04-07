# Problem: DSU Method for Redundant Connection

## Source
- Platform: LeetCode / Pattern
- Topic: DSU
- Tags: DSU, CycleDetection
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Use DSU when an undirected graph was originally a tree and then one extra edge was added, creating exactly one cycle.

## Brief Problem Statement

You are given an undirected graph with `n` nodes and `n` edges. The graph started as a tree, and then one extra edge was added. Return the edge that can be removed so the remaining graph becomes a tree again.

## Recognition Pattern

- Undirected graph
- Need to detect the first edge that closes a cycle
- Connectivity evolves incrementally as edges are processed
- DSU is the cleanest way to ask: "Were these nodes already connected before adding this edge?"

## Brute Force Thought

For each edge, temporarily remove it and check whether the remaining graph is still connected and acyclic.

Why it is too slow:
- repeated graph reconstruction
- repeated DFS/BFS checks over almost the whole graph

## Core Insight

In DSU, if the endpoints of a new edge already belong to the same component, then that edge must create a cycle. That edge is exactly the redundant connection we need to return.

## Solution Approach

1. Initialize DSU for nodes `1..n`.
2. Process edges in input order.
3. For each edge `[u, v]`:
   - if `find(u) == find(v)`, `u` and `v` are already connected, so this edge forms a cycle
   - return `[u, v]`
4. Otherwise union the two components and continue.

## Thought Process During Solving

1. What changed from a tree? Exactly one extra edge.
2. What does that extra edge do? It creates one cycle.
3. How can I detect a cycle online while processing edges? Check whether endpoints are already connected.
4. Which structure supports repeated connectivity checks fast? DSU.
5. Why return the current edge immediately? Because the first edge whose endpoints are already connected is the one closing the cycle under the chosen scan order.

## Java Solution
```java
class Solution {
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        DSU dsu = new DSU(n + 1);

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];

            if (!dsu.union(u, v)) {
                return edge;
            }
        }

        return new int[0];
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
- Time: Near `O(n * alpha(n))`
- Space: `O(n)`

## Edge Cases / Traps

- Nodes are 1-indexed in the standard problem
- Return the edge itself, not just a boolean
- Do not overcomplicate this with full graph traversal
- Redundant Connection II is different because the graph is directed and may have a two-parent conflict

## Why This Works

In a tree, every edge connects two previously separate components. The one extra edge is the only edge whose endpoints are already connected by an existing path. DSU tracks exactly these connected components, so the first failed union identifies the redundant edge.

## Interview Explanation

Since the graph was a tree plus one extra edge, there is exactly one cycle. I process edges one by one using DSU. If an edge tries to connect two nodes already in the same component, then they already had a path between them, so this new edge creates the cycle. That makes it the redundant connection.

## Similar Problems

- Union-Find Basics
- Count Connected Components using DSU
- Redundant Connection II

## Anki Recall Prompts

- What DSU event tells me an edge is redundant?
- Why does a failed union mean cycle edge in this problem?
- How is Redundant Connection II different?
