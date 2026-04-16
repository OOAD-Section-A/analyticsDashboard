package com.jackfruit.scm.reporting.model;

import java.time.LocalDate;

public class ShipmentData {
    private String shipmentId;
    private String orderId;
    private String status;
    private LocalDate expectedDate;
    private LocalDate actualDate;

    public ShipmentData(String shipmentId, String orderId, String status, LocalDate expectedDate, LocalDate actualDate) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.status = status;
        this.expectedDate = expectedDate;
        this.actualDate = actualDate;
    }

    public String getShipmentId() { return shipmentId; }
    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public LocalDate getExpectedDate() { return expectedDate; }
    public LocalDate getActualDate() { return actualDate; }
}