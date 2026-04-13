# Problem: DSU pattern for Kruskal MST

## Source
- Platform: Anki deck seed
- Topic: DSU
- Tags: DSU, Kruskal, MST, Day22
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 2

## Problem Cue

What is the DSU pattern for Kruskal MST?

## Problem Statement

Given an undirected weighted graph, the objective is to find the minimum spanning tree (MST) that connects all vertices with the smallest possible total edge weight. The DSU pattern for Kruskal MST uses Disjoint Set Union to efficiently detect cycles while selecting edges in increasing order of weight.

The input usually consists of a list of weighted edges `(u, v, w)` and the number of vertices `n`. The algorithm must:

- Sort the edges by weight ascending,
- Iterate through the sorted edges,
- Use DSU to check if the current edge connects two previously disconnected components,
- If it does, add the edge to the MST and union the two sets,
- Stop when the MST contains `n - 1` edges or no more edges remain.

This pattern is crucial when the graph has many edges and the key challenge is cycle detection while maintaining connectivity.

## Recognition Pattern

- Topic signal: DSU, Union-Find, Kruskal
- Tags: DSU, Kruskal, MST
- Pattern hint: sort edges by weight, then build MST using union-find to avoid cycles
- Tier 1 note: understand the exact edge selection invariant and why DSU gives near-linear cycle checks

## Brute Force Thought

A brute-force MST would try all edge subsets or repeatedly search for the next valid edge with cycle detection by scanning visited paths. That is too slow for graphs with many edges. The optimized DSU/Kruskal pattern avoids repeated path checking by maintaining component representatives.

## Core Insight

Sort edges by weight. For each edge `(u, v, w)`, if `find(u) != find(v)`, then `union(u, v)` and add the edge to the MST. This ensures the MST remains acyclic and minimal.

## Solution Approach

1. Parse `n` and the edge list.
2. Sort edges by weight.
3. Initialize DSU for `n` vertices with path compression and union by rank/size.
4. Iterate through sorted edges:
   - If endpoints are in different components, union them and include the edge.
   - Track MST weight and edge count.
5. Stop after `n - 1` edges or when all edges are processed.
6. Return the MST weight or the list of edges if required.

## Thought Process During Solving

1. Why is cycle detection the key bottleneck in Kruskal?
2. How does DSU compress paths and keep future `find` calls fast?
3. What happens if the graph is disconnected?
4. How do we know `n - 1` edges is enough for the MST?

## Java Skeleton
```java
class Solution {
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        boolean union(int x, int y) {
            int rx = find(x);
            int ry = find(y);
            if (rx == ry) return false;
            if (size[rx] < size[ry]) {
                parent[rx] = ry;
                size[ry] += size[rx];
            } else {
                parent[ry] = rx;
                size[rx] += size[ry];
            }
            return true;
        }
    }

    public long kruskalMST(int n, int[][] edges) {
        Arrays.sort(edges, Comparator.comparingInt(a -> a[2]));
        DSU dsu = new DSU(n);
        long totalWeight = 0;
        int count = 0;

        for (int[] edge : edges) {
            if (dsu.union(edge[0], edge[1])) {
                totalWeight += edge[2];
                count++;
                if (count == n - 1) break;
            }
        }

        return count == n - 1 ? totalWeight : -1;
    }
}
```

## Complexity
- Time: O(E log E + E α(V))
- Space: O(V + E)

## Edge Cases / Traps

- Graph is disconnected, so MST may not exist.
- Edge list may contain duplicate edges or self-loops.
- Vertex indexing may be 0-based or 1-based.
- Ensure DSU path compression and union by size/rank for performance.

## Why This Works

Kruskal’s algorithm picks the smallest available edge that does not create a cycle. DSU efficiently tracks connected components and ensures each selected edge merges two distinct components, producing the minimum spanning tree.

## Interview Explanation

Sort all edges by weight, then add them one by one if they connect two different components. DSU handles cycle detection efficiently, so you can build the MST in near-linear time.

## Similar Problems

- Minimum Spanning Tree using Prim’s algorithm
- Reducing graph connectivity with DSU
- Detect cycle in an undirected graph with Union-Find

## Anki Recall Prompts

- Why does Kruskal use DSU instead of DFS-based cycle detection?
- What invariant does `find`/`union` maintain in Kruskal MST?
- How many edges does a spanning tree on `n` nodes have?
