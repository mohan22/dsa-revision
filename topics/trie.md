# Trie

## Recognition Cues

- Prefix search
- Many dictionary words checked against shared prefixes
- Need early pruning on invalid prefixes

## Core Idea

A Trie compresses shared prefixes so search can stop as soon as a prefix is impossible.

## Common Mistakes

- Treating every DFS branch as valid without prefix pruning
- Forgetting `isWord` handling separate from prefix existence
- Leaving found words in output multiple times
