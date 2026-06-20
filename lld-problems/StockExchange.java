import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

public class StockExchange {
    private final Map<String, OrderBook> orderBooks; // symbol -> OrderBook
    private final Map<String, Stock> stocks;
    private final Map<String, User> users;
    private final List<Transaction> transactionHistory;
    private final ExecutorService matchingEngine;

    public StockExchange() {
        this.orderBooks = new ConcurrentHashMap<>();
        this.stocks = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
        this.transactionHistory = new CopyOnWriteArrayList<>();
        this.matchingEngine = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "MatchingEngine");
            t.setDaemon(false);
            return t;
        });
        startMatchingEngine();
    }

    private void startMatchingEngine() {
        matchingEngine.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    for (OrderBook orderBook : orderBooks.values()) {
                        List<Order> matchedOrders = orderBook.matchOrders();
                        for (Order order : matchedOrders) {
                            if (order.getStatus() == Order.OrderStatus.COMPLETED) {
                                executeTransaction(order);
                            }
                        }
                    }
                    Thread.sleep(100); // Check for matches every 100ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    public void registerStock(String symbol, String name, BigDecimal initialPrice) {
        Stock stock = new Stock(symbol, name, initialPrice);
        stocks.put(symbol, stock);
        orderBooks.put(symbol, new OrderBook(symbol));
    }

    public void registerUser(User user) {
        users.put(user.getUserId(), user);
    }

    public synchronized OrderResult placeOrder(String userId, String symbol, 
                                              Order.OrderType orderType, 
                                              int quantity, BigDecimal pricePerShare) {
        User user = users.get(userId);
        if (user == null) {
            return OrderResult.invalidOrder("User not found");
        }

        Stock stock = stocks.get(symbol);
        if (stock == null) {
            return OrderResult.invalidOrder("Stock not found");
        }

        if (orderType == Order.OrderType.BUY) {
            BigDecimal requiredCash = pricePerShare.multiply(new BigDecimal(quantity));
            if (user.getCash().compareTo(requiredCash) < 0) {
                return OrderResult.insufficientFunds();
            }
            user.withdrawCash(requiredCash);
        } else { // SELL
            if (user.getHolding(symbol) < quantity) {
                return OrderResult.insufficientHoldings();
            }
            user.removeHolding(symbol, quantity);
        }

        Order order = new Order(userId, symbol, orderType, quantity, pricePerShare);
        OrderBook orderBook = orderBooks.get(symbol);
        orderBook.addOrder(order);

        return OrderResult.success(order.getOrderId());
    }

    private synchronized void executeTransaction(Order order) {
        User user = users.get(order.getUserId());
        if (user == null) return;

        if (order.getOrderType() == Order.OrderType.BUY) {
            user.addHolding(order.getSymbol(), order.getFilledQuantity());
        } else { // SELL
            BigDecimal proceeds = order.getExecutionPrice()
                    .multiply(new BigDecimal(order.getFilledQuantity()));
            user.addCash(proceeds);
        }

        Transaction transaction = new Transaction(
                order.getOrderId(),
                user.getUserId(),
                order.getSymbol(),
                order.getOrderType() == Order.OrderType.BUY ? 
                    Transaction.TransactionType.BUY : Transaction.TransactionType.SELL,
                order.getFilledQuantity(),
                order.getExecutionPrice()
        );
        transactionHistory.add(transaction);
    }

    public boolean cancelOrder(String userId, String orderId) {
        for (OrderBook orderBook : orderBooks.values()) {
            OrderBook.OrderBookSnapshot snapshot = orderBook.getSnapshot();
            List<Order> allOrders = new ArrayList<>();
            allOrders.addAll(snapshot.getBuyOrders());
            allOrders.addAll(snapshot.getSellOrders());

            for (Order order : allOrders) {
                if (order.getOrderId().equals(orderId) && order.getUserId().equals(userId)) {
                    if (order.getStatus() == Order.OrderStatus.PENDING) {
                        order.cancel();
                        User user = users.get(userId);
                        if (user != null) {
                            if (order.getOrderType() == Order.OrderType.BUY) {
                                BigDecimal refund = order.getPricePerShare()
                                        .multiply(new BigDecimal(order.getQuantity()));
                                user.addCash(refund);
                            } else { // SELL
                                user.addHolding(order.getSymbol(), order.getQuantity());
                            }
                        }
                        orderBook.removeOrder(order);
                        return true;
                    }
                    return false; // Can't cancel non-pending order
                }
            }
        }
        return false; // Order not found
    }

    public OrderBook.OrderBookSnapshot getOrderBook(String symbol) {
        OrderBook orderBook = orderBooks.get(symbol);
        return orderBook != null ? orderBook.getSnapshot() : null;
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public void shutdown() {
        matchingEngine.shutdownNow();
    }
}
