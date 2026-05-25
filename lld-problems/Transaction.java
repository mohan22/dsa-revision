import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    public enum TransactionType {
        BUY, SELL
    }

    private final String transactionId;
    private final String orderId;
    private final String userId;
    private final String symbol;
    private final TransactionType transactionType;
    private final int quantity;
    private final BigDecimal pricePerShare;
    private final BigDecimal totalAmount;
    private final LocalDateTime timestamp;

    public Transaction(String orderId, String userId, String symbol, 
                      TransactionType transactionType, int quantity, BigDecimal pricePerShare) {
        this.transactionId = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.userId = userId;
        this.symbol = symbol;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.totalAmount = pricePerShare.multiply(new BigDecimal(quantity));
        this.timestamp = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPricePerShare() {
        return pricePerShare;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", transactionType=" + transactionType +
                ", quantity=" + quantity +
                ", pricePerShare=" + pricePerShare +
                ", totalAmount=" + totalAmount +
                ", timestamp=" + timestamp +
                '}';
    }
}
