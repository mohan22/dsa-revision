# Problem: Check If Graph Is a Valid Tree Using DSU

## Source
- Platform: LeetCode / Pattern
- Topic: DSU
- Tags: DSU, TreeCheck
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Use DSU when you need to verify whether an undirected graph forms exactly one connected, acyclic tree.

## Brief Problem Statement

You are given `n` nodes and a list of undirected edges. Determine whether the graph is a valid tree, meaning it is fully connected and contains no cycle.

## Recognition Pattern

- Undirected graph
- Need a yes/no tree check, not traversal order
- A valid tree must satisfy two conditions:
  - exactly `n - 1` edges
  - no cycles and fully connected
- DSU is a clean way to enforce the cycle-free condition while processing edges

## Brute Force Thought

Build the graph, run DFS/BFS to check connectivity, and also detect cycles separately.

Why it is slower or more cumbersome:
- requires explicit adjacency construction
- needs separate reasoning for connectivity and cycle detection
- DSU gives a compact check directly from the edge list

## Core Insight

An undirected graph is a tree if and only if:
- it has exactly `n - 1` edges, and
- no edge connects two nodes already in the same component

If the edge count is `n - 1` and DSU never sees a failed union, the graph must be connected and acyclic.

## Solution Approach

1. First check `edges.length == n - 1`.
   - if not, the graph cannot be a tree
2. Initialize DSU for all `n` nodes.
3. Process each edge `[u, v]`:
   - if `union(u, v)` fails, `u` and `v` were already connected, so the edge creates a cycle
   - return `false`
4. If all unions succeed and the edge count was `n - 1`, return `true`.

## Thought Process During Solving

1. What two properties define a tree? Connected and acyclic.
2. What quick structural shortcut helps immediately? A tree on `n` nodes must have exactly `n - 1` edges.
3. How do I detect cycles online? A failed DSU union.
4. Why don’t I need a separate connectivity traversal afterward? Because `n - 1` edges plus no cycle already implies connectivity.

## Java Solution
```java
class Solution {
    public boolean validTree(int n, int[][] edges) {
        if (edges.length != n - 1) {
            return false;
        }

        DSU dsu = new DSU(n);
        for (int[] edge : edges) {
            if (!dsu.union(edge[0], edge[1])) {
                return false;
            }
        }

        return true;
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

- If edge count is not `n - 1`, return `false` immediately
- A failed union means cycle in an undirected graph
- Do not forget that `n - 1` edges alone is not enough if a cycle exists
- Node labels are usually `0`-indexed in this version of the problem

## Why This Works

A tree with `n` nodes must have exactly `n - 1` edges. If an undirected graph with `n - 1` edges also has no cycle, then it cannot be disconnected; otherwise it would need fewer edges across separate components. DSU guarantees the no-cycle check by detecting whether an edge tries to join nodes already in the same component.

## Interview Explanation

I use the standard tree characterization: a valid tree must have exactly `n - 1` edges and must not contain a cycle. DSU lets me process edges one by one and detect cycles using failed unions. So I first check the edge count, then union every edge. If any union fails, there is a cycle, so the graph is not a tree. Otherwise it is a valid tree.

## Similar Problems

- Count Connected Components Using DSU
- DSU Method for Redundant Connection
- Number of Provinces

## Anki Recall Prompts

- What two conditions make an undirected graph a tree?
- Why is `edges.length == n - 1` necessary?
- Why does a failed union imply cycle here?
