package model;

public class ShipmentData {
    public String shipmentId;
    public String orderId;
    public String status;
    public ShipmentData(String shipmentId, String orderId, String status) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.status = status;
    }
}
