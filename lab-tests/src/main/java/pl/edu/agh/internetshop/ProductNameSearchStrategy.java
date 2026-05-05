package pl.edu.agh.internetshop;

public class ProductNameSearchStrategy implements SearchStrategy {
    private final String expectedName;

    public ProductNameSearchStrategy(String expectedName) {
        this.expectedName = expectedName;
    }

    @Override
    public boolean filter(Order order) {
        for (Product product : order.getProducts()) {
            if (product.getName().equalsIgnoreCase(expectedName)) {
                return true; // Zwraca true, jeśli znajdzie produkt o takiej nazwie
            }
        }
        return false;
    }
}