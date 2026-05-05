package pl.edu.agh.internetshop;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static pl.edu.agh.internetshop.util.CustomAssertions.assertBigDecimalCompareValue;

public class OrderTest {

	private Order getOrderWithMockedProduct() {
		Product product = mock(Product.class);
		return new Order(product);
	}

	@Test
	public void testOrderDiscount() {
		// given
		// Tworzymy dwa zmockowane produkty
		Product p1 = mock(Product.class);
		given(p1.getPrice()).willReturn(BigDecimal.valueOf(100));

		Product p2 = mock(Product.class);
		given(p2.getPrice()).willReturn(BigDecimal.valueOf(50));

		Order order = new Order(p1);
		order.addProduct(p2);

		// when
		// Nakładamy 10% rabatu na zamówienie 
		order.setDiscount(BigDecimal.valueOf(0.10));

		// then
		// Oczekujemy, że 150 zł - 10% (15 zł) = 135 zł
		assertBigDecimalCompareValue(BigDecimal.valueOf(135), order.getPrice());
	}

	@Test
	public void testGetPriceWithMultipleProducts() {
		// given
		Product p1 = mock(Product.class);
		Product p2 = mock(Product.class);
		given(p1.getPrice()).willReturn(BigDecimal.valueOf(10));
		given(p2.getPrice()).willReturn(BigDecimal.valueOf(20));

		Order order = new Order(p1); // Tu na razie mamy problem, bo konstruktor przyjmuje tylko jeden
		order.addProduct(p2); // Ta metoda jeszcze nie istnieje! (Błąd kompilacji)

		// when
		BigDecimal totalPrice = order.getPrice();

		// then
		assertBigDecimalCompareValue(BigDecimal.valueOf(30), totalPrice);
	}

	@Test
	public void testPriceWithTaxesNewVateRate() {
		Order order = getOrderWithCertainProductPrice(100);
		BigDecimal priceWithTaxes = order.getPriceWithTaxes();
		assertBigDecimalCompareValue(BigDecimal.valueOf(123.00), priceWithTaxes);
	}

	@Test
	public void testGetProductThroughOrder() {
		// given
		Product expectedProduct = mock(Product.class);
		Order order = new Order(expectedProduct);

		// when
		// Pobieramy całą listę produktów
		List<Product> actualProducts = order.getProducts();

		// then
		// Sprawdzamy, czy lista nie jest pusta, ma 1 element i czy jest to nasz produkt
		assertEquals(1, actualProducts.size());
		assertTrue(actualProducts.contains(expectedProduct));
	}

	@Test
	public void testSetShipment() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();
		Shipment expectedShipment = mock(Shipment.class);

		// when
		order.setShipment(expectedShipment);

		// then
		assertSame(expectedShipment, order.getShipment());
	}

	@Test
	public void testShipmentWithoutSetting() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();

		// when

		// then
		assertNull(order.getShipment());
	}

	@Test
	public void testGetPrice() throws Exception {
		// given
		BigDecimal expectedProductPrice = BigDecimal.valueOf(1000);
		Product product = mock(Product.class);
		given(product.getPrice()).willReturn(expectedProductPrice);
		Order order = new Order(product);

		// when
		BigDecimal actualProductPrice = order.getPrice();

		// then
		assertBigDecimalCompareValue(expectedProductPrice, actualProductPrice);
	}

	private Order getOrderWithCertainProductPrice(double productPriceValue) {
		BigDecimal productPrice = BigDecimal.valueOf(productPriceValue);
		Product product = mock(Product.class);
		given(product.getPrice()).willReturn(productPrice);
		return new Order(product);
	}

	@Test
	public void testPriceWithTaxesWithoutRoundUp() {
		// given

		// when
		Order order = getOrderWithCertainProductPrice(2); // 2 PLN

		// then
		assertBigDecimalCompareValue(order.getPriceWithTaxes(), BigDecimal.valueOf(2.46)); // 2.44 PLN
	}

	@Test
	public void testPriceWithTaxesWithRoundDown() {
		// given

		// when
		Order order = getOrderWithCertainProductPrice(0.01); // 0.01 PLN

		// then
		assertBigDecimalCompareValue(order.getPriceWithTaxes(), BigDecimal.valueOf(0.01)); // 0.01 PLN

	}

	@Test
	public void testPriceWithTaxesWithRoundUp() {
		// given

		// when
		Order order = getOrderWithCertainProductPrice(0.03); // 0.03 PLN

		// then
		assertBigDecimalCompareValue(order.getPriceWithTaxes(), BigDecimal.valueOf(0.04)); // 0.04 PLN

	}

	@Test
	public void testSetShipmentMethod() {
		// given
		Order order = getOrderWithMockedProduct();
		ShipmentMethod surface = mock(SurfaceMailBus.class);

		// when
		order.setShipmentMethod(surface);

		// then
		assertSame(surface, order.getShipmentMethod());
	}

	@Test
	public void testSending() {
		// given
		Order order = getOrderWithMockedProduct();
		SurfaceMailBus surface = mock(SurfaceMailBus.class);
		Shipment shipment = mock(Shipment.class);
		given(shipment.isShipped()).willReturn(true);

		// when
		order.setShipmentMethod(surface);
		order.setShipment(shipment);
		order.send();

		// then
		assertTrue(order.isSent());
	}

	@Test
	public void testIsSentWithoutSending() {
		// given
		Order order = getOrderWithMockedProduct();
		Shipment shipment = mock(Shipment.class);
		given(shipment.isShipped()).willReturn(true);

		// when

		// then
		assertFalse(order.isSent());
	}

	@Test
	public void testWhetherIdExists() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();

		// when

		// then
		assertNotNull(order.getId());
	}

	@Test
	public void testSetPaymentMethod() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();
		PaymentMethod paymentMethod = mock(MoneyTransferPaymentTransaction.class);

		// when
		order.setPaymentMethod(paymentMethod);

		// then
		assertSame(paymentMethod, order.getPaymentMethod());
	}

	@Test
	public void testPaying() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();
		PaymentMethod paymentMethod = mock(MoneyTransferPaymentTransaction.class);
		given(paymentMethod.commit(any(MoneyTransfer.class))).willReturn(true);
		MoneyTransfer moneyTransfer = mock(MoneyTransfer.class);
		given(moneyTransfer.isCommitted()).willReturn(true);

		// when
		order.setPaymentMethod(paymentMethod);
		order.pay(moneyTransfer);

		// then
		assertTrue(order.isPaid());
	}

	@Test
	public void testIsPaidWithoutPaying() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();

		// when

		// then
		assertFalse(order.isPaid());
	}
}
