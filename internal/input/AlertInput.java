package internal.input;

import model.InventoryData;
import model.ShipmentData;

import java.util.List;

public class AlertInput {
    private final List<InventoryData> inventory;
    private final List<ShipmentData> shipments;

    public AlertInput(List<InventoryData> inventory, List<ShipmentData> shipments) {
        this.inventory = inventory;
        this.shipments = shipments;
    }

    public List<InventoryData> getInventory() { return inventory; }
    public List<ShipmentData> getShipments() { return shipments; }
}