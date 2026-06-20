# Problem: Find Minimum Coins To Make Up An Amount

## Source
- Platform: Anki deck seed
- Topic: Dynamic Programming
- Tags: DynamicProgramming, 1D, Day6
- Difficulty: Not labeled
- Tier: Tier 1

## Problem Description

**Coin Change (LeetCode 322)**

Given an integer array `coins` representing coin denominations and an integer `amount` representing a target amount of money, return the fewest number of coins that you need to make up that amount. If that amount of money cannot be made up by any combination of the coins, return `-1`.

**Constraints:**
- `1 <= coins.length <= 12`
- `1 <= coins[i] <= 2^31 - 1`
- `0 <= amount <= 10^4`

**Examples:**
- Input: `coins = [1,2,5]`, `amount = 5` → Output: `1` (one 5-coin)
- Input: `coins = [2]`, `amount = 3` → Output: `-1` (impossible)
- Input: `coins = [10]`, `amount = 10` → Output: `1` (one 10-coin)

## Recognition Pattern

- **Signal**: Optimization problem asking for minimum count/cost to reach a target value.
- **Classic DP setup**: Build up solutions from base case (amount = 0) to target amount.
- **Subproblem**: DP[i] = minimum coins needed to make amount i.

## Core Insight

**Bottom-up DP**: For each amount from 1 to target, try subtracting each coin and pick the minimum:
$$\text{DP}[i] = \min(\text{DP}[i], 1 + \text{DP}[i - \text{coin}])$$

Base case: DP[0] = 0 (zero coins needed for zero amount). All other amounts start at infinity.

## Solution Approach

1. **State definition**: DP[i] = minimum coins to make amount i.
2. **Base case**: DP[0] = 0 (no coins needed for amount 0).
3. **Transition**: For each amount i from 1 to target amount, try each coin. If coin ≤ i and DP[i - coin] is valid, update DP[i] = min(DP[i], 1 + DP[i - coin]).
4. **Answer**: DP[amount]. If it's still infinity, return -1.

## Thought Process During Solving

1. **Brute-force (recursive)**: Try each coin, recurse on remaining amount. Overlapping subproblems → exponential time.
2. **Why DP?**: Memoize results for each amount. Once we know the answer for amount i, we never recalculate it.
3. **Direction**: Build bottom-up from 0 to target amount (easier to implement, no stack overflow risk).
4. **Invariant**: After processing amount i, DP[i] contains the optimal solution for that amount.

## Java Solution

```java
class Solution {
    public int coinChange(int[] coins, int amount) {
        // dp[i] = minimum coins needed to make amount i
        int[] dp = new int[amount + 1];
        
        // Initialize: all amounts impossible (Integer.MAX_VALUE)
        // Only amount 0 is possible with 0 coins
        for (int i = 1; i <= amount; i++) {
            dp[i] = Integer.MAX_VALUE;
        }
        
        // Build up solutions for each amount
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i && dp[i - coin] != Integer.MAX_VALUE) {
                    // Can make amount i using this coin
                    dp[i] = Math.min(dp[i], 1 + dp[i - coin]);
                }
            }
        }
        
        // If dp[amount] is still MAX_VALUE, impossible to make that amount
        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }
}
```

## Complexity
- **Time**: $O(\text{amount} \times |\text{coins}|)$. We iterate through each amount from 1 to target, and for each amount we check all coins.
- **Space**: $O(\text{amount})$. DP array of size amount + 1.

## Edge Cases / Traps

1. **Impossible amounts**: If amount cannot be made, return -1 (check that DP[amount] is still Integer.MAX_VALUE).
2. **Amount = 0**: Should return 0 (zero coins needed). Already handled by initialization.
3. **Single large coin > amount**: Should return -1. Handled because coin is never ≤ i.
4. **Duplicate coins in input**: No issue; we try all coins for each amount anyway.
5. **Integer overflow**: Use Integer.MAX_VALUE carefully in the condition `dp[i - coin] != Integer.MAX_VALUE` before adding 1.
6. **All coins are 1**: Should return the amount itself. Correctly handled.

## Brute-Force vs Optimized

**Brute-Force (Recursive w/o Memoization):**
```java
int minCoins(int[] coins, int amount) {
    if (amount == 0) return 0;
    if (amount < 0) return -1;
    
    int min = Integer.MAX_VALUE;
    for (int coin : coins) {
        int result = minCoins(coins, amount - coin);
        if (result >= 0) {
            min = Math.min(min, 1 + result);
        }
    }
    return min == Integer.MAX_VALUE ? -1 : min;
}
```
**Problem**: Exponential time O(2^amount) due to overlapping subproblems. Each amount is recomputed many times.

**Optimized (DP)**: Linear traversal, each amount computed once. Time: O(amount × |coins|).

## Test Cases

```java
// Test 1: Standard case
assert new Solution().coinChange(new int[]{1, 2, 5}, 5) == 1;

// Test 2: Impossible
assert new Solution().coinChange(new int[]{2}, 3) == -1;

// Test 3: Amount = 0
assert new Solution().coinChange(new int[]{1}, 0) == 0;

// Test 4: Multiple coins needed
assert new Solution().coinChange(new int[]{1, 3, 4}, 6) == 2; // 3 + 3

// Test 5: Greedy doesn't work
assert new Solution().coinChange(new int[]{3, 4}, 6) == 2; // 3 + 3 (not 4 + 2)
```
