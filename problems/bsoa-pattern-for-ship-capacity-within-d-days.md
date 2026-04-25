# Problem: BSOA pattern for Ship Capacity Within D Days

## Source
- Platform: Anki deck seed
- Topic: Binary Search
- Tags: BinarySearch, AnswerSearch, Capacity, Day25
- Difficulty: Medium
- Revision Status: Complete
- Tier: Tier 1

## Problem Cue

What is the BSOA pattern for Ship Capacity Within D Days?

## Detailed Problem Statement

A conveyor belt with packages that need to be shipped is loaded in the order they appear on the belt. The ship must transport all packages from the belt within D days. The i-th package on the belt has a weight of `weights[i]`. Each day, the ship loads packages on the belt (in the order they appear). The loaded packages must add up to a weight that is at most the ship's capacity. The ship will not load more than its capacity and some packages may not be shipped.

Find the least weight capacity of the ship so that all the packages can be shipped within D days.

**Example:**
- Input: `weights = [1,2,3,4,5,6,7,8,9,10]`, `days = 5`
- Output: `15`
- Explanation: With capacity 15, the ship loads `[1,2,3,4,5]` (day 1), `[6,7]` (day 2), `[8]` (day 3), `[9]` (day 4), `[10]` (day 5). Total: 5 days.

**Constraints:**
- `1 <= weights.length <= 50000`
- `1 <= weights[i] <= 500`
- `1 <= days <= weights.length`

## Core Insight (BSOA Pattern)

The key is to recognize this as a **binary search on the answer** problem:
1. **Left boundary (min capacity):** Must be at least the maximum weight (no package can be split)
2. **Right boundary (max capacity):** Sum of all weights (ship everything in one day)
3. **Feasibility check:** Given a capacity, simulate the shipping and count required days
4. **Binary search:** If we can ship within D days with capacity `mid`, try smaller capacity; otherwise, try larger

The pattern: `l = max(weights)`, `r = sum(weights)`. Simulate days using capacity `mid`. If `days <= D`: reduce capacity (search left). Else: increase capacity (search right).

## Solution Approach

**Step 1:** Identify the search space
- Minimum capacity = maximum single weight (lower bound)
- Maximum capacity = sum of all weights (upper bound)

**Step 2:** Define the feasibility check
- Given a capacity, how many days are needed?
- Greedily pack packages: load packages until adding the next would exceed capacity, then move to next day

**Step 3:** Binary search on capacity
- While left < right:
  - Calculate mid capacity
  - Check if we can ship all packages in D days with mid capacity
  - If yes, search left (smaller capacity is possible)
  - If no, search right (need larger capacity)

**Step 4:** Return the minimum valid capacity

## Brute-Force vs Optimized

**Brute-Force (Linear Search):**
- Try every capacity from `max(weights)` to `sum(weights)`
- For each, simulate shipping and count days
- Time: O(n × sum(weights)) - extremely slow for large sums

**Optimized (Binary Search):**
- Binary search reduces capacity checks from O(sum) to O(log(sum))
- Each simulation is O(n)
- Total: O(n × log(sum)) - dramatically faster

## Java Implementation

```java
class Solution {
    public int shipWithinDays(int[] weights, int days) {
        // Binary search on the answer: ship capacity
        int left = 0, right = 0;
        
        // Find boundaries: left = max weight, right = sum of all weights
        for (int w : weights) {
            left = Math.max(left, w);  // Min capacity must fit heaviest package
            right += w;                 // Max capacity is shipping everything in 1 day
        }
        
        // Binary search for minimum capacity
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            // Check if we can ship all packages in 'days' with capacity 'mid'
            if (canShipInDays(weights, mid, days)) {
                // If possible, try smaller capacity
                right = mid;
            } else {
                // If not possible, need larger capacity
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    /**
     * Helper: Simulate shipping and check if all packages fit within given days
     * @param weights array of package weights
     * @param capacity ship capacity per day
     * @param days maximum days allowed
     * @return true if all packages can be shipped within days, false otherwise
     */
    private boolean canShipInDays(int[] weights, int capacity, int days) {
        int daysNeeded = 1;
        int currentLoad = 0;
        
        for (int w : weights) {
            if (currentLoad + w > capacity) {
                // Can't fit this package today, move to next day
                daysNeeded++;
                currentLoad = w;
                
                // If we've exceeded the allowed days, it's impossible
                if (daysNeeded > days) {
                    return false;
                }
            } else {
                currentLoad += w;
            }
        }
        
        return true;
    }
}
```

## Complexity Analysis

**Time Complexity:** O(n × log(sum))
- Binary search on capacity: O(log(sum)) where sum is sum of all weights
- Feasibility check (canShipInDays): O(n) to iterate through all packages
- Total: O(n × log(sum))

**Space Complexity:** O(1)
- Only using a constant amount of extra space (left, right, mid, counters)

## Edge Cases and Traps

### Trap 1: Minimum Capacity Boundary
- **Mistake:** Setting `left = 0`
- **Fix:** `left = max(weights)` because no package can be split; we must have capacity for the heaviest package
- **Example:** `weights = [5]`, even with 1 day, capacity must be ≥ 5

### Trap 2: Greedy Packing May Fail with Wrong Capacity
- **Mistake:** Assuming "pack as much as possible each day" works with arbitrary capacity
- **Fix:** It does work! Greedy packing is optimal here because packages must be shipped in order
- **Proof:** There's no benefit to leaving space for a lighter future package if a heavier one fits

### Trap 3: Off-by-One in Days Count
- **Mistake:** Starting `daysNeeded = 0` instead of `1`
- **Fix:** Start with `daysNeeded = 1` because shipping at least one package requires 1 day
- **Example:** `weights = [1,2,3]`, capacity = 6 → all fit day 1, should return 1, not 0

### Trap 4: Integer Overflow in Sum
- **Mistake:** Using `int` for `right = sum(weights)` when sum is large
- **Fix:** With constraints (max 50k packages × 500 weight = 25M), `int` is safe. Still good practice to check constraints
- **Prevention:** Note the constraint limits before implementing

## When to Apply This Pattern

✅ **Use BSOA (Binary Search on Answer) when:**
- You need to find a value (capacity, time, speed, etc.)
- There's a clear feasibility test: "Is X sufficient?"
- Increasing the value always makes the problem easier (monotonic property)
- The answer space is continuous or can be treated as ordered range

✅ **This exact pattern applies to:**
- Minimum capacity problems (ship, painter, etc.)
- Minimum time problems (cow racing, tasks)
- Minimum speed problems (slowest fastest)
- Allocation/distribution optimization

## Key Takeaways

1. **BSOA = Binary Search + Feasibility Check:** The pattern is (1) define boundaries, (2) simulate feasibility, (3) binary search
2. **Greedy Feasibility:** In order-dependent shipping, greedy packing is optimal
3. **Space:** Left=max(single), Right=sum(all) - covers entire solution space
4. **Monotonicity:** Larger capacity always allows shipping in ≤ same number of days
