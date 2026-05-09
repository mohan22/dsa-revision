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

    static class OrderBook {
        private final String symbol;
        private final PriorityQueue<Order> buyOrders;
        private final PriorityQueue<Order> sellOrders;
        private final Map<String, Order> orderMap;
        private final List<Trade> trades;
        private final AtomicLong tradeIdGenerator;

        OrderBook(String symbol) {
            this.symbol = symbol;
            this.buyOrders = new PriorityQueue<>(Comparator
                    .<Order>comparingDouble(o -> o.price).reversed()
                    .thenComparingLong(o -> o.timestamp));
            this.sellOrders = new PriorityQueue<>(Comparator
                    .<Order>comparingDouble(o -> o.price)
                    .thenComparingLong(o -> o.timestamp));
            this.orderMap = new HashMap<>();
            this.trades = new ArrayList<>();
            this.tradeIdGenerator = new AtomicLong(1);
        }

        synchronized void placeOrder(Order order) {
            if (orderMap.containsKey(order.orderId)) {
                throw new IllegalArgumentException("Duplicate order id: " + order.orderId);
            }
            orderMap.put(order.orderId, order);
            if (order.side == OrderSide.BUY) {
                buyOrders.offer(order);
            } else {
                sellOrders.offer(order);
            }
            matchOrders();
        }

        synchronized boolean cancelOrder(String orderId) {
            Order order = orderMap.get(orderId);
            if (order == null || order.isFilled() || order.status == OrderStatus.CANCELLED) {
                return false;
            }
            boolean removed = (order.side == OrderSide.BUY)
                    ? buyOrders.remove(order)
                    : sellOrders.remove(order);
            order.cancel();
            return removed;
        }

        private void matchOrders() {
            while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
                Order bestBuy = buyOrders.peek();
                Order bestSell = sellOrders.peek();

                if (!canMatch(bestBuy, bestSell)) {
                    break;
                }

                int executableQuantity = Math.min(bestBuy.remainingQuantity(), bestSell.remainingQuantity());
                double executionPrice = determineExecutionPrice(bestBuy, bestSell);
                createTrade(bestBuy, bestSell, executableQuantity, executionPrice);

                bestBuy.applyFill(executableQuantity);
                bestSell.applyFill(executableQuantity);

                if (bestBuy.isFilled()) {
                    buyOrders.poll();
                }
                if (bestSell.isFilled()) {
                    sellOrders.poll();
                }
            }
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

        private void createTrade(Order buy, Order sell, int quantity, double price) {
            String tradeId = symbol + "-T" + tradeIdGenerator.getAndIncrement();
            Trade trade = new Trade(tradeId, buy.orderId, sell.orderId, symbol, quantity, price, System.currentTimeMillis());
            trades.add(trade);
        }

        List<Trade> getTrades() {
            return new ArrayList<>(trades);
        }

        @Override
        public String toString() {
            return String.format("OrderBook{%s, buy=%d, sell=%d, trades=%d}",
                    symbol, buyOrders.size(), sellOrders.size(), trades.size());
        }
    }

    static class OrderMatchingEngine {
        private final Map<String, OrderBook> books = new HashMap<>();

        OrderBook getOrCreateBook(String symbol) {
            return books.computeIfAbsent(symbol, OrderBook::new);
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
