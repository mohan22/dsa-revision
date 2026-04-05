# Problem: Union-Find Basics

## Source
- Platform: Pattern / Concept
- Topic: DSU
- Tags: DSU, PathCompression
- Difficulty: Medium Pattern
- Revision Status: New
- Tier: Tier 2

## Problem Cue

Maintain connected components under repeated union and connectivity checks.

## Recognition Pattern

- Repeated "are these connected?" queries
- Many merges over time
- Need near-constant amortized operations

## Core Insight

Store each component as a rooted tree. `find` returns the representative root, path compression flattens the structure, and union-by-rank keeps trees shallow.

## Solution Approach

1. Initialize `parent[i] = i`.
2. `find(x)` climbs to the root and compresses the path.
3. `union(x, y)` merges the roots if they differ.
4. Use rank or size to attach the smaller tree under the larger one.

## Thought Process During Solving

1. Is traversal enough? Maybe for one query, but not for many online merges.
2. What makes DSU fast? Path compression + union by rank.
3. What indicates a successful merge? Roots were different.

## Java Skeleton
```java
class DSU {
    private final int[] parent;
    private final int[] rank;

    DSU(int n) {
        parent = new int[n];
        rank = new int[n];
    }
}
```

## Complexity
- Time: Nearly `O(1)` amortized per operation
- Space: `O(n)`

## Edge Cases / Traps

- Forgetting to initialize each node as its own parent
- Updating rank incorrectly
- Unioning raw nodes instead of their roots

## Promotion Checklist

- Add final `find` and `union` Java implementation
- Add one example problem such as Number of Provinces or Redundant Connection
