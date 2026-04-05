# Quick Revision Checklist

Use this before interviews or timed practice.

## For Any Problem

1. Restate the problem in one sentence.
2. Identify the likely pattern from constraints and input shape.
3. Say the brute force approach first.
4. Explain why it is too slow.
5. State the invariant or state you will maintain.
6. Dry run on a tiny example.
7. Check empty input, duplicates, bounds, and overflow.

## Java-Specific Sanity Checks

1. Did I pick the right structure: `HashMap`, `HashSet`, `ArrayDeque`, `PriorityQueue`, array?
2. Am I using `ArrayDeque` instead of legacy `Stack` unless forced otherwise?
3. Did I avoid integer overflow in sums, products, or `mid` computation?
4. If recursion is deep, should I mention iterative DFS/BFS as an alternative?
5. Is my comparator direction correct for heaps and sorting?

## Pattern Triggers

- Hashing: lookup or complement checks in linear time
- Sliding window: contiguous range with validity constraint
- Stack: nested structure, nearest greater/smaller, undo order
- Binary search: sorted answer space or sorted structure
- DFS/BFS: connectivity, components, shortest path in unweighted graph
- Backtracking: generate all valid choices with pruning
- DP: repeated subproblems with state transition
- Trie: prefix-heavy string search
- DSU: repeated connectivity / merging queries
