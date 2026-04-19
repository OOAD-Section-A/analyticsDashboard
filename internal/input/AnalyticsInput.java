package internal.input;

import model.ForecastData;
import model.InventoryData;
import model.OrderData;
import model.SalesData;
import model.ShipmentData;
import model.SupplierData;
import model.WarehouseData;

import java.util.List;

public class AnalyticsInput {
    private final List<InventoryData> inventory;
    private final List<SalesData> sales;
    private final List<OrderData> orders;
    private final List<ShipmentData> shipments;
    private final List<WarehouseData> warehouses;
    private final List<SupplierData> suppliers;
    private final List<ForecastData> forecasts;

    public AnalyticsInput(List<InventoryData> inventory,
                          List<SalesData> sales,
                          List<OrderData> orders,
                          List<ShipmentData> shipments,
                          List<WarehouseData> warehouses,
                          List<SupplierData> suppliers,
                          List<ForecastData> forecasts) {
        this.inventory = inventory;
        this.sales = sales;
        this.orders = orders;
        this.shipments = shipments;
        this.warehouses = warehouses;
        this.suppliers = suppliers;
        this.forecasts = forecasts;
    }

    public List<InventoryData> getInventory() { return inventory; }
    public List<SalesData> getSales() { return sales; }
    public List<OrderData> getOrders() { return orders; }
    public List<ShipmentData> getShipments() { return shipments; }
    public List<WarehouseData> getWarehouses() { return warehouses; }
    public List<SupplierData> getSuppliers() { return suppliers; }
    public List<ForecastData> getForecasts() { return forecasts; }
}