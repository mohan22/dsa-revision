# Problem: Kth Largest Element in an Array

## Source
- Platform: LeetCode
- Topic: Heap / Priority Queue
- Tags: Heap, KthLargest
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 2

## Problem Cue

Find the `k`th largest value without fully sorting the array.

## Recognition Pattern

- Need only one ranked element
- Top K style selection problem
- Min-heap of size `k` is the clean interview solution

## Core Insight

Keep only the `k` largest values seen so far. The smallest of those `k` values is the answer, so it lives at the root of a min-heap.

## Solution Approach

1. Create a min-heap.
2. Push each number.
3. If heap size exceeds `k`, pop the smallest.
4. Heap root is the `k`th largest at the end.

## Thought Process During Solving

1. Do I need full ordering? No.
2. What subset should I preserve? The best `k` elements so far.
3. Which element should be easiest to evict? The smallest among those `k`.

## Java Skeleton
```java
class Solution {
    public int findKthLargest(int[] nums, int k) {
        return 0;
    }
}
```

## Complexity
- Time: `O(n log k)`
- Space: `O(k)`

## Edge Cases / Traps

- Mixing up min-heap and max-heap roles
- `k = 1`
- duplicates are allowed and still count

## Promotion Checklist

- Add final Java min-heap solution
- Add optional quickselect note as a follow-up
