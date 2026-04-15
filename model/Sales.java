package model;

public class Sales {
    public String productId;
    public int unitsSold;
    public Sales(String productId, int unitsSold) {
        this.productId = productId;
        this.unitsSold = unitsSold;
    }
}
