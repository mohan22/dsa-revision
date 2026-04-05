# Trees and BST

## Recognition Cues

- Recursive structure with left/right subproblems
- Path, depth, balance, subtree, BST ordering

## Key BST Reminder

BST validity is global, not local. A node must fit within the full allowed range from its ancestors, not just be larger than its left child and smaller than its right child.

## Common Patterns

- Postorder DFS for bottom-up information
- Inorder traversal for BST sorted order
- DFS with bounds for BST validation

## Common Mistakes

- Using only parent-child comparisons in BST validation
- Forgetting null bounds
- Returning subtree height without propagating invalid states
