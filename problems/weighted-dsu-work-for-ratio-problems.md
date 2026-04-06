# Problem: Weighted DSU for Ratio Problems

## Source
- Platform: Pattern / Evaluate Division style problems
- Topic: DSU
- Tags: DSU, Weighted, Ratios
- Difficulty: Medium-Hard Pattern
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Use DSU when variables belong to connected components and each edge gives a multiplicative ratio like `a / b = value`.

## Recognition Pattern

- Variables are connected by equations or ratios.
- Queries ask for a derived ratio between two nodes.
- Connectivity alone is not enough; we also need relative weight information inside each component.

## Brute Force Thought

Build a graph and run DFS/BFS for every query to multiply edge weights along a path.

Why it is too slow:
- repeated traversal for many queries
- recomputes the same component relationships again and again

## Core Insight

Weighted DSU stores both connectivity and relative ratios to the parent.

If `weight[x] = x / parent[x]`, then after path compression we can make `weight[x] = x / root`. That lets us answer:

`x / y = (x / root) / (y / root)`

whenever `x` and `y` share the same root.

## Solution Approach

1. Map every string variable to an integer id.
2. Store:
   - `parent[x]`: representative parent
   - `weight[x]`: ratio `x / parent[x]`
3. `find(x)` returns the root and compresses the path while updating `weight[x]` to mean `x / root`.
4. To union equation `a / b = value`:
   - find roots of `a` and `b`
   - if different, attach one root under the other
   - compute the new root weight so the given equation remains true
5. To answer query `a / b`:
   - if either variable is unknown, answer `-1`
   - if roots differ, answer `-1`
   - otherwise return `weight[a] / weight[b]`

## Thought Process During Solving

1. Are we only checking connectivity? No, we need exact ratios too.
2. What extra state should DSU carry? Relative weight to parent/root.
3. What should `weight[x]` mean? Pick one invariant and keep it consistent.
4. Why does path compression still work? Because we multiply weights while skipping intermediate parents.
5. How do we derive the union formula? Write the equation in terms of both roots and solve algebraically.

## Java Solution
```java
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Solution {
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Integer> idByName = new HashMap<>();

        int nextId = 0;
        for (List<String> equation : equations) {
            for (String variable : equation) {
                if (!idByName.containsKey(variable)) {
                    idByName.put(variable, nextId++);
                }
            }
        }

        WeightedDSU dsu = new WeightedDSU(nextId);

        for (int i = 0; i < equations.size(); i++) {
            String numerator = equations.get(i).get(0);
            String denominator = equations.get(i).get(1);
            dsu.union(idByName.get(numerator), idByName.get(denominator), values[i]);
        }

        double[] answer = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String numerator = queries.get(i).get(0);
            String denominator = queries.get(i).get(1);

            if (!idByName.containsKey(numerator) || !idByName.containsKey(denominator)) {
                answer[i] = -1.0;
                continue;
            }

            int x = idByName.get(numerator);
            int y = idByName.get(denominator);

            if (dsu.find(x) != dsu.find(y)) {
                answer[i] = -1.0;
            } else {
                answer[i] = dsu.weight[x] / dsu.weight[y];
            }
        }

        return answer;
    }

    private static class WeightedDSU {
        private final int[] parent;
        private final int[] rank;
        private final double[] weight;

        WeightedDSU(int n) {
            parent = new int[n];
            rank = new int[n];
            weight = new double[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                weight[i] = 1.0;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                int originalParent = parent[x];
                parent[x] = find(parent[x]);
                weight[x] *= weight[originalParent];
            }
            return parent[x];
        }

        void union(int x, int y, double ratio) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return;
            }

            double weightX = weight[x];
            double weightY = weight[y];

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                weight[rootX] = ratio * weightY / weightX;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
                weight[rootY] = weightX / (ratio * weightY);
            } else {
                parent[rootY] = rootX;
                weight[rootY] = weightX / (ratio * weightY);
                rank[rootX]++;
            }
        }
    }
}
```

## Complexity
- Time: Near `O(1)` amortized per union/find, so roughly `O((E + Q) * alpha(n))`
- Space: `O(n)`

## Edge Cases / Traps

- Query variable not seen in any equation
- Query across different connected components
- Using inconsistent meaning for `weight[x]`
- Forgetting that path compression must also update weights
- Division direction mistakes in the union formula

## Why This Works

The DSU invariant is: after `find(x)`, `weight[x]` equals `x / root(x)`. If two variables share a root, their ratio is just the quotient of those two root-relative values. During union, we solve for the one new root-edge weight that makes the given equation true across both components. Path compression preserves correctness because multiplying along the parent chain composes ratios exactly.

## Interview Explanation

This is DSU plus extra numeric state. Normal union-find tells me whether two variables are connected, but not their ratio. So I store `weight[x] = x / parent[x]`. After path compression, that becomes `x / root`, which makes query answering easy: if two nodes share the same root, divide their weights. The only tricky part is the union formula, which comes from writing the equation in terms of both roots and solving for the new parent edge.

## Similar Problems

- Evaluate Division
- Union-Find Basics
- Count Connected Components using DSU

## Anki Recall Prompts

- What does `weight[x]` mean in weighted DSU?
- After path compression, what ratio does `weight[x]` store?
- How do we answer `x / y` once both nodes share the same root?
