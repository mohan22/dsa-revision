import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class Stock {
    private final String symbol;
    private final String name;
    private AtomicReference<BigDecimal> currentPrice;
    private AtomicReference<LocalDateTime> lastUpdated;

    public Stock(String symbol, String name, BigDecimal initialPrice) {
        if (symbol == null || symbol.isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty");
        }
        if (initialPrice.signum() <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = new AtomicReference<>(initialPrice);
        this.lastUpdated = new AtomicReference<>(LocalDateTime.now());
    }

    public void updatePrice(BigDecimal newPrice) {
        if (newPrice.signum() <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.currentPrice.set(newPrice);
        this.lastUpdated.set(LocalDateTime.now());
    }

    public BigDecimal getCurrentPrice() {
        return this.currentPrice.get();
    }

    public LocalDateTime getLastUpdated() {
        return this.lastUpdated.get();
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice.get() +
                ", lastUpdated=" + lastUpdated.get() +
                '}';
    }
}
