package com.jackfruit.scm.reporting.engine;

import com.jackfruit.scm.reporting.model.InventoryData;
import com.jackfruit.scm.reporting.model.ShipmentData;

import java.util.ArrayList;
import java.util.List;

public class AlertGenerator {

    private static final int LOW_INVENTORY_THRESHOLD = 10;

    public List<String> generate(List<InventoryData> inventory, List<ShipmentData> shipments) {
        List<String> alerts = new ArrayList<>();

        inventory.stream()
                .filter(i -> i.getQuantity() < LOW_INVENTORY_THRESHOLD)
                .forEach(i -> alerts.add("LOW INVENTORY: Product [" + i.getProductName()
                        + "] has only " + i.getQuantity() + " units left."));

        shipments.stream()
                .filter(s -> s.getActualDate() != null && s.getExpectedDate() != null
                        && s.getActualDate().isAfter(s.getExpectedDate()))
                .forEach(s -> alerts.add("SHIPMENT DELAY: Shipment [" + s.getShipmentId()
                        + "] for Order [" + s.getOrderId() + "] is delayed."));

        return alerts;
    }
}