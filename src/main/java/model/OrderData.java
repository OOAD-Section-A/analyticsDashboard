package model;

import java.time.LocalDate;

public class OrderData {
    private String orderId;
    private String customerId;
    private double totalAmount;
    private LocalDate orderDate;
    private String status;

    public OrderData() {}

    public OrderData(String orderId, String customerId, double totalAmount, LocalDate orderDate, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderStatus() {
        return status;
    }

    public void setOrderStatus(String orderStatus) {
        this.status = orderStatus;
    }
}
