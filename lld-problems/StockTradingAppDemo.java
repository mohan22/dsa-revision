import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

public class StockTradingAppDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Stock Trading App - Low Level Design Implementation ===\n");

        // Initialize the exchange
        StockExchange exchange = new StockExchange();

        // Register stocks
        System.out.println("1. Registering stocks...");
        exchange.registerStock("AAPL", "Apple Inc.", new BigDecimal("150.00"));
        exchange.registerStock("GOOGL", "Alphabet Inc.", new BigDecimal("2800.00"));
        exchange.registerStock("MSFT", "Microsoft Corp.", new BigDecimal("300.00"));
        System.out.println("   ✓ 3 stocks registered\n");

        // Register users
        System.out.println("2. Registering users...");
        User user1 = new User("Alice", "alice@example.com", new BigDecimal("50000.00"));
        User user2 = new User("Bob", "bob@example.com", new BigDecimal("100000.00"));
        User user3 = new User("Charlie", "charlie@example.com", new BigDecimal("75000.00"));

        exchange.registerUser(user1);
        exchange.registerUser(user2);
        exchange.registerUser(user3);
        System.out.println("   ✓ 3 users registered");
        System.out.println("   User1 (Alice): $" + user1.getCash());
        System.out.println("   User2 (Bob): $" + user2.getCash());
        System.out.println("   User3 (Charlie): $" + user3.getCash() + "\n");

        // Place orders
        System.out.println("3. Placing orders...");
        
        // Alice buys 100 AAPL @ $150
        OrderResult result1 = exchange.placeOrder(user1.getUserId(), "AAPL", 
                Order.OrderType.BUY, 100, new BigDecimal("150.00"));
        System.out.println("   Alice BUY 100 AAPL @ $150: " + result1.getStatus());

        // Give time for matching
        Thread.sleep(200);

        // Bob sells 100 AAPL @ $150 (should match with Alice's order)
        OrderResult result2 = exchange.placeOrder(user2.getUserId(), "AAPL", 
                Order.OrderType.SELL, 100, new BigDecimal("150.00"));
        System.out.println("   Bob SELL 100 AAPL @ $150: " + result2.getStatus());

        Thread.sleep(200);

        System.out.println("\n4. After first trade:");
        System.out.println("   Alice cash: $" + user1.getCash());
        System.out.println("   Alice AAPL holdings: " + user1.getHolding("AAPL"));
        System.out.println("   Bob cash: $" + user2.getCash());
        System.out.println("   Bob AAPL holdings: " + user2.getHolding("AAPL"));

        // Charlie buys GOOGL
        OrderResult result3 = exchange.placeOrder(user3.getUserId(), "GOOGL", 
                Order.OrderType.BUY, 10, new BigDecimal("2800.00"));
        System.out.println("\n   Charlie BUY 10 GOOGL @ $2800: " + result3.getStatus());

        Thread.sleep(200);

        // Sell GOOGL
        OrderResult result4 = exchange.placeOrder(user2.getUserId(), "GOOGL", 
                Order.OrderType.SELL, 10, new BigDecimal("2800.00"));
        System.out.println("   Bob SELL 10 GOOGL @ $2800: " + result4.getStatus());

        Thread.sleep(200);

        System.out.println("\n5. After second trade:");
        System.out.println("   Charlie cash: $" + user3.getCash());
        System.out.println("   Charlie GOOGL holdings: " + user3.getHolding("GOOGL"));
        System.out.println("   Bob cash: $" + user2.getCash());
        System.out.println("   Bob GOOGL holdings: " + user2.getHolding("GOOGL"));

        // Test partial fill
        System.out.println("\n6. Testing partial fill...");
        OrderResult result5 = exchange.placeOrder(user1.getUserId(), "MSFT", 
                Order.OrderType.BUY, 50, new BigDecimal("300.00"));
        System.out.println("   Alice BUY 50 MSFT @ $300: " + result5.getStatus());

        Thread.sleep(200);

        OrderResult result6 = exchange.placeOrder(user2.getUserId(), "MSFT", 
                Order.OrderType.SELL, 30, new BigDecimal("300.00"));
        System.out.println("   Bob SELL 30 MSFT @ $300: " + result6.getStatus());

        Thread.sleep(200);

        System.out.println("\n7. After partial fill:");
        System.out.println("   Alice cash: $" + user1.getCash());
        System.out.println("   Alice MSFT holdings: " + user1.getHolding("MSFT"));

        // Display order books
        System.out.println("\n8. Current order books:");
        displayOrderBook(exchange, "AAPL");
        displayOrderBook(exchange, "MSFT");

        // Display transaction history
        System.out.println("\n9. Transaction history:");
        List<Transaction> transactions = exchange.getTransactionHistory();
        for (Transaction t : transactions) {
            System.out.println("   [" + t.getTransactionType() + "] " + t.getQuantity() + 
                    " " + t.getSymbol() + " @ $" + t.getPricePerShare() + 
                    " (User: " + t.getUserId().substring(0, 8) + "...)");
        }

        // Test insufficient funds
        System.out.println("\n10. Testing error cases...");
        OrderResult insufficientFunds = exchange.placeOrder(user3.getUserId(), "GOOGL",
                Order.OrderType.BUY, 1000, new BigDecimal("2800.00"));
        System.out.println("   Insufficient funds test: " + insufficientFunds.getStatus());

        // Final portfolio summary
        System.out.println("\n11. Final portfolio summary:");
        System.out.println("   Alice Portfolio:");
        System.out.println("     Cash: $" + user1.getCash());
        System.out.println("     Holdings: " + user1.getPortfolio());
        System.out.println("   Bob Portfolio:");
        System.out.println("     Cash: $" + user2.getCash());
        System.out.println("     Holdings: " + user2.getPortfolio());
        System.out.println("   Charlie Portfolio:");
        System.out.println("     Cash: $" + user3.getCash());
        System.out.println("     Holdings: " + user3.getPortfolio());

        exchange.shutdown();
        System.out.println("\n=== Demo completed successfully ===");
    }

    private static void displayOrderBook(StockExchange exchange, String symbol) {
        OrderBook.OrderBookSnapshot snapshot = exchange.getOrderBook(symbol);
        if (snapshot == null || (snapshot.getBuyOrders().isEmpty() && snapshot.getSellOrders().isEmpty())) {
            System.out.println("   " + symbol + " order book: EMPTY");
            return;
        }

        System.out.println("   " + symbol + " order book:");
        if (!snapshot.getBuyOrders().isEmpty()) {
            System.out.println("     BUY orders:");
            for (Order order : snapshot.getBuyOrders()) {
                System.out.println("       Qty: " + order.getQuantity() + 
                        ", Remaining: " + order.getRemainingQuantity() +
                        ", Price: $" + order.getPricePerShare() + 
                        ", Status: " + order.getStatus());
            }
        }
        if (!snapshot.getSellOrders().isEmpty()) {
            System.out.println("     SELL orders:");
            for (Order order : snapshot.getSellOrders()) {
                System.out.println("       Qty: " + order.getQuantity() + 
                        ", Remaining: " + order.getRemainingQuantity() +
                        ", Price: $" + order.getPricePerShare() + 
                        ", Status: " + order.getStatus());
            }
        }
    }
}
