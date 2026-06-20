import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Order {
    public enum OrderType {
        BUY, SELL
    }

    public enum OrderStatus {
        PENDING, PARTIALLY_FILLED, COMPLETED, CANCELLED
    }

    private final String orderId;
    private final String userId;
    private final String symbol;
    private final OrderType orderType;
    private final int quantity;
    private final BigDecimal pricePerShare;
    private int filledQuantity;
    private OrderStatus status;
    private BigDecimal executionPrice;
    private final LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public Order(String userId, String symbol, OrderType orderType, 
                 int quantity, BigDecimal pricePerShare) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (pricePerShare.signum() <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        this.orderId = UUID.randomUUID().toString();
        this.userId = userId;
        this.symbol = symbol;
        this.orderType = orderType;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.filledQuantity = 0;
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public synchronized void fillOrder(int filledQty, BigDecimal executionPrice) {
        if (filledQty < 0 || filledQty > quantity - filledQuantity) {
            throw new IllegalArgumentException("Invalid fill quantity");
        }

        this.filledQuantity += filledQty;
        this.executionPrice = executionPrice;

        if (this.filledQuantity == this.quantity) {
            this.status = OrderStatus.COMPLETED;
            this.completedAt = LocalDateTime.now();
        } else if (this.filledQuantity > 0) {
            this.status = OrderStatus.PARTIALLY_FILLED;
        }
    }

    public synchronized void cancel() {
        if (this.status == OrderStatus.PENDING || this.status == OrderStatus.PARTIALLY_FILLED) {
            this.status = OrderStatus.CANCELLED;
            this.completedAt = LocalDateTime.now();
        }
    }

    public synchronized int getRemainingQuantity() {
        return quantity - filledQuantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPricePerShare() {
        return pricePerShare;
    }

    public int getFilledQuantity() {
        return filledQuantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getExecutionPrice() {
        return executionPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", orderType=" + orderType +
                ", quantity=" + quantity +
                ", filledQuantity=" + filledQuantity +
                ", pricePerShare=" + pricePerShare +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
