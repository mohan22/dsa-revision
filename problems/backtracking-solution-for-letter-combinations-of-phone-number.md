# Problem: Backtracking Solution For Letter Combinations Of Phone Number

## Source
- Platform: LeetCode / Interview
- Topic: Backtracking
- Tags: Backtracking, PhoneNumber, Combinations, Day24
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Statement

Given a string containing digits 2-9, return all possible letter combinations that the number could represent on a traditional phone keypad. Each digit maps to a set of letters (like 2="abc", 3="def", etc.).

The input typically includes:
- `digits`: a string of digits 2-9

The algorithm should:
- map each digit to its corresponding letters,
- use backtracking to build all combinations,
- for each digit, explore all possible letters it can represent,
- combine letters from different digits systematically.

This pattern is essential for understanding backtracking on problems where choices vary by input element.

## Recognition Pattern

- Topic signal: Backtracking
- Pattern hint from tags: Backtracking / PhoneNumber / Combinations
- Key signal: generate all combinations of letters based on digit mappings
- Tier 1 note: know the digit-to-letters mapping and the recursive exploration pattern

## Brute Force Thought

A brute-force approach uses nested loops for each digit, but the depth is variable based on input length. Backtracking elegantly handles this by recursively exploring one digit at a time.

## Core Insight

For each digit in the input, map it to its letters. Use backtracking to build combinations by choosing one letter from the current digit's mapping, then recursing on the remaining digits.

## Solution Approach

1. Create a mapping from digits 2-9 to their letter combinations.
2. Use backtracking with parameters: current combination, current digit index.
3. Base case: if digit index reaches the end, add the current combination to result.
4. Recursive case: for each letter in the current digit's mapping:
   - Add the letter to the combination,
   - recurse to the next digit,
   - remove the letter (backtrack).
5. Return all collected combinations.

## Thought Process During Solving

1. How do I map digits to letters?
2. What is the base case for the recursion?
3. Why is backtracking suitable here instead of iteration?
4. How do I handle an empty input?

## Java Skeleton
```java
class Solution {
    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits == null || digits.length() == 0) {
            return result;
        }

        String[] mapping = {
            "",     // 0
            "",     // 1
            "abc",  // 2
            "def",  // 3
            "ghi",  // 4
            "jkl",  // 5
            "mno",  // 6
            "pqrs", // 7
            "tuv",  // 8
            "wxyz"  // 9
        };

        backtrack(result, new StringBuilder(), digits, mapping, 0);
        return result;
    }

    private void backtrack(List<String> result, StringBuilder combination, String digits, String[] mapping, int index) {
        if (index == digits.length()) {
            result.add(combination.toString());
            return;
        }

        String letters = mapping[digits.charAt(index) - '0'];
        for (char letter : letters.toCharArray()) {
            combination.append(letter);
            backtrack(result, combination, digits, mapping, index + 1);
            combination.deleteCharAt(combination.length() - 1);
        }
    }
}
```

## Complexity
- Time: O(4^n * n) where n is the length of digits (4 is max letters per digit)
- Space: O(n) for recursion depth

## Edge Cases / Traps

- Empty input string should return an empty list.
- Input contains 0 or 1, which have no letters (should be skipped or handled as empty).
- Single digit input.
- All digits map to letters with different counts (2 has 3, 7 and 9 have 4).

## Why This Works

Each recursive call explores one digit and iterates through its letter options. The backtracking mechanism builds up combinations incrementally, undoing each choice after exploring it. This naturally generates all combinations without nested loops.

## Interview Explanation

Map each digit to its letters using a string array. Use backtracking to build combinations: for each digit, try each of its letters, recurse to the next digit, then backtrack.

## Similar Problems

- Generate Parentheses
- Permutations
- Subsets
- Combination Sum

## Anki Recall Prompts

- What digits do not have letter mappings?
- Why use StringBuilder for building the combination?
- What is the base case for the recursion?
