# Heap and Priority Queue

## Recognition Cues

- Need repeated access to current smallest or largest
- Top K queries
- Streaming updates
- Dijkstra / best-next-state expansions

## Core Java Reminder

`PriorityQueue` is a min-heap by default.

For a max-heap:

```java
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
```

## Common Mistakes

- Using full sort when only Top K is needed
- Forgetting heap size control in min-heap-of-size-k problems
- Comparator direction reversed
