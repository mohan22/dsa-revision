# Problem: Monotonic deque solution for sliding window max

## Source
- Platform: Anki deck seed
- Topic: Two Pointers / Sliding Window
- Tags: SlidingWindow, Deque, Day23
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Statement

Given an integer array and a sliding window size `k`, the objective is to compute the maximum value in every window of length `k` as the window moves from left to right across the array. The monotonic deque solution maintains candidate indices in a way that keeps the current maximum at the front, allowing each window's maximum to be found in amortized O(1) time per element.

The input typically includes:
- `nums[]`: the array of values,
- `k`: the window size.

The algorithm should:
- process each element exactly once,
- keep only indices of values that can still be the maximum in the active window,
- discard outdated indices that fall outside the window range,
- output one maximum value for each valid window.

This pattern is essential for sliding-window problems where you need a running extreme value and brute-force scanning of each window is too slow.

## Recognition Pattern

- Topic signal: Two Pointers / Sliding Window
- Pattern hint from tags: SlidingWindow / Deque
- Key signal: need running max/min across overlapping subarrays
- Tier 1 note: know the invariant that deque stores indices in decreasing order of values and within the current window range

## Brute Force Thought

A brute-force solution checks all `k` values for each window, resulting in O(nk) time. The optimized monotonic deque pattern avoids repeated scans by preserving only useful candidates, reducing work to O(n).

## Core Insight

Use a deque of indices such that:
- values at deque indices are in decreasing order,
- the front is always the maximum for the current window,
- when adding a new element, remove smaller values from the back,
- when sliding the window, remove the front index if it leaves the window.

## Solution Approach

1. Initialize an empty deque for indices and an output list.
2. Iterate through the array with index `i`.
3. Remove indices from the front if they are out of the window range `i - k`.
4. Remove indices from the back while `nums[i]` is greater than or equal to `nums[deque.back()]`.
5. Add the current index `i` to the back.
6. Starting at `i >= k - 1`, append `nums[deque.front()]` to the result.

## Thought Process During Solving

1. Why is scanning each window directly too slow?
2. What invariant does the deque maintain to keep the maximum accessible?
3. How do you handle the first `k - 1` elements before the first full window?
4. What happens when the outgoing index is currently the maximum?

## Java Skeleton
```java
class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || k <= 0) {
            return new int[0];
        }

        int n = nums.length;
        int[] answer = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }

            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }

            deque.offerLast(i);

            if (i >= k - 1) {
                answer[i - k + 1] = nums[deque.peekFirst()];
            }
        }

        return answer;
    }
}
```

## Complexity
- Time: O(n)
- Space: O(k)

## Edge Cases / Traps

- `k` may be 1, which should return the original array.
- `k` may equal `nums.length`, producing a single maximum.
- Handle empty or null arrays gracefully.
- Ensure indices, not values, are stored in the deque.

## Why This Works

The deque always stores indices of elements in decreasing order of value. When the window moves, expired indices are removed from the front and smaller values are dropped from the back, guaranteeing the front index points to the current window maximum.

## Interview Explanation

Keep a deque of useful element indices so the window maximum is always at the front. Update it on each step by removing outdated or smaller elements before adding the new one.

## Similar Problems

- Sliding window minimum using monotonic deque
- Longest subarray with limit on difference
- Max of all subarrays of size k

## Anki Recall Prompts

- Why does the deque store indices instead of values?
- How does the deque maintain a decreasing order of values?
- What two removals are needed at each step?

