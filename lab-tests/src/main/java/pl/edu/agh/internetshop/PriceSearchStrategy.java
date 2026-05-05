package pl.edu.agh.internetshop;

import java.math.BigDecimal;

public class PriceSearchStrategy implements SearchStrategy {
    private final BigDecimal expectedPrice;

    public PriceSearchStrategy(BigDecimal expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    @Override
    public boolean filter(Order order) {
        // Porównujemy ceny. compareTo zwraca 0, gdy wartości są równe.
        return order.getPrice().compareTo(expectedPrice) == 0;
    }
}