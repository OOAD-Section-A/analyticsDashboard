package integrator;

import model.*;
import java.util.*;

public class UnifiedData {
    public List<Inventory> inventory;
    public List<Order> orders;
    public List<Supplier> suppliers;
    public List<Shipment> shipments;
    public List<Sales> sales;

    public UnifiedData(List<Inventory> inventory,
                       List<Order> orders,
                       List<Supplier> suppliers,
                       List<Shipment> shipments,
                       List<Sales> sales) {

        this.inventory = inventory;
        this.orders = orders;
        this.suppliers = suppliers;
        this.shipments = shipments;
        this.sales = sales;
    }
}