# Problem: Shortest Cycle in a Graph

## Source
- Platform: LeetCode
- Problem Number: 2608
- Topic: Graph / BFS
- Tags: Graph, BFS, Shortest Path
- Difficulty: Hard
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Given a graph with n nodes and m edges, find the length of the shortest cycle in the graph. If there is no cycle, return -1.

## Recognition Pattern

- Undirected graph, possibly disconnected.
- Need shortest cycle length.
- BFS from each node, track distances, detect cycles via back edges.
- Cycle length = distance to parent + 1 when back edge found.

## Brute Force Thought

Enumerate all possible cycles, find minimum length.

Why it is too slow:
- Exponential time, impossible for large graphs.

## Core Insight

For each node, run BFS. When visiting a neighbor that's already visited and not parent, a cycle is found. Length = dist[current] + dist[neighbor] + 1. Track minimum.

## Solution Approach

1. Build adjacency list.
2. For each node as start:
   - BFS: queue, dist[], parent[].
   - For each neighbor, if not visited, set dist and parent, enqueue.
   - If visited and not parent, cycle length = dist[current] + dist[neighbor] + 1.
3. Return minimum cycle length or -1.

## Thought Process During Solving

1. What makes brute-force slow? Enumerating all cycles.
2. Which data structure fixes it? BFS with distance tracking.
3. What edge case breaks it? No cycles, disconnected graph, self-loops.
4. Can I explain in 3-4 sentences? Run BFS from each node. When finding a back edge to a visited node that's not parent, calculate cycle length using distances. Keep the minimum across all such cycles.

## Java Solution
```java
import java.util.*;

class Solution {
    public int shortestCycle(int n, int[][] edges) {
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }

        int minCycle = Integer.MAX_VALUE;

        for (int start = 0; start < n; start++) {
            int[] dist = new int[n];
            Arrays.fill(dist, -1);
            int[] parent = new int[n];
            Arrays.fill(parent, -1);

            Queue<Integer> queue = new LinkedList<>();
            queue.offer(start);
            dist[start] = 0;

            while (!queue.isEmpty()) {
                int current = queue.poll();

                for (int neighbor : graph[current]) {
                    if (dist[neighbor] == -1) {
                        dist[neighbor] = dist[current] + 1;
                        parent[neighbor] = current;
                        queue.offer(neighbor);
                    } else if (neighbor != parent[current]) {
                        // Cycle found
                        int cycleLen = dist[current] + dist[neighbor] + 1;
                        minCycle = Math.min(minCycle, cycleLen);
                    }
                }
            }
        }

        return minCycle == Integer.MAX_VALUE ? -1 : minCycle;
    }
}
```

## Complexity
- Time: `O(n * (n + m))`
- Space: `O(n + m)`

## Edge Cases / Traps

- No cycles: return -1
- Graph with self-loops: cycle of length 1
- Disconnected components: check all
- Multiple cycles: find shortest
- n=1: no cycle

## Why This Works

BFS finds shortest paths. When a back edge is found to a visited node, the cycle length is computed using the distances from the start node.

## Interview Explanation

We perform BFS from each node. During BFS, if we encounter a neighbor that is already visited and not the parent, we've found a cycle. The length is the sum of distances from the start to both nodes plus one. We track the minimum such length.

## Similar Problems

- Detect Cycle in Graph
- Minimum Cycle Length
- Graph Diameter

## Anki Recall Prompts

- How does BFS detect cycles?
- What is the cycle length formula?
- Why run BFS from every node?</content>
<parameter name="filePath">d:\work\dsaprep\dsa-java-revision\problems\shortest-cycle-in-a-graph.md