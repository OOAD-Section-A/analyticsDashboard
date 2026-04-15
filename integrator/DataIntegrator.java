package integrator;

import model.*;
import java.util.*;

public class DataIntegrator {

    public UnifiedData integrate(Map<String, Object> rawData) {

        // Extract raw data
        List<Inventory> inventory = castList(rawData.get("inventory"));
        List<Order> orders = castList(rawData.get("orders"));
        List<Supplier> suppliers = castList(rawData.get("suppliers"));
        List<Shipment> shipments = castList(rawData.get("shipments"));
        List<Sales> sales = castList(rawData.get("sales"));

        // --- CROSS-MODULE MAPPING ---

        // Map: OrderID → Shipment
        Map<String, Shipment> shipmentByOrder = new HashMap<>();
        for (Shipment s : shipments) {
            shipmentByOrder.put(s.orderId, s);
        }

        // Map: ProductID → Inventory
        Map<String, Inventory> inventoryMap = new HashMap<>();
        for (Inventory inv : inventory) {
            inventoryMap.put(inv.productId, inv);
        }

        // Map: ProductID → Sales
        Map<String, Sales> salesMap = new HashMap<>();
        for (Sales s : sales) {
            salesMap.put(s.productId, s);
        }

        // --- SYNCHRONIZATION / CONSISTENCY CHECKS ---

        // Ensure every order has corresponding inventory entry
        for (Order o : orders) {
            if (!inventoryMap.containsKey(o.productId)) {
                inventory.add(new Inventory(o.productId, 0)); // default entry
                inventoryMap.put(o.productId, new Inventory(o.productId, 0));
            }
        }

        // Ensure every order has shipment (if not → mark pending)
        for (Order o : orders) {
            if (!shipmentByOrder.containsKey(o.orderId)) {
                shipments.add(new Shipment("AUTO_" + o.orderId, o.orderId, "Pending"));
            }
        }

        // Ensure every product has sales record
        for (Inventory inv : inventory) {
            if (!salesMap.containsKey(inv.productId)) {
                sales.add(new Sales(inv.productId, 0));
            }
        }

        // --- FINAL UNIFIED DATASET ---

        return new UnifiedData(
                inventory,
                orders,
                suppliers,
                shipments,
                sales
        );
    }

    // Safe casting utility
    @SuppressWarnings("unchecked")
    private <T> List<T> castList(Object obj) {
        return (List<T>) obj;
    }
}