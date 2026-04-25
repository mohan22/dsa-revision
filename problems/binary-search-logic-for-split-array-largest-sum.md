# Problem: Binary search logic for Split Array Largest Sum

## Source
- Platform: Anki deck seed
- Topic: Binary Search
- Tags: BinarySearch, AnswerSearch, SplitArray, Day25
- Difficulty: Medium
- Revision Status: Complete
- Tier: Tier 1

## Problem Cue

What is the binary search on answer strategy for Split Array Largest Sum?

## Detailed Problem Statement

Given an integer array `nums` and an integer `m`, split the array into `m` non-empty continuous subarrays.
Minimize the largest sum among these subarrays, and return that minimum possible largest sum.

The array must be split in order, and every element must belong to exactly one subarray.

**Example 1:**
- Input: `nums = [7,2,5,10,8]`, `m = 2`
- Output: `18`
- Explanation: Split into `[7,2,5]` and `[10,8]`; largest sum is `18`.

**Example 2:**
- Input: `nums = [1,2,3,4,5]`, `m = 2`
- Output: `9`
- Explanation: Split into `[1,2,3,4]` and `[5]` or `[1,2,3]` and `[4,5]`.

**Example 3:**
- Input: `nums = [1,4,4]`, `m = 3`
- Output: `4`
- Explanation: Each subarray contains one element.

**Constraints:**
- `1 <= nums.length <= 1000`
- `1 <= nums[i] <= 10^6`
- `1 <= m <= nums.length`

## Recognition Pattern

- Topic signal: Binary Search
- Pattern hint: `AnswerSearch`, `SplitArray`
- The problem asks for the minimum feasible value that satisfies a monotonic condition
- This is a classic binary search-on-answer problem where feasibility is checked by greedily forming subarrays

## Core Insight

The minimum possible largest subarray sum lies between:
- `left = max(nums)` (one subarray must contain the largest element)
- `right = sum(nums)` (one subarray contains the whole array)

Define a feasibility function `canSplit(nums, m, cap)` that returns whether the array can be split into at most `m` subarrays such that no subarray sum exceeds `cap`.

If `canSplit(mid)` is true, we can try a smaller `mid`.
If false, we need a larger `mid`.

## Solution Approach

1. Compute `left` and `right` from the input array
2. Binary search while `left < right`
3. For each candidate `mid`, greedily partition the array into subarrays with sums ≤ `mid`
4. If the number of required subarrays is ≤ `m`, the candidate is feasible
5. Adjust search bounds until the smallest feasible capacity remains

## Brute-Force vs Optimized

**Brute-Force:** Test every candidate largest sum in the range `[max(nums), sum(nums)]`, partitioning the array for each candidate. Time is O(n × sum(nums)), which is not feasible for large sums.

**Optimized:** Binary search over that answer space, using O(log(sum)) candidate checks. Each check is O(n), so overall time is O(n × log(sum(nums))).

## Java Implementation

```java
class Solution {
    public int splitArray(int[] nums, int m) {
        int left = 0;
        int right = 0;

        for (int num : nums) {
            left = Math.max(left, num);
            right += num;
        }

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (canSplit(nums, m, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    private boolean canSplit(int[] nums, int m, int cap) {
        int count = 1;
        int sum = 0;

        for (int num : nums) {
            if (sum + num > cap) {
                count++;
                sum = num;
                if (count > m) {
                    return false;
                }
            } else {
                sum += num;
            }
        }

        return true;
    }
}
```

## Complexity

- Time: O(n × log(sum(nums)))
  - Binary search over the candidate largest sums
  - Feasibility check scans the array in O(n)
- Space: O(1)
  - Only a few variables are used besides the input

## Edge Cases / Traps

- `m == 1`: the answer is `sum(nums)` because the whole array is one subarray
- `m == nums.length`: the answer is `max(nums)` because every element can be its own subarray
- Off-by-one in binary search: use `right = mid` when feasible, not `mid - 1`
- In the feasibility check, always start `count = 1` because one subarray is required
- Avoid overflow in `mid` computation by using `left + (right - left) / 2`

## Key Takeaways

- Split Array Largest Sum is solved with binary search on the answer space
- Feasibility is checked by greedy partitioning into subarrays with bounded sums
- The search space is defined by `[max(nums), sum(nums)]`
- The final answer is the minimum `cap` for which the array can be split into at most `m` subarrays
