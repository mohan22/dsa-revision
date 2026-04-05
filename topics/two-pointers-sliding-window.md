# Two Pointers and Sliding Window

## Recognition Cues

- Array or string segment must stay valid while expanding
- Contiguous subarray or substring
- Need longest, shortest, or count over windows

## Variable Window Template

```java
int left = 0;
for (int right = 0; right < s.length(); right++) {
    // add s.charAt(right)

    while (windowIsInvalid()) {
        // remove s.charAt(left)
        left++;
    }

    // update answer
}
```

## Common Mistakes

- Forgetting to shrink until valid again
- Moving `left` backward when using last-seen indices
- Updating answer before restoring window validity

## Representative Problems

- Longest Substring Without Repeating Characters
- Minimum Window Substring
- Longest Substring with K Distinct Characters
