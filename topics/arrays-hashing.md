# Arrays and Hashing

## Recognition Cues

- Need `O(1)` average lookup while scanning once
- Complement / seen-before / frequency problems
- Order of original array still matters

## Core Java Tools

- `HashMap<Integer, Integer>` for index or frequency
- `HashSet<Integer>` for membership
- `int[] freq` when the value range is small and fixed

## Common Invariants

- Map contains exactly the elements processed so far
- Frequency map reflects the current prefix or window
- Never overwrite the earliest index when earliest position matters

## Common Mistakes

- Inserting before checking complement in Two Sum
- Overwriting first occurrence when longest span is needed
- Using sorting when original indices must be preserved

## Representative Problems

- Two Sum
- Group Anagrams
- Subarray Sum Equals K
- Top K Frequent Elements
