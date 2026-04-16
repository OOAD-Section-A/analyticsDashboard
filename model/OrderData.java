package model;

import java.time.LocalDate;

public class OrderData {
    private String orderId;
    private String customerId;
    private String status;
    private double totalAmount;
    private LocalDate orderDate;

    public OrderData(String orderId, String customerId, String status, double totalAmount, LocalDate orderDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDate getOrderDate() { return orderDate; }
}