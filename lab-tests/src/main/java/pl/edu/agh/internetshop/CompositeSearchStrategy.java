package pl.edu.agh.internetshop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeSearchStrategy implements SearchStrategy {
    private final List<SearchStrategy> strategies = new ArrayList<>();

    // Konstruktor, który pozwala przekazać wiele strategii po przecinku
    public CompositeSearchStrategy(SearchStrategy... searchStrategies) {
        this.strategies.addAll(Arrays.asList(searchStrategies));
    }

    @Override
    public boolean filter(Order order) {
        for (SearchStrategy strategy : strategies) {
            if (!strategy.filter(order)) {
                return false; // Jeśli chociaż jeden filtr nie pasuje, odrzucamy zamówienie
            }
        }
        return true; // Wszystkie filtry pasują
    }
}