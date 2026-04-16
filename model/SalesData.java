package model;

import java.time.LocalDate;

public class SalesData {
    private String saleId;
    private String productId;
    private int quantitySold;
    private double revenue;
    private LocalDate saleDate;

    public SalesData(String saleId, String productId, int quantitySold, double revenue, LocalDate saleDate) {
        this.saleId = saleId;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.revenue = revenue;
        this.saleDate = saleDate;
    }

    public String getSaleId() { return saleId; }
    public String getProductId() { return productId; }
    public int getQuantitySold() { return quantitySold; }
    public double getRevenue() { return revenue; }
    public LocalDate getSaleDate() { return saleDate; }
}