# Problem: Word Search II

## Source
- Platform: LeetCode
- Topic: Trie / Backtracking
- Tags: Trie, DFS, Grid
- Difficulty: Hard
- Revision Status: New
- Tier: Tier 2

## Problem Cue

Find all dictionary words that can be formed on a board using adjacent cells.

## Recognition Pattern

- Multiple words checked against the same board
- Shared prefixes matter
- Plain DFS from each cell without pruning is too expensive

## Core Insight

Build a Trie of the words, then DFS the board while walking the Trie simultaneously. The Trie prunes paths as soon as the current prefix is impossible.

## Solution Approach

1. Insert all words into a Trie.
2. Start DFS from each board cell.
3. At each DFS step:
   - stop if char is not a Trie child
   - advance Trie node
   - record word if Trie node marks a complete word
   - mark visited, explore neighbors, unmark
4. Deduplicate found words, usually by nulling out the stored word once collected.

## Thought Process During Solving

1. Why not run Word Search for each word independently? Too much repeated board work.
2. What structure shares prefix work? Trie.
3. What is the pruning condition? Current board path is not a Trie prefix.
4. Where do bugs happen? Visited handling and duplicate results.

## Java Skeleton
```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<String> findWords(char[][] board, String[] words) {
        return new ArrayList<>();
    }
}
```

## Complexity
- Time: Better than naive repeated search due to prefix pruning; still exponential in worst-case DFS branching
- Space: Trie size plus recursion stack

## Edge Cases / Traps

- Reusing a cell in the same word path
- Returning duplicates
- Forgetting to restore the board after DFS

## Promotion Checklist

- Add Trie node structure
- Add full Java DFS + Trie implementation
- Add note on deduplicating found words efficiently
