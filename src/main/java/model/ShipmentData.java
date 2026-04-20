package model;

import java.time.LocalDate;

public class ShipmentData {
    private String shipmentId;
    private String orderId;
    private String status;
    private LocalDate dispatchDate;
    private LocalDate deliveryDate;

    public ShipmentData() {}

    public ShipmentData(String shipmentId, String orderId, String status, LocalDate dispatchDate, LocalDate deliveryDate) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.status = status;
        this.dispatchDate = dispatchDate;
        this.deliveryDate = deliveryDate;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(LocalDate dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDate getShipmentDate() {
        return dispatchDate;
    }

    public void setShipmentDate(LocalDate shipmentDate) {
        this.dispatchDate = shipmentDate;
    }

    public boolean isDelayed() {
        return "DELAYED".equalsIgnoreCase(status)
                || (deliveryDate == null && dispatchDate != null && dispatchDate.isBefore(LocalDate.now().minusDays(3)));
    }
}
