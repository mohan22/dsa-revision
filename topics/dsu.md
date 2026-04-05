# DSU

## Recognition Cues

- Repeated connectivity queries
- Incremental unions
- Need to know whether two nodes belong to the same component

## Core Operations

- `find(x)`: locate representative root
- `union(x, y)`: merge components

## Optimization

- path compression during `find`
- union by rank or size during `union`

## Common Mistakes

- Forgetting to compress paths
- Updating rank incorrectly
- Using DSU when traversal is simpler and only one query exists
