package model;

public class Shipment {
    public String shipmentId;
    public String orderId;
    public String status;
    public Shipment(String shipmentId, String orderId, String status) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.status = status;
    }
}
