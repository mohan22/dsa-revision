import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    private final String userId;
    private final String userName;
    private final String email;
    private BigDecimal cash;
    private final Map<String, Integer> portfolio; // symbol -> quantity
    private final LocalDateTime createdAt;

    public User(String userName, String email, BigDecimal initialCash) {
        this.userId = UUID.randomUUID().toString();
        this.userName = userName;
        this.email = email;
        this.cash = initialCash;
        this.portfolio = new ConcurrentHashMap<>();
        this.createdAt = LocalDateTime.now();
    }

    public synchronized void addCash(BigDecimal amount) {
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.cash = this.cash.add(amount);
    }

    public synchronized boolean withdrawCash(BigDecimal amount) {
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.cash.compareTo(amount) < 0) {
            return false; // Insufficient funds
        }
        this.cash = this.cash.subtract(amount);
        return true;
    }

    public synchronized BigDecimal getCash() {
        return this.cash;
    }

    public void addHolding(String symbol, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        portfolio.merge(symbol, quantity, Integer::sum);
    }

    public boolean removeHolding(String symbol, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        Integer currentHolding = portfolio.getOrDefault(symbol, 0);
        if (currentHolding < quantity) {
            return false; // Insufficient holdings
        }
        if (currentHolding - quantity == 0) {
            portfolio.remove(symbol);
        } else {
            portfolio.put(symbol, currentHolding - quantity);
        }
        return true;
    }

    public int getHolding(String symbol) {
        return portfolio.getOrDefault(symbol, 0);
    }

    public Map<String, Integer> getPortfolio() {
        return new HashMap<>(portfolio);
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", cash=" + cash +
                ", portfolio=" + portfolio +
                ", createdAt=" + createdAt +
                '}';
    }
}
