# Problem: Top K Frequent Elements

## Source
- Platform: LeetCode
- Topic: Heap / Priority Queue
- Tags: Heap, TopK, Frequency
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 2

## Problem Cue

Return the `k` most frequent elements in the array.


## Brief Problem Statement

Return the `k` most frequent elements in the array.

## Recognition Pattern

- Need frequency counting plus repeated best selection
- Top K is usually heap or bucket-sort territory

## Core Insight

Count frequencies first, then either:
- keep a min-heap of size `k`, or
- bucket by frequency for near-linear extraction

## Solution Approach

1. Build `value -> frequency`.
2. Choose implementation:
   - interview default: min-heap of size `k`
   - faster asymptotically: bucket sort when needed
3. Extract final `k` values.

## Thought Process During Solving

1. Is full sort necessary? Usually no.
2. Do I only need the top `k`, not full ranking? Yes.
3. What do I store in the heap? `(frequency, value)`.
4. When is bucket sort worth remembering? When frequency range is bounded by `n`.

## Java Skeleton
```java
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        return new int[0];
    }
}
```

## Complexity
- Time: `O(n log k)` with min-heap, or `O(n)` with buckets
- Space: `O(n)`

## Edge Cases / Traps

- Comparator direction
- Extracting heap into answer in arbitrary order
- Forgetting that bucket index is frequency

## Promotion Checklist

- Add one full min-heap solution
- Add one short note on why bucket sort can be better here
- Add Java `PriorityQueue<int[]>` comparator reminder
