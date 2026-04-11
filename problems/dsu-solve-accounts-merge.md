# Problem: DSU Solves Accounts Merge

## Source
- Platform: LeetCode / Pattern
- Topic: DSU
- Tags: DSU, Merge
- Difficulty: Medium
- Revision Status: New
- Tier: Tier 1

## Problem Cue

Use DSU in Accounts Merge when different account rows should be merged if they share at least one common email.

## Brief Problem Statement

You are given accounts where each account has a user name followed by one or more email addresses. Accounts with at least one common email belong to the same person. Merge such accounts and return each merged account with the name and sorted emails.

## Recognition Pattern

- Entities should be merged by shared identifiers
- Overlap is transitive: if A shares with B and B shares with C, all three belong together
- DSU fits well because emails form connected components
- Final answer is built by grouping nodes by component root

## Brute Force Thought

Repeatedly compare accounts and merge any pair that shares an email until no more merges are possible.

Why it is too slow:
- too many repeated overlap checks
- awkward transitive merging logic
- inefficient when many accounts share long email lists

## Core Insight

Treat each email as a node. Union all emails that appear in the same account. After processing all accounts, emails with the same DSU root belong to one merged account. Then group emails by root and attach the corresponding name.

## Solution Approach

1. Give every unique email an integer id.
2. Map each email to its owner name.
3. For each account:
   - take the first email as the anchor
   - union it with every other email in that account
4. After all unions, group emails by `find(emailId)`.
5. Sort each email list and prepend the owner name.

## Thought Process During Solving

1. What exactly is being merged? Emails, not just account rows.
2. What makes DSU natural here? Shared emails define connectivity.
3. Why do we union emails within the same account? They definitely belong to one person.
4. How do we build the final merged accounts? Group emails by DSU root.
5. Where does the displayed name come from? Any email in the component maps back to the same owner name.

## Java Solution
```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Solution {
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, Integer> emailToId = new HashMap<>();
        Map<String, String> emailToName = new HashMap<>();
        int nextId = 0;

        for (List<String> account : accounts) {
            String name = account.get(0);
            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                if (!emailToId.containsKey(email)) {
                    emailToId.put(email, nextId++);
                }
                emailToName.put(email, name);
            }
        }

        DSU dsu = new DSU(nextId);

        for (List<String> account : accounts) {
            int firstEmailId = emailToId.get(account.get(1));
            for (int i = 2; i < account.size(); i++) {
                dsu.union(firstEmailId, emailToId.get(account.get(i)));
            }
        }

        Map<Integer, List<String>> emailsByRoot = new HashMap<>();
        for (String email : emailToId.keySet()) {
            int root = dsu.find(emailToId.get(email));
            emailsByRoot.computeIfAbsent(root, ignored -> new ArrayList<>()).add(email);
        }

        List<List<String>> result = new ArrayList<>();
        for (List<String> emails : emailsByRoot.values()) {
            Collections.sort(emails);
            List<String> merged = new ArrayList<>();
            merged.add(emailToName.get(emails.get(0)));
            merged.addAll(emails);
            result.add(merged);
        }

        return result;
    }

    private static class DSU {
        private final int[] parent;
        private final int[] rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return;
            }

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }
}
```

## Complexity
- Time: `O(T * alpha(E) + E log E)` where `T` is total email occurrences and `E` is unique emails
- Space: `O(E)`

## Edge Cases / Traps

- Same name does not guarantee same person; shared email does
- Different account rows may merge transitively through intermediate accounts
- The displayed name should come from the emails' owner mapping, not from arbitrary row order assumptions
- Sorting emails is required in the final output
- Be careful when an account contains only one email

## Why This Works

DSU groups emails into connected components where connectivity means "belongs to the same person." Emails listed in the same account are unioned together, and transitive sharing is handled automatically by DSU. Once all unions finish, each DSU root represents one merged person, so grouping emails by root reconstructs the final merged accounts.

## Interview Explanation

I model each email as a node in DSU. If two emails appear in the same account, I union them because they must belong to the same person. After processing all accounts, any emails that end up with the same root belong together. Then I group emails by root, sort them, and prepend the owner name to build the merged accounts.

## Similar Problems

- Union-Find Basics
- Count Connected Components Using DSU
- Number of Provinces

## Anki Recall Prompts

- What should be the DSU node here: account row or email?
- Why does grouping by DSU root solve transitive merges?
- When do we sort in the final solution?
