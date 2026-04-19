package engine;

import internal.input.AlertInput;
import model.InventoryData;
import model.ShipmentData;

import java.util.ArrayList;
import java.util.List;

public class AlertGenerator {

    private static final int LOW_INVENTORY_THRESHOLD = 10;

    public List<String> generate(AlertInput input) {
        List<InventoryData> inventory = input.getInventory();
        List<ShipmentData> shipments = input.getShipments();
        List<String> alerts = new ArrayList<>();

        inventory.stream()
                .filter(i -> i.getQuantity() < LOW_INVENTORY_THRESHOLD)
                .forEach(i -> alerts.add("LOW INVENTORY: Product [" + i.getProductName()
                        + "] has only " + i.getQuantity() + " units left."));

        shipments.stream()
                .filter(ShipmentData::isDelayed)
                .forEach(s -> alerts.add("SHIPMENT DELAY: Shipment [" + s.getShipmentId()
                        + "] for Order [" + s.getOrderId() + "] is delayed."));

        return alerts;
    }
}
