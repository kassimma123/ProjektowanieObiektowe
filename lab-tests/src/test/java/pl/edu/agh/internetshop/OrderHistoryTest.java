package pl.edu.agh.internetshop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;

public class OrderHistoryTest {

    @Test
    public void testAddOrderToHistory() {
        // given
        OrderHistory history = new OrderHistory(); // Kod zaświeci się na czerwono - klasa nie istnieje
        Order mockOrder = mock(Order.class);

        // when
        history.addOrder(mockOrder);

        // then
        assertTrue(history.getOrders().contains(mockOrder));
        assertEquals(1, history.getOrders().size());
    }

    @Test
    public void testSearchByProductName() {
        // given
        OrderHistory history = new OrderHistory();

        // Tworzymy zamówienie z produktem A
        Order orderA = mock(Order.class);
        Product productA = mock(Product.class);
        // import static org.mockito.BDDMockito.given;
        org.mockito.BDDMockito.given(productA.getName()).willReturn("Laptop");
        org.mockito.BDDMockito.given(orderA.getProducts()).willReturn(java.util.Collections.singletonList(productA));

        // Tworzymy zamówienie z produktem B
        Order orderB = mock(Order.class);
        Product productB = mock(Product.class);
        org.mockito.BDDMockito.given(productB.getName()).willReturn("Myszka");
        org.mockito.BDDMockito.given(orderB.getProducts()).willReturn(java.util.Collections.singletonList(productB));

        history.addOrder(orderA);
        history.addOrder(orderB);

        SearchStrategy strategy = mock(SearchStrategy.class);
        org.mockito.BDDMockito.given(strategy.filter(orderA)).willReturn(true);
        org.mockito.BDDMockito.given(strategy.filter(orderB)).willReturn(false);

        // when
        // Szukamy używając strategii
        List<Order> result = history.search(strategy);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(orderA));
    }

    @Test
    public void testSearchByMultipleCriteria() {
        // given
        OrderHistory history = new OrderHistory();

        // Konfigurujemy zamówienie
        Product product = mock(Product.class);
        org.mockito.BDDMockito.given(product.getName()).willReturn("Laptop");

        Order order = mock(Order.class);
        org.mockito.BDDMockito.given(order.getProducts()).willReturn(java.util.Collections.singletonList(product));
        org.mockito.BDDMockito.given(order.getPrice()).willReturn(BigDecimal.valueOf(5000));

        history.addOrder(order);

        // when
        // Tworzymy kompozyt z dwóch kryteriów: nazwy i ceny
        SearchStrategy nameStrategy = new ProductNameSearchStrategy("Laptop");
        SearchStrategy priceStrategy = new PriceSearchStrategy(BigDecimal.valueOf(5000));
        SearchStrategy combinedStrategy = new CompositeSearchStrategy(nameStrategy, priceStrategy);

        List<Order> result = history.search(combinedStrategy);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(order));
    }
}