package model;

import java.time.LocalDate;

public class SalesData {
    private String saleId;
    private String productId;
    private int quantitySold;
    private double revenue;
    private LocalDate saleDate;

    public SalesData() {}

    public SalesData(String saleId, String productId, int quantitySold, double revenue, LocalDate saleDate) {
        this.saleId = saleId;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.revenue = revenue;
        this.saleDate = saleDate;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public LocalDate getDate() {
        return saleDate;
    }

    public void setDate(LocalDate date) {
        this.saleDate = date;
    }
}
