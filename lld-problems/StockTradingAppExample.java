import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class StockTradingAppExample {

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

    static class Order {
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

    static class Trade {
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

    interface OrderStorage {
        void addOrder(Order order);
        boolean removeOrder(String orderId);
        Order getOrder(String orderId);
        PriorityQueue<Order> getBuyOrders();
        PriorityQueue<Order> getSellOrders();
    }

    interface OrderMatcher {
        List<Trade> matchOrders(OrderStorage storage);
    }

    static class OrderBookStorage implements OrderStorage {
        private final PriorityQueue<Order> buyOrders;
        private final PriorityQueue<Order> sellOrders;
        private final Map<String, Order> orderMap;

        OrderBookStorage() {
            this.buyOrders = new PriorityQueue<>(Comparator
                    .<Order>comparingDouble(o -> o.price).reversed()
                    .thenComparingLong(o -> o.timestamp));
            this.sellOrders = new PriorityQueue<>(Comparator
                    .<Order>comparingDouble(o -> o.price)
                    .thenComparingLong(o -> o.timestamp));
            this.orderMap = new HashMap<>();
        }

        @Override
        public void addOrder(Order order) {
            if (orderMap.containsKey(order.orderId)) {
                throw new IllegalArgumentException("Duplicate order id: " + order.orderId);
            }
            orderMap.put(order.orderId, order);
            if (order.side == OrderSide.BUY) {
                buyOrders.offer(order);
            } else {
                sellOrders.offer(order);
            }
        }

        @Override
        public boolean removeOrder(String orderId) {
            Order order = orderMap.get(orderId);
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
        public PriorityQueue<Order> getBuyOrders() {
            return buyOrders;
        }

        @Override
        public PriorityQueue<Order> getSellOrders() {
            return sellOrders;
        }
    }

    static class PriceTimeOrderMatcher implements OrderMatcher {
        private final AtomicLong tradeIdGenerator = new AtomicLong(1);
        private final String symbol;

        PriceTimeOrderMatcher(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public List<Trade> matchOrders(OrderStorage storage) {
            List<Trade> trades = new ArrayList<>();
            PriorityQueue<Order> buyOrders = storage.getBuyOrders();
            PriorityQueue<Order> sellOrders = storage.getSellOrders();

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

    static class OrderBook {
        private final String symbol;
        private final OrderStorage storage;
        private final OrderMatcher matcher;
        private final List<Trade> trades;

        OrderBook(String symbol, OrderStorage storage, OrderMatcher matcher) {
            this.symbol = symbol;
            this.storage = storage;
            this.matcher = matcher;
            this.trades = new ArrayList<>();
        }

        synchronized void placeOrder(Order order) {
            storage.addOrder(order);
            List<Trade> newTrades = matcher.matchOrders(storage);
            trades.addAll(newTrades);
        }

        synchronized boolean cancelOrder(String orderId) {
            return storage.removeOrder(orderId);
        }

        List<Trade> getTrades() {
            return new ArrayList<>(trades);
        }

        @Override
        public String toString() {
            return String.format("OrderBook{%s, buy=%d, sell=%d, trades=%d}",
                    symbol, storage.getBuyOrders().size(), storage.getSellOrders().size(), trades.size());
        }
    }

    static class OrderMatchingEngine {
        private final Map<String, OrderBook> books = new HashMap<>();

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
