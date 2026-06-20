import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class OrderBook {
    private final String symbol;
    private final PriorityQueue<Order> buyOrders;  // Max heap - highest price first
    private final PriorityQueue<Order> sellOrders; // Min heap - lowest price first
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public OrderBook(String symbol) {
        this.symbol = symbol;
        this.buyOrders = new PriorityQueue<>((o1, o2) -> {
            int priceComparison = o2.getPricePerShare().compareTo(o1.getPricePerShare());
            if (priceComparison != 0) return priceComparison;
            return o1.getCreatedAt().compareTo(o2.getCreatedAt()); // FIFO for same price
        });
        this.sellOrders = new PriorityQueue<>((o1, o2) -> {
            int priceComparison = o1.getPricePerShare().compareTo(o2.getPricePerShare());
            if (priceComparison != 0) return priceComparison;
            return o1.getCreatedAt().compareTo(o2.getCreatedAt()); // FIFO for same price
        });
    }

    public void addOrder(Order order) {
        lock.writeLock().lock();
        try {
            if (order.getOrderType() == Order.OrderType.BUY) {
                buyOrders.offer(order);
            } else {
                sellOrders.offer(order);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeOrder(Order order) {
        lock.writeLock().lock();
        try {
            if (order.getOrderType() == Order.OrderType.BUY) {
                buyOrders.remove(order);
            } else {
                sellOrders.remove(order);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Order> matchOrders() {
        lock.writeLock().lock();
        try {
            List<Order> matchedOrders = new ArrayList<>();

            while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
                Order buyOrder = buyOrders.peek();
                Order sellOrder = sellOrders.peek();

                if (buyOrder.getPricePerShare().compareTo(sellOrder.getPricePerShare()) < 0) {
                    break; // No match possible
                }

                int buyRemaining = buyOrder.getRemainingQuantity();
                int sellRemaining = sellOrder.getRemainingQuantity();
                int matchQuantity = Math.min(buyRemaining, sellRemaining);

                // Execute at sell price (price of the sell order that came first)
                BigDecimal executionPrice = sellOrder.getPricePerShare();

                buyOrder.fillOrder(matchQuantity, executionPrice);
                sellOrder.fillOrder(matchQuantity, executionPrice);

                matchedOrders.add(buyOrder);
                matchedOrders.add(sellOrder);

                if (buyOrder.getStatus() == Order.OrderStatus.COMPLETED) {
                    buyOrders.poll();
                }
                if (sellOrder.getStatus() == Order.OrderStatus.COMPLETED) {
                    sellOrders.poll();
                }
            }

            return matchedOrders;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public OrderBookSnapshot getSnapshot() {
        lock.readLock().lock();
        try {
            List<Order> buyOrdersCopy = new ArrayList<>(buyOrders);
            List<Order> sellOrdersCopy = new ArrayList<>(sellOrders);
            return new OrderBookSnapshot(symbol, buyOrdersCopy, sellOrdersCopy);
        } finally {
            lock.readLock().unlock();
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public static class OrderBookSnapshot {
        private final String symbol;
        private final List<Order> buyOrders;
        private final List<Order> sellOrders;

        public OrderBookSnapshot(String symbol, List<Order> buyOrders, List<Order> sellOrders) {
            this.symbol = symbol;
            this.buyOrders = new ArrayList<>(buyOrders);
            this.sellOrders = new ArrayList<>(sellOrders);
        }

        public String getSymbol() {
            return symbol;
        }

        public List<Order> getBuyOrders() {
            return new ArrayList<>(buyOrders);
        }

        public List<Order> getSellOrders() {
            return new ArrayList<>(sellOrders);
        }

        @Override
        public String toString() {
            return "OrderBookSnapshot{" +
                    "symbol='" + symbol + '\'' +
                    ", buyOrders=" + buyOrders +
                    ", sellOrders=" + sellOrders +
                    '}';
        }
    }
}
