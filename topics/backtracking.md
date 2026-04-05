# Backtracking

## Recognition Cues

- Need all valid combinations, permutations, paths, or placements
- Can build answer incrementally
- Constraints allow pruning

## Universal Shape

```java
void dfs(...) {
    if (baseCase) {
        // record answer
        return;
    }

    for (int i = start; i < choices; i++) {
        // choose
        dfs(...);
        // unchoose
    }
}
```

## Common Mistakes

- Forgetting to undo a choice
- Using wrong next index for reuse vs no-reuse problems
- Missing sort/pruning when duplicates or early stopping matter
