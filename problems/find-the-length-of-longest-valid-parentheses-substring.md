# Problem: Find The Length Of Longest Valid Parentheses Substring

## Source
- Platform: Anki deck seed
- Topic: Stack
- Tags: Stack, Strings, Hard, Day4
- Difficulty: Not labeled
- Revision Status: New
- Tier: Tier 2

## Problem Cue

How to find the length of longest valid parentheses substring


## Brief Problem Statement

Given the problem setup, find an efficient way to find the length of longest valid parentheses substring.

## Recognition Pattern

- Topic signal: Stack
- Pattern hint from tags: Stack / Strings
- Use this note to reconstruct the full solution before promoting it to Tier 1.


## Core Insight
Use a stack to track indices of unmatched '(' characters. Initialize the stack with -1 to handle the base case. For each character, if it is '(', push its index. If it is ')', pop the stack. If the stack is not empty after popping, calculate the length as current index minus stack top. If the stack is empty, push the current index as a new base.


## Solution Approach

### Objective and Constraints
- Input: A string `s` consisting of only '(' and ')'.
- Output: The length of the longest valid (well-formed) parentheses substring.
- Constraints: $1 \leq |s| \leq 10^5$

### Why Brute Force is Slow
- Checking all substrings for validity is $O(n^3)$ (for each substring, check if valid).

### Why Stack Works
- The stack keeps track of indices of unmatched '('. When a ')' is found, pop the stack. If the stack is not empty, the current valid substring length is `i - stack.peek()`. If the stack is empty, push the current index as a new base.
- Each index is pushed and popped at most once, so the algorithm is $O(n)$.

### Dry Run Example
Suppose `s = "(()())"`

| i | char | stack (top left) | maxLen |
|---|------|------------------|--------|
| 0 |  (   | [-1,0]           | 0      |
| 1 |  (   | [-1,0,1]         | 0      |
| 2 |  )   | [-1,0]           | 2      |
| 3 |  (   | [-1,0,3]         | 2      |
| 4 |  )   | [-1,0]           | 4      |
| 5 |  )   | [-1]             | 6      |

Final answer: 6


## Detailed Reasoning and Interview Explanation

**Why Brute Force is Slow:**
For each possible substring, checking if it is valid requires scanning and counting, which is $O(n^3)$ overall.

**Why Stack is Efficient:**
The stack allows us to efficiently match parentheses and compute the length of valid substrings in one pass. Each index is pushed and popped at most once, so the algorithm is $O(n)$.

**Interview-Style Explanation:**
"We use a stack to keep track of indices of unmatched '('. When we see a ')', we pop the stack. If the stack is not empty, the current valid substring length is the difference between the current index and the index on top of the stack. If the stack is empty, we push the current index as a new base. This ensures we always know the start of the last unmatched substring."


## Full Java Solution
```java
class Solution {
    public int longestValidParentheses(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1); // base for valid substring
        int maxLen = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (!stack.isEmpty()) {
                    maxLen = Math.max(maxLen, i - stack.peek());
                } else {
                    stack.push(i);
                }
            }
        }
        return maxLen;
    }
}
```


## Complexity
- Time: $O(n)$ — Each index is pushed and popped at most once.
- Space: $O(n)$ — For the stack.


## Brute-force vs Optimized Comparison
- **Brute-force:** Check all substrings for validity. Time: $O(n^3)$.
- **Optimized (Stack):** One pass, stack operations amortized. Time: $O(n)$.

## Additional Problem-Specific Traps
- Input string can be empty — return 0.
- All '(' or all ')' — answer is 0.
- Multiple valid substrings — ensure you track the maximum.


## Tier
- Tier 1
