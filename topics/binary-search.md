# Binary Search

## Recognition Cues

- Sorted array or sorted property
- Need first true / last false / position in monotonic space
- Can decide whether answer lies on left or right side

## Safe Mid

```java
int mid = left + (right - left) / 2;
```

## Common Variants

- direct target search
- first / last occurrence
- rotated sorted array
- binary search on answer

## Common Mistakes

- Infinite loop from wrong boundary updates
- Losing target when deciding sorted half in rotated arrays
- Returning wrong bound after loop terminates
