# Dynamic Programming

## Recognition Cues

- Repeated subproblems
- Best/count/feasibility answer built from smaller states
- Recurrence can be written clearly

## DP Workflow

1. Define the state.
2. Write the transition.
3. Set base cases.
4. Start with recursive reasoning and recurrence structure.
5. Convert to DP using memoization or bottom-up tabulation.
6. Optimize space only after logic is correct.

## DP Solution Structure

- Recursive approach / recurrence reasoning first.
- DP solution next: memoized top-down or bottom-up table.
- Space-optimized DP last: reduce memory when the recurrence allows it.

## Common Mistakes

- Wrong base cases
- Mixing "ways" DP and "best value" DP
- Iterating in the wrong order for knapsack-style updates
