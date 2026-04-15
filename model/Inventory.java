package model;

public class Inventory {
    public String productId;
    public int quantity;
    public Inventory(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
