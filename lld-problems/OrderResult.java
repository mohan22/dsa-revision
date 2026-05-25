import java.math.BigDecimal;

public class OrderResult {
    public enum Status {
        SUCCESS, INSUFFICIENT_FUNDS, INSUFFICIENT_HOLDINGS, INVALID_ORDER, ERROR
    }

    private final Status status;
    private final String orderId;
    private final String message;

    public OrderResult(Status status, String orderId, String message) {
        this.status = status;
        this.orderId = orderId;
        this.message = message;
    }

    public static OrderResult success(String orderId) {
        return new OrderResult(Status.SUCCESS, orderId, "Order placed successfully");
    }

    public static OrderResult insufficientFunds() {
        return new OrderResult(Status.INSUFFICIENT_FUNDS, null, "Insufficient funds to execute BUY order");
    }

    public static OrderResult insufficientHoldings() {
        return new OrderResult(Status.INSUFFICIENT_HOLDINGS, null, "Insufficient holdings to execute SELL order");
    }

    public static OrderResult invalidOrder(String message) {
        return new OrderResult(Status.INVALID_ORDER, null, message);
    }

    public static OrderResult error(String message) {
        return new OrderResult(Status.ERROR, null, message);
    }

    public Status getStatus() {
        return status;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    @Override
    public String toString() {
        return "OrderResult{" +
                "status=" + status +
                ", orderId='" + orderId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
