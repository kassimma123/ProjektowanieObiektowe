package pl.edu.agh.internetshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.edu.agh.internetshop.util.CustomAssertions.assertBigDecimalCompareValue;

import java.math.BigDecimal;

public class ProductTest {

    private static final String NAME = "Mr. Sparkle";
    private static final BigDecimal PRICE = BigDecimal.valueOf(1);

    @Test
    public void testProductDiscount() {
        // given
        // Tworzymy produkt za 100 PLN
        Product product = new Product("Koszulka", BigDecimal.valueOf(100));

        // when
        // Ustawiamy 10% rabatu (0.10) 
        product.setDiscount(BigDecimal.valueOf(0.10));

        // then
        // Oczekujemy, że cena po rabacie wyniesie 90 PLN
        assertBigDecimalCompareValue(BigDecimal.valueOf(90), product.getPrice());
    }

    @Test
    public void testProductName() throws Exception {
        // given

        // when
        Product product = new Product(NAME, PRICE);

        // then
        assertEquals(NAME, product.getName());
    }

    @Test
    public void testProductPrice() throws Exception {
        // given

        // when
        Product product = new Product(NAME, PRICE);

        // then
        assertBigDecimalCompareValue(product.getPrice(), PRICE);
    }
}