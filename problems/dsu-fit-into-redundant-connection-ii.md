# Problem: DSU Fit Into Redundant Connection II

## Source
- Platform: LeetCode / Pattern
- Topic: DSU
- Tags: DSU, Directed
- Difficulty: Hard
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Use DSU in Redundant Connection II when the graph is directed and the bad edge may come from either a cycle or a node having two parents.

## Brief Problem Statement

You are given a directed graph with `n` nodes and `n` edges. The graph was originally a rooted tree, and then one extra directed edge was added. Return the edge that can be removed so the remaining graph becomes a rooted tree again.

## Recognition Pattern

- Directed graph, not undirected
- Rooted-tree structure with exactly one extra edge
- Two different failure modes are possible:
  - a cycle exists
  - some node has two parents
- DSU still helps, but only after handling the two-parent conflict carefully

## Brute Force Thought

Try removing each edge one by one and test whether the remaining directed graph is a valid rooted tree.

Why it is too slow:
- repeated full validation work
- awkward to check both parent constraints and cycle constraints every time
- wastes the structural special case of "rooted tree + one extra edge"

## Core Insight

This problem splits into cases:

1. No node has two parents:
   - then it reduces to cycle detection, similar to Redundant Connection
2. A node has two parents:
   - there are two candidate incoming edges
   - temporarily ignore the later candidate and run DSU
   - if a cycle still exists, remove the earlier candidate
   - otherwise remove the later candidate

## Solution Approach

1. Scan edges and track each node's parent.
2. If some node gets a second parent, record:
   - `candidate1`: the earlier edge to that child
   - `candidate2`: the later edge to that child
3. Run DSU over all edges, but skip `candidate2` for now.
4. During DSU:
   - if union fails and there was no two-parent conflict, return the current edge
   - if union fails and there was a two-parent conflict, return `candidate1`
5. If no cycle happens when skipping `candidate2`, return `candidate2`.

## Thought Process During Solving

1. Why is this harder than Redundant Connection? Because the graph is directed, so a node may illegally have two parents even before thinking about cycles.
2. What should I detect first? Parent conflict, because it changes which edge should be tested.
3. Why skip the later candidate first? Because if that fixes the tree, it is the redundant edge; otherwise the earlier candidate is the real problem.
4. What is DSU still doing here? Detecting whether the remaining chosen edges form a cycle.
5. What makes this problem easy to mess up? Mixing up the "cycle only" case with the "two parents + cycle" case.

## Java Solution
```java
class Solution {
    public int[] findRedundantDirectedConnection(int[][] edges) {
        int n = edges.length;
        int[] directParent = new int[n + 1];
        int[] candidate1 = null;
        int[] candidate2 = null;

        for (int[] edge : edges) {
            int parent = edge[0];
            int child = edge[1];

            if (directParent[child] == 0) {
                directParent[child] = parent;
            } else {
                candidate1 = new int[] {directParent[child], child};
                candidate2 = new int[] {parent, child};
                edge[1] = 0;
            }
        }

        DSU dsu = new DSU(n + 1);

        for (int[] edge : edges) {
            int parent = edge[0];
            int child = edge[1];

            if (child == 0) {
                continue;
            }

            if (!dsu.union(parent, child)) {
                if (candidate1 == null) {
                    return edge;
                }
                return candidate1;
            }
        }

        return candidate2;
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

- Do not treat this exactly like undirected Redundant Connection
- A node having two parents is a separate violation from cycle creation
- Be careful which candidate edge is skipped during DSU
- If there is both a two-parent conflict and a cycle, the answer is the earlier candidate edge
- The in-place `edge[1] = 0` skip trick is common, but remember it mutates the input array

## Why This Works

A rooted tree must satisfy both conditions: every node except the root has exactly one parent, and there must be no cycle. The extra directed edge can break either or both. By first isolating the two-parent conflict and then using DSU to test whether the remaining structure still has a cycle, we can distinguish all valid cases and return the unique removable edge.

## Interview Explanation

This problem is harder than Redundant Connection because directed trees can fail in two ways: cycle, or a node having two parents. I first detect whether any node has two incoming edges. If yes, I keep both conflicting edges as candidates and temporarily skip the later one. Then I run DSU on the rest. If a cycle still appears, the earlier candidate must be removed; otherwise the later candidate is the redundant one. If there was never a two-parent conflict, it reduces to the usual cycle-detection case.

## Similar Problems

- DSU Method for Redundant Connection
- Union-Find Basics
- Count Connected Components Using DSU

## Anki Recall Prompts

- What are the two failure modes in Redundant Connection II?
- Why do we skip the later candidate edge first?
- When do we return `candidate1` instead of `candidate2`?
