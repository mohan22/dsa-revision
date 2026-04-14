# Problem: Template for Minimum Window Substring

## Source
- Platform: Anki deck seed
- Topic: Two Pointers / Sliding Window
- Tags: SlidingWindow, MinWindow, Day23
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 1

## Problem Statement

Given two strings `s` and `t`, find the smallest substring of `s` that contains all characters of `t` with at least the same frequency. Return the minimum window substring. If no such substring exists, return an empty string.

The input typically includes:
- `s`: the source string
- `t`: the target string of required characters

The algorithm should:
- build a frequency map for `t`,
- expand a sliding window over `s`,
- track how many required characters are still missing,
- shrink the window when the current window is valid,
- record the smallest valid window seen.

This template is essential for the general class of minimum-substring and minimum-subarray problems with a collection of required elements.

## Recognition Pattern

- Topic signal: Two Pointers / Sliding Window
- Pattern hint from tags: SlidingWindow / MinWindow
- Key signal: smallest substring or subarray that covers all required items
- Tier 1 note: know the missing-count invariant and the two-phase expand/shrink window procedure

## Brute Force Thought

A brute-force approach checks every possible substring and verifies whether it covers `t`, leading to O(n^2 * m) time. The optimized template keeps counts and moves both ends of the window in one pass, reducing the work to O(n + m).

## Core Insight

Maintain a count of required characters. Expand the right end to include new characters until the window is valid. Then move the left end to remove extra characters while preserving validity. Track the best window length and position during the process.

## Solution Approach

1. Count characters in `t` using a frequency map.
2. Initialize `left = 0`, `matched = 0`, `minLen = Integer.MAX_VALUE`, `start = 0`.
3. Slide `right` through `s`:
   - decrement the count for `s[right]` if it is required,
   - increment `matched` when a required character's remaining count is non-negative,
   - when all chars are matched, shrink the window from the left while keeping it valid,
   - update the minimum window when the current window is valid.
4. Return the substring defined by the best window or `""` if none exists.

## Thought Process During Solving

1. Why does the valid window condition depend on character counts rather than just distinct presence?
2. How do you track whether removing the left character breaks validity?
3. What is the invariant that lets you stop shrinking the window?
4. Why is this template reusable for other minimum-window variants?

## Java Skeleton

Another version of the solution - https://docs.google.com/document/d/1wQA3e_XPPrqR1nfX5ppWDxQB9a4WcJBMBE7ag6TQWTg/edit?pli=1&tab=t.vt1w2o1x28dd#heading=h.r0q4qh4p6xz7

```java
class Solution {
    public String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) {
            return "";
        }

        int[] need = new int[128];
        for (char c : t.toCharArray()) {
            need[c]++;
        }

        int left = 0;
        int minLen = Integer.MAX_VALUE;
        int start = 0;
        int matchCount = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (need[c] > 0) {
                matchCount++;
            }
            need[c]--;

            while (matchCount == t.length()) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }

                char leftChar = s.charAt(left);
                need[leftChar]++;
                if (need[leftChar] > 0) {
                    matchCount--;
                }
                left++;
            }
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }
}
```

## Complexity
- Time: O(n + m)
- Space: O(1) for fixed alphabet or O(k) for the required character set

## Edge Cases / Traps

- `t` may be empty or longer than `s`.
- Characters may repeat in `t`; counts matter.
- The source string may contain characters not present in `t`.
- If the target uses a larger alphabet, adjust the frequency map accordingly.

## Why This Works

The algorithm expands until the window contains all required characters, then contracts to remove excess while preserving validity. The two-pointer window ensures every candidate substring is considered only once, yielding an optimal minimum window.

## Interview Explanation

Build a required-character count map, expand the window to satisfy all requirements, then shrink from the left to find the smallest valid window. Track the smallest valid window seen and return it.

## Similar Problems

- Minimum window subsequence
- Smallest substring containing all unique characters
- Smallest subarray with all distinct numbers

## Anki Recall Prompts

- What is the invariant for when the window is valid?
- How does the algorithm know when to move the left pointer?
- Why are counts required instead of just presence checks?
