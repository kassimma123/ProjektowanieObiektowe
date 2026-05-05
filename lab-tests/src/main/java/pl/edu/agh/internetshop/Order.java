package pl.edu.agh.internetshop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private static final BigDecimal TAX_VALUE = BigDecimal.valueOf(1.23);
    private final UUID id;
    private final List<Product> products = new ArrayList<>();
    private boolean paid;
    private Shipment shipment;
    private ShipmentMethod shipmentMethod;
    private PaymentMethod paymentMethod;
    private BigDecimal discount;

    public Order(Product product) {
        this.products.add(product);
        id = UUID.randomUUID();
        paid = false;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public UUID getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isSent() {
        return shipment != null && shipment.isShipped();
    }

    public boolean isPaid() {
        return paid;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public BigDecimal getPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Product p : products) {
            sum = sum.add(p.getPrice());
        }
        BigDecimal discountValue = sum.multiply(discount);
        return sum.subtract(discountValue).setScale(Product.PRICE_PRECISION, Product.ROUND_STRATEGY);
    }

    public BigDecimal getPriceWithTaxes() {
        return getPrice().multiply(TAX_VALUE).setScale(Product.PRICE_PRECISION, Product.ROUND_STRATEGY);
    }

    public ShipmentMethod getShipmentMethod() {
        return shipmentMethod;
    }

    public void setShipmentMethod(ShipmentMethod shipmentMethod) {
        this.shipmentMethod = shipmentMethod;
    }

    public void send() {
        boolean sentSuccesful = getShipmentMethod().send(shipment, shipment.getSenderAddress(),
                shipment.getRecipientAddress());
        shipment.setShipped(sentSuccesful);
    }

    public void pay(MoneyTransfer moneyTransfer) {
        moneyTransfer.setCommitted(getPaymentMethod().commit(moneyTransfer));
        paid = moneyTransfer.isCommitted();
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }
}
