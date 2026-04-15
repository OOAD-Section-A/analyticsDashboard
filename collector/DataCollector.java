package collector;

import core.SCMCoreModules;
import model.*;

import java.util.*;
import java.sql.Connection;

public class DataCollector {

    private SCMCoreModules scm;

    // Constructor accepts DB connection and passes it to SCMCoreModules
    public DataCollector(Connection conn) {
        this.scm = new SCMCoreModules(conn);
    }

    // Collect all raw data from SCM modules
    public Map<String, Object> collectData() throws Exception {

        Map<String, Object> rawData = new HashMap<>();

        // Fetch data from DB via SCMCoreModules
        List<Inventory> inventory = scm.getInventoryData();
        List<Order> orders = scm.getOrderData();
        List<Supplier> suppliers = scm.getSupplierData();
        List<Shipment> shipments = scm.getShipmentData();
        List<Sales> sales = scm.getSalesData();

        // Store in map (raw datasets)
        rawData.put("inventory", inventory);
        rawData.put("orders", orders);
        rawData.put("suppliers", suppliers);
        rawData.put("shipments", shipments);
        rawData.put("sales", sales);

        return rawData;
    }
}