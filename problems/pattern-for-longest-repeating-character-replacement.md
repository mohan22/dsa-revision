# Problem: Pattern for Longest Repeating Character Replacement

## Source
- Platform: Anki deck seed
- Topic: Two Pointers / Sliding Window
- Tags: SlidingWindow, ReplaceChar, Day23
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Statement

Given a string `s` and an integer `k`, find the length of the longest substring where you can replace at most `k` characters to make all characters in the substring the same. This is the classic "Longest Repeating Character Replacement" problem where the sliding window tracks the maximum frequency of any character in the current window.

The input typically includes:
- `s`: the input string
- `k`: the maximum number of replacements allowed

The algorithm should:
- expand a sliding window across the string,
- track the frequency of characters in the window,
- shrink the window when the number of replacements needed exceeds `k`,
- maintain the maximum valid window length.

This pattern is essential for sliding-window problems involving character frequencies and replacement constraints.

## Recognition Pattern

- Topic signal: Two Pointers / Sliding Window
- Pattern hint from tags: SlidingWindow / ReplaceChar
- Key signal: longest substring with at most k changes to make all characters identical
- Tier 1 note: know the invariant that replacements needed = window size - max frequency in window

## Brute Force Thought

A brute-force approach checks every substring and counts replacements needed, leading to O(n^2) time. The optimized sliding-window solution maintains frequencies and moves both ends in one pass, reducing to O(n).

## Core Insight

Use a frequency map for the current window. Expand the right end, and when the window size minus the maximum frequency exceeds `k`, shrink from the left. Track the longest valid window.

## Solution Approach

1. Initialize `left = 0`, `maxLen = 0`, and a frequency array or map.
2. Iterate `right` from `0` to `n - 1`.
3. Increment the count for `s[right]`.
4. While `right - left + 1 - maxFreq > k`, decrement the count for `s[left]` and increment `left`.
5. Update `maxLen = max(maxLen, right - left + 1)`.
6. Return `maxLen`.

## Thought Process During Solving

1. Why is the replacement count based on window size minus max frequency?
2. How does shrinking the window ensure the constraint is met?
3. What happens when multiple characters have the same max frequency?
4. Why is this approach linear time?

## Java Skeleton
```java
class Solution {
    public int characterReplacement(String s, int k) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        int[] count = new int[26];
        int left = 0;
        int maxFreq = 0;
        int maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            count[s.charAt(right) - 'A']++;
            maxFreq = Math.max(maxFreq, count[s.charAt(right) - 'A']);

            while (right - left + 1 - maxFreq > k) {
                count[s.charAt(left) - 'A']--;
                left++;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }
}
```

## Complexity
- Time: O(n)
- Space: O(1) for fixed alphabet

## Edge Cases / Traps

- `k` may be 0, requiring no replacements.
- `k` may be larger than the string length.
- All characters are the same, so the result is the full length.
- The string may contain only one character.

## Why This Works

The window is expanded until the replacements needed exceed `k`, then contracted to restore validity. The invariant ensures the window always satisfies the replacement limit, and the maximum length is tracked.

## Interview Explanation

Use a sliding window with a frequency count. Expand the window, and shrink when replacements needed exceed k. The longest valid window is the answer.

## Similar Problems

- Longest substring with at most two distinct characters
- Minimum window substring
- Longest subarray with sum at most k

## Anki Recall Prompts

- What is the formula for replacements needed in the window?
- When should the left pointer move?
- Why is maxFreq updated inside the loop?

