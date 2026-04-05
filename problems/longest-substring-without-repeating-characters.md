# Problem: Longest Substring Without Repeating Characters

## Source
- Platform: LeetCode
- Topic: Two Pointers / Sliding Window
- Tags: SlidingWindow, Strings
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Find the length of the longest substring containing no repeated characters.

## Recognition Pattern

- Substring means contiguous range.
- Validity condition is "all characters unique".
- This is a classic variable-size sliding window.

## Brute Force Thought

Generate all substrings and test whether each has duplicates.

Why it is too slow:
- too many substrings
- repeated duplicate checks

## Core Insight

Use a window `[left, right]` that always stays duplicate-free.
When a repeated character appears, move `left` just past its previous index.

## Solution Approach

1. Track the most recent index of each character.
2. Expand `right` through the string.
3. If the current character was seen inside the active window, move `left` to `lastSeen + 1`.
4. Update the best length after each step.

## Thought Process During Solving

1. Is this substring or subsequence? Substring, so think window.
2. What property must stay true? No duplicates inside the window.
3. If I see a repeated character, do I move `left` one step or jump? Jump using the stored last index.
4. Why store last seen instead of only a set? To avoid shrinking one by one.
5. What bug is common? Moving `left` backward when the old duplicate is already outside the window.

## Java Solution
```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        int left = 0;
        int best = 0;

        for (int right = 0; right < s.length(); right++) {
            char ch = s.charAt(right);
            if (lastSeen.containsKey(ch)) {
                left = Math.max(left, lastSeen.get(ch) + 1);
            }
            lastSeen.put(ch, right);
            best = Math.max(best, right - left + 1);
        }

        return best;
    }
}
```

## Complexity
- Time: `O(n)`
- Space: `O(min(n, charset))`

## Edge Cases / Traps

- Empty string
- All characters same
- Repeated char whose previous index is already left of the window
- Using `left = lastSeen + 1` without `Math.max`

## Why This Works

The window always represents the longest valid substring ending at `right`. When a duplicate is found, the only way to restore validity is to exclude the earlier copy by moving `left` past it. Because `left` only moves forward, each index is processed a constant number of times.

## Interview Explanation

I keep a sliding window of unique characters. A map tells me the last position of each character, so when I see a duplicate I can jump the left boundary instead of shrinking step by step. That keeps the solution linear while preserving the window invariant that every character inside it is unique.

## Similar Problems

- Minimum Window Substring
- Longest Substring with K Distinct Characters
- Permutation in String

## Anki Recall Prompts

- What is the invariant of the sliding window here?
- Why do we use `Math.max(left, lastSeen + 1)`?
- Why is a last-index map stronger than a set?
