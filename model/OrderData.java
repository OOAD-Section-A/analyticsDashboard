package model;

public class OrderData {
    public String orderId;
    public String productId;
    public int quantity;
    public OrderData(String orderId, String productId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
