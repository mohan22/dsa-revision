
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.*;

public class StockTradingAppExample {
    public static void main(String[] args) {
        OrderMatchingEngine engine = new OrderMatchingEngine();

        engine.placeOrder(new Order("O1", "AAPL", OrderSide.BUY, OrderType.LIMIT, 100, 172.50, System.nanoTime()));
        engine.placeOrder(new Order("O2", "AAPL", OrderSide.SELL, OrderType.LIMIT, 50, 172.25, System.nanoTime()));
        engine.placeOrder(new Order("O3", "AAPL", OrderSide.SELL, OrderType.LIMIT, 70, 172.50, System.nanoTime()));
        engine.placeOrder(new Order("O4", "AAPL", OrderSide.BUY, OrderType.MARKET, 30, 0, System.nanoTime()));

        List<Trade> trades = engine.getTrades("AAPL");
        System.out.println("Executed trades:");
        for (Trade trade : trades) {
            System.out.println(trade);
        }
    }
}

// --- Enums ---
enum OrderSide {
    BUY,
    SELL
}

enum OrderType {
    LIMIT,
    MARKET
}

enum OrderStatus {
    NEW,
    PARTIALLY_FILLED,
    FILLED,
    CANCELLED
}

// --- Domain Classes ---
class Order {
    final String orderId;
    final String symbol;
    final OrderSide side;
    final OrderType type;
    final long timestamp;
    final double price;
    final int quantity;
    int filledQuantity;
    OrderStatus status;

    Order(String orderId, String symbol, OrderSide side, OrderType type,
          int quantity, double price, long timestamp) {
        this.orderId = orderId;
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
        this.filledQuantity = 0;
        this.status = OrderStatus.NEW;
    }

    int remainingQuantity() {
        return quantity - filledQuantity;
    }

    boolean isFilled() {
        return remainingQuantity() <= 0;
    }

    void applyFill(int executedQuantity) {
        filledQuantity += executedQuantity;
        if (isFilled()) {
            status = OrderStatus.FILLED;
        } else {
            status = OrderStatus.PARTIALLY_FILLED;
        }
    }

    void cancel() {
        if (!isFilled()) {
            status = OrderStatus.CANCELLED;
        }
    }

    @Override
    public String toString() {
        return String.format("Order{id=%s, side=%s, type=%s, symbol=%s, qty=%d, price=%.2f, filled=%d, status=%s}",
                orderId, side, type, symbol, quantity, price, filledQuantity, status);
    }
}

class Trade {
    final String tradeId;
    final String buyOrderId;
    final String sellOrderId;
    final String symbol;
    final int quantity;
    final double price;
    final long timestamp;

    Trade(String tradeId, String buyOrderId, String sellOrderId, String symbol,
          int quantity, double price, long timestamp) {
        this.tradeId = tradeId;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("Trade{id=%s, symbol=%s, qty=%d, price=%.2f, buy=%s, sell=%s}",
                tradeId, symbol, quantity, price, buyOrderId, sellOrderId);
    }
}

// --- Interfaces ---
interface OrderStorage {
    void addOrder(Order order);
    boolean removeOrder(String orderId);
    Order getOrder(String orderId);
    PriorityBlockingQueue<Order> getBuyOrders();
    PriorityBlockingQueue<Order> getSellOrders();
}

interface OrderMatcher {
    List<Trade> matchOrders(OrderStorage storage);
}

// --- Implementations ---
class OrderBookStorage implements OrderStorage {
    private final PriorityBlockingQueue<Order> buyOrders;
    private final PriorityBlockingQueue<Order> sellOrders;
    private final ConcurrentHashMap<String, Order> orderMap;

    OrderBookStorage() {
        this.buyOrders = new PriorityBlockingQueue<>(11, Comparator
                .<Order>comparingDouble(o -> o.price).reversed()
                .thenComparingLong(o -> o.timestamp));
        this.sellOrders = new PriorityBlockingQueue<>(11, Comparator
                .<Order>comparingDouble(o -> o.price)
                .thenComparingLong(o -> o.timestamp));
        this.orderMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addOrder(Order order) {
        if (orderMap.putIfAbsent(order.orderId, order) != null) {
            throw new IllegalArgumentException("Duplicate order id: " + order.orderId);
        }
        if (order.side == OrderSide.BUY) {
            buyOrders.offer(order);
        } else {
            sellOrders.offer(order);
        }
    }

    @Override
    public boolean removeOrder(String orderId) {
        Order order = orderMap.remove(orderId);
        if (order == null) {
            return false;
        }
        boolean removed = (order.side == OrderSide.BUY)
                ? buyOrders.remove(order)
                : sellOrders.remove(order);
        if (removed) {
            order.cancel();
        }
        return removed;
    }

    @Override
    public Order getOrder(String orderId) {
        return orderMap.get(orderId);
    }

    @Override
    public PriorityBlockingQueue<Order> getBuyOrders() {
        return buyOrders;
    }

    @Override
    public PriorityBlockingQueue<Order> getSellOrders() {
        return sellOrders;
    }
}

class PriceTimeOrderMatcher implements OrderMatcher {
    private final AtomicLong tradeIdGenerator = new AtomicLong(1);
    private final String symbol;

