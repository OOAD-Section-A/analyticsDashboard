package model;

import java.time.LocalDate;

public class ShipmentData {
    private String shipmentId;
    private String orderId;
    private String status;
    private LocalDate dispatchDate;
    private LocalDate deliveryDate;
    private boolean delayed;

    public ShipmentData(String shipmentId, String orderId, String status, LocalDate dispatchDate, LocalDate deliveryDate, boolean delayed) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.status = status;
        this.dispatchDate = dispatchDate;
        this.deliveryDate = deliveryDate;
        this.delayed = delayed;
    }

    public String getShipmentId() { return shipmentId; }
    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public LocalDate getDispatchDate() { return dispatchDate; }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public boolean isDelayed() { return delayed; }
}
