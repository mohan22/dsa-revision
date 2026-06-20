# Stock Trading App - Low Level Design (LLD)

## System Overview
A real-time stock trading application that allows users to buy/sell stocks, manage their portfolio, and view market data with proper concurrency handling and transaction management.

---

## 1. Core Entities

### User
- userId: String (UUID)
- userName: String
- email: String
- cash: BigDecimal
- portfolio: Map<String, Integer> (stockSymbol -> quantity)
- transactionHistory: List<Transaction>

### Stock
- symbol: String (unique identifier, e.g., "AAPL")
- name: String
- currentPrice: BigDecimal
- lastUpdated: LocalDateTime

### Order
- orderId: String (UUID)
- userId: String
- symbol: String
- orderType: BUY/SELL
- quantity: Integer
- pricePerShare: BigDecimal
- status: PENDING/COMPLETED/CANCELLED
- timestamp: LocalDateTime
- executionPrice: BigDecimal (actual price at execution)

### Transaction
- transactionId: String (UUID)
- orderId: String (foreign key)
- userId: String
- symbol: String
- transactionType: BUY/SELL
- quantity: Integer
- pricePerShare: BigDecimal
- totalAmount: BigDecimal
- timestamp: LocalDateTime

### Market Quote
- symbol: String
- bid: BigDecimal
- ask: BigDecimal
- lastSalePrice: BigDecimal
- timestamp: LocalDateTime

---

## 2. System Components

### StockExchange (Core Trading Engine)
```
Responsibilities:
- Match buy/sell orders
- Maintain order book
- Execute trades atomically
- Update stock prices
- Manage multiple order queues (one per stock)

Key Methods:
- placeOrder(Order): OrderResult
- cancelOrder(String orderId): boolean
- getOrderBook(String symbol): OrderBook
- updateStockPrice(String symbol, BigDecimal price): void
```

### OrderBook
```
Responsibilities:
- Maintain buy and sell orders for a specific stock
- Order matching (FIFO, price-priority)
- Track pending/matched orders

Structure:
- buyOrders: PriorityQueue<Order> (sorted by price DESC)
- sellOrders: PriorityQueue<Order> (sorted by price ASC)
```

### PortfolioManager
```
Responsibilities:
- Manage user holdings
- Update user cash balance
- Track transaction history
- Generate portfolio report

Key Methods:
- addHolding(userId, symbol, quantity, pricePerShare): void
- removeHolding(userId, symbol, quantity): void
- updateCash(userId, amount): void
- getPortfolio(userId): Portfolio
```

### User Service
```
Responsibilities:
- User registration/authentication
- User profile management
- Cash management

Key Methods:
- registerUser(userName, email): User
- getUser(userId): User
- depositCash(userId, amount): void
- withdrawCash(userId, amount): void
```

### Stock Price Service
```
Responsibilities:
- Maintain current stock prices
- Update stock prices from market data
- Provide price quotes

Key Methods:
- updatePrice(symbol, price): void
- getPrice(symbol): BigDecimal
- getAllPrices(): Map<String, BigDecimal>
```

---

## 3. Key Design Patterns & Considerations

### Concurrency Handling
- **Thread-Safe Collections**: ConcurrentHashMap for user holdings
- **ReentrantReadWriteLock**: For order book access (read-heavy)
- **Synchronized Queue**: For order matching queue
- **Atomic Operations**: For balance updates

### Transaction Management
- **ACID Properties**:
  - Atomicity: Order execution is atomic (all or nothing)
  - Consistency: Cash + Holdings always balanced
  - Isolation: CompletableFuture for async order execution
  - Durability: Transaction history persisted

### Order Matching Strategy
- Price-Time Priority: Higher price buys/lower price sells matched first
- FIFO: Orders at same price matched by arrival time
- Partial Fills: Allowed (order qty > available qty)

---

## 4. API Endpoints/Methods

### Trading Operations
```
placeOrder(userId, symbol, quantity, pricePerShare, orderType): CompletableFuture<OrderResult>
cancelOrder(userId, orderId): boolean
```

### Portfolio Operations
```
getPortfolio(userId): Portfolio
getTransactionHistory(userId, limit): List<Transaction>
getHolding(userId, symbol): Integer
```

### Market Data
```
getStockPrice(symbol): BigDecimal
getOrderBook(symbol): OrderBook
```

### User Operations
```
registerUser(userName, email, initialCash): User
depositCash(userId, amount): void
withdrawCash(userId, amount): void
```

---

## 5. Implementation Sequence

1. **Phase 1**: Core domain models (User, Stock, Order, Transaction)
2. **Phase 2**: StockExchange and OrderBook with matching logic
3. **Phase 3**: PortfolioManager and transaction persistence
4. **Phase 4**: User Service and authentication
5. **Phase 5**: Concurrency & thread-safety
6. **Phase 6**: Error handling and edge cases

---

## 6. Edge Cases & Error Handling

### Insufficient Funds
- Validate cash before buy order
- Return OrderResult with error code

### Insufficient Holdings
- Validate holdings before sell order
- Reject if qty insufficient

### Price Validation
- Reject negative/zero prices
- Reject invalid order quantities

### Race Conditions
- Use locks for order matching
- Atomic balance updates

### Order Cancellation
- Only allow if order PENDING
- Return cash if buy order cancelled
- Return holdings if sell order cancelled

---

## 7. Database Schema (Persistence Layer)

### Users Table
```
user_id (PK), username, email, total_cash, created_at
```

### Holdings Table
```
holding_id (PK), user_id (FK), symbol, quantity, updated_at
```

### Orders Table
```
order_id (PK), user_id (FK), symbol, order_type, quantity, price_per_share, status, created_at
```

### Transactions Table
```
transaction_id (PK), order_id (FK), user_id (FK), symbol, quantity, price, timestamp
```

---

## 8. Performance Considerations

- **Order Book**: TreeMap for O(log n) insertion/deletion
- **Price Lookup**: HashMap for O(1) access
- **Async Processing**: CompletableFuture for non-blocking order execution
- **Connection Pooling**: For database operations
- **Caching**: Cache stock prices with TTL