    PriceTimeOrderMatcher(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public List<Trade> matchOrders(OrderStorage storage) {
        List<Trade> trades = new ArrayList<>();
        PriorityBlockingQueue<Order> buyOrders = storage.getBuyOrders();
        PriorityBlockingQueue<Order> sellOrders = storage.getSellOrders();

        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            Order bestBuy = buyOrders.peek();
            Order bestSell = sellOrders.peek();

            if (!canMatch(bestBuy, bestSell)) {
                break;
            }

            int executableQuantity = Math.min(bestBuy.remainingQuantity(), bestSell.remainingQuantity());
            double executionPrice = determineExecutionPrice(bestBuy, bestSell);
            Trade trade = createTrade(bestBuy, bestSell, executableQuantity, executionPrice);
            trades.add(trade);

            bestBuy.applyFill(executableQuantity);
            bestSell.applyFill(executableQuantity);

            if (bestBuy.isFilled()) {
                buyOrders.poll();
            }
            if (bestSell.isFilled()) {
                sellOrders.poll();
            }
        }
        return trades;
    }

    private boolean canMatch(Order buy, Order sell) {
        if (buy.type == OrderType.MARKET || sell.type == OrderType.MARKET) {
            return true;
        }
        return buy.price >= sell.price;
    }

    private double determineExecutionPrice(Order buy, Order sell) {
        if (buy.type == OrderType.MARKET && sell.type == OrderType.MARKET) {
            return (buy.price + sell.price) / 2;
        }
        if (buy.type == OrderType.MARKET) {
            return sell.price;
        }
        if (sell.type == OrderType.MARKET) {
            return buy.price;
        }
        return sell.price;
    }

    private Trade createTrade(Order buy, Order sell, int quantity, double price) {
        String tradeId = symbol + "-T" + tradeIdGenerator.getAndIncrement();
        return new Trade(tradeId, buy.orderId, sell.orderId, symbol, quantity, price, System.currentTimeMillis());
    }
}

class OrderBook {
    private final String symbol;
    private final OrderStorage storage;
    private final OrderMatcher matcher;
    private final List<Trade> trades;
    private final ReentrantLock lock = new ReentrantLock();

    OrderBook(String symbol, OrderStorage storage, OrderMatcher matcher) {
        this.symbol = symbol;
        this.storage = storage;
        this.matcher = matcher;
        this.trades = new ArrayList<>();
    }

    void placeOrder(Order order) {
        lock.lock();
        try {
            storage.addOrder(order);
            List<Trade> newTrades = matcher.matchOrders(storage);
            trades.addAll(newTrades);
        } finally {
            lock.unlock();
        }
    }

    boolean cancelOrder(String orderId) {
        lock.lock();
        try {
            return storage.removeOrder(orderId);
        } finally {
            lock.unlock();
        }
    }

    List<Trade> getTrades() {
        lock.lock();
        try {
            return new ArrayList<>(trades);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return String.format("OrderBook{%s, buy=%d, sell=%d, trades=%d}",
                symbol, storage.getBuyOrders().size(), storage.getSellOrders().size(), trades.size());
    }
}

class OrderMatchingEngine {
    private final ConcurrentMap<String, OrderBook> books = new ConcurrentHashMap<>();

    OrderBook getOrCreateBook(String symbol) {
        return books.computeIfAbsent(symbol, s -> {
            OrderStorage storage = new OrderBookStorage();
            OrderMatcher matcher = new PriceTimeOrderMatcher(s);
            return new OrderBook(s, storage, matcher);
        });
    }

    void placeOrder(Order order) {
        OrderBook book = getOrCreateBook(order.symbol);
        book.placeOrder(order);
    }

    boolean cancelOrder(String symbol, String orderId) {
        OrderBook book = books.get(symbol);
        return book != null && book.cancelOrder(orderId);
    }

    List<Trade> getTrades(String symbol) {
        OrderBook book = books.get(symbol);
        return book == null ? Collections.emptyList() : book.getTrades();
    }
}
