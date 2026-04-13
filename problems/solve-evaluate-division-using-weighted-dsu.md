# Problem: Solve Evaluate Division Using Weighted DSU

## Source
- Platform: LeetCode
- Topic: DSU
- Tags: DSU, Weighted, Ratios
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Use weighted DSU when equations define multiplicative ratios between variables and queries ask for the derived ratio between two variables.

## Brief Problem Statement

You are given equations such as `a / b = 2.0` and queries asking for ratios like `a / c`. Return the value of each query if it can be determined from the equations; otherwise return `-1.0`.

## Recognition Pattern

- Variables connected by ratio equations
- Need many ratio queries after processing relationships
- Connectivity alone is not enough; relative weight within each component matters
- Weighted DSU is a clean alternative to rebuilding paths for every query

## Brute Force Thought

Build a graph and run DFS or BFS for every query to multiply edge ratios along a path.

Why it is too slow:
- repeated traversal for many queries
- recomputes the same connected-component relationships again and again
- less efficient once queries become frequent

## Core Insight

Store both connectivity and relative ratios in DSU.

Let `weight[x] = x / parent[x]`. After path compression, `weight[x]` becomes `x / root`. If two variables share the same root, then:

`x / y = (x / root) / (y / root)`

That makes each query easy after unions are built.

## Solution Approach

1. Map every distinct variable string to an integer id.
2. Initialize weighted DSU over those ids.
3. Maintain this invariant:
   - `weight[x] = x / parent[x]`
   - after path compression, `weight[x] = x / root(x)`
4. For an equation `x / y = ratio`:
   - let `rootX = find(x)` and `rootY = find(y)`
   - after `find`, we know:
     - `weight[x] = x / rootX`
     - `weight[y] = y / rootY`
   - so:
     - `ratio = x / y = (x / rootX) * (rootX / rootY) / (y / rootY)`
     - `ratio = weight[x] * (rootX / rootY) / weight[y]`
5. If we attach `rootX` under `rootY`, we need:
   - `weight[rootX] = rootX / rootY = ratio * weight[y] / weight[x]`
6. If we attach `rootY` under `rootX`, we need:
   - `weight[rootY] = rootY / rootX = weight[x] / (ratio * weight[y])`
7. During path compression, when `x` skips its old parent and points directly to the root, multiply weights along the path so `weight[x]` stays equal to `x / root`.
8. For each query:
   - if either variable is unknown, answer `-1.0`
   - if roots differ, answer `-1.0`
   - otherwise return `weight[x] / weight[y]`

## Thought Process During Solving

1. Why is plain DSU not enough? It tells me connectivity but not the actual ratio.
2. What extra invariant do I need? Relative weight from node to parent/root.
3. How do I answer a query once both variables share a root? Divide their root-relative weights.
4. What is the trickiest part? Getting the union formula correct when attaching one root under another.
5. Why does path compression still work? Because ratios compose multiplicatively along the parent chain.

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
- Time: Near `O((E + Q) * alpha(n))`
- Space: `O(n)`

## Edge Cases / Traps

- Query variable may not appear in any equation
- Variables can be disconnected even if both are known
- The meaning of `weight[x]` must stay consistent throughout the DSU
- Path compression must also update weights, not just parents
- Be careful with division direction in the union formula

## Why This Works

Weighted DSU keeps every variable attached to a component root together with its multiplicative ratio to that root. Once two variables are in the same component, their relative ratio is just the quotient of their root-relative weights. Union operations preserve the equation constraints, and path compression keeps the structure efficient without breaking the ratio invariant.

## Interview Explanation

I treat each variable as a DSU node, but unlike normal union-find, I also store a weight that represents the ratio to the parent. After path compression, that becomes the ratio to the root. Then for any query `x / y`, if `x` and `y` share a root, I divide their stored weights; otherwise the answer is `-1.0`. The main challenge is deriving the correct union formula so the given equation remains true after merging two components.

## Similar Problems

- Weighted DSU for Ratio Problems
- Union-Find Basics
- DSU Solves Accounts Merge

## Anki Recall Prompts

- What does `weight[x]` represent in weighted DSU?
- How do we answer `x / y` once both variables share a root?
- Why must path compression update weights too?
