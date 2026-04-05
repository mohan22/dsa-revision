# Problem: Valid Parentheses

## Source
- Platform: LeetCode
- Topic: Stack
- Tags: Stack, Strings
- Difficulty: Easy
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Check whether a string of brackets is valid: every opening bracket must be closed by the correct type in the correct order.

## Recognition Pattern

- Nested structure
- Most recent unmatched opener matters
- Natural LIFO behavior points to a stack

## Brute Force Thought

Repeatedly remove valid adjacent pairs until nothing changes.

Why it is too slow:
- repeated rescans
- awkward string rebuilding

## Core Insight

Each closing bracket must match the latest unmatched opening bracket, not just any opening bracket. That is exactly a stack rule.

## Solution Approach

1. Traverse characters from left to right.
2. Push opening brackets onto a stack.
3. On a closing bracket, the stack must be non-empty and its top must be the matching opener.
4. If mismatch happens, return `false`.
5. At the end, the stack must be empty.

## Thought Process During Solving

1. Does order matter? Yes, so a set or frequency count is not enough.
2. What unresolved state do I need? The chain of still-open brackets.
3. Which opener should a closer match? The most recent unmatched one.
4. What causes failure immediately? Empty stack on closer or mismatched types.

## Java Solution
```java
import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char ch : s.toCharArray()) {
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.push(ch);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }

                char open = stack.pop();
                if ((ch == ')' && open != '(')
                        || (ch == ']' && open != '[')
                        || (ch == '}' && open != '{')) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }
}
```

## Complexity
- Time: `O(n)`
- Space: `O(n)`

## Edge Cases / Traps

- Starts with a closing bracket
- Extra opening brackets left at the end
- Using a legacy `Stack` when `ArrayDeque` is cleaner in Java

## Why This Works

The stack always stores exactly the unmatched opening brackets in left-to-right processing order. Because only the most recent unmatched opener can legally match the next closer, popping from the top verifies correctness greedily and completely.

## Interview Explanation

This is a matching and nesting problem, so I use a stack of opening brackets. Each closing bracket must match the opener on the top of the stack. If the stack is empty or the types differ, the string is invalid. If I finish with an empty stack, every opener was matched correctly.

## Similar Problems

- Longest Valid Parentheses
- Min Stack
- Daily Temperatures

## Anki Recall Prompts

- Why is this a stack problem instead of a counting problem?
- What are the two immediate invalid cases?
- Why is `ArrayDeque` preferred in Java?
