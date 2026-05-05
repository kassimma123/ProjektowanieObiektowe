package pl.edu.agh.internetshop;

public class BuyerNameStrategy implements SearchStrategy{
    private final String expectedName;

    public BuyerNameStrategy(String expectedName) {
        this.expectedName = expectedName;
    }

    @Override
    public boolean filter(Order order) {
        if (order.getShipment() != null && order.getShipment().getRecipientAddress() != null ){
            return order.getShipment().getRecipientAddress().getName().equalsIgnoreCase(expectedName);
        }
        return false;
    }
    
}
