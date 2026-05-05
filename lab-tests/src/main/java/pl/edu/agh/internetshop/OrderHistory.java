package pl.edu.agh.internetshop;
import java.util.ArrayList;
import java.util.List;
public class OrderHistory {
    private final List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Order> search(SearchStrategy strategy) {
        return orders.stream()
        .filter(strategy::filter)
        .collect(java.util.stream.Collectors.toList());
    }
}
