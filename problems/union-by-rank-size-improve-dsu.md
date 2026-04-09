# Problem: Union-by-Rank/Size Improves DSU

## Source
- Platform: DSU concept / implementation pattern
- Topic: DSU
- Tags: DSU, Rank
- Difficulty: Core Pattern
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Use union-by-rank or union-by-size when implementing DSU so repeated unions do not create tall trees.

## Brief Problem Statement

You are implementing Disjoint Set Union with repeated `find` and `union` operations. Improve the DSU so the internal trees stay shallow, making future operations faster.

## Recognition Pattern

- DSU / Union-Find implementation detail
- Many union operations over time
- Performance depends on keeping component trees shallow
- Usually paired with path compression

## Brute Force Thought

Always attach one root to the other without considering tree shape.

Why it is too slow:
- can create tall chains
- makes `find` slower before path compression has enough effect
- performance degrades on adversarial union order

## Core Insight

When merging two components, attach the smaller or shallower tree under the larger or deeper one. That prevents unnecessary height growth and keeps `find` operations fast.

## Solution Approach

1. Each node starts as its own parent.
2. Track either:
   - `rank`: approximate tree height, or
   - `size`: number of nodes in the component
3. On `union(x, y)`:
   - find both roots
   - if roots match, do nothing
   - otherwise attach the lower-rank tree under the higher-rank tree
   - if ranks are equal, choose one root and increment its rank
4. Combine this with path compression inside `find` for near-constant amortized time.

## Thought Process During Solving

1. What makes naive DSU slow? Tall parent chains.
2. What should union try to avoid? Increasing tree height unnecessarily.
3. What metadata helps me choose the better root? Rank or size.
4. Why is this usually taught with path compression? The two optimizations work together.
5. What is the key invariant? Parents always form rooted trees, and smaller/shallower trees get attached underneath larger/deeper ones.

## Java Solution
```java
class DSU {
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
```

## Complexity
- Time: Near `O(alpha(n))` amortized per operation when combined with path compression
- Space: `O(n)`

## Edge Cases / Traps

- Do not compare raw nodes; compare their roots
- Only increase rank when both ranks are equal
- Rank is not the exact current height after path compression, and that is fine
- If using size instead of rank, remember to update the new root's size after union

## Why This Works

Union-by-rank/size limits how quickly tree height can grow. Instead of arbitrarily attaching one root under another, it makes the structurally safer choice each time. Combined with path compression, this keeps DSU operations extremely fast in practice and near-constant amortized in theory.

## Interview Explanation

A naive DSU can become a long chain if unions happen in a bad order. Union-by-rank or size fixes that by always attaching the smaller or shallower tree under the larger or deeper one. That keeps trees flat, and when path compression is added on top, `find` and `union` become almost constant time amortized.

## Similar Problems

- Union-Find Basics
- Weighted DSU for Ratio Problems
- DSU Method for Redundant Connection

## Anki Recall Prompts

- Why does naive union make DSU slower?
- When do we increment rank?
- Why are union-by-rank and path compression usually taught together?
