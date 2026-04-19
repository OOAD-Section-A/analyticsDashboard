package internal.input;

import model.InventoryData;
import model.SalesData;

import java.util.List;

public class VisualizationInput {
    private final List<SalesData> sales;
    private final List<InventoryData> inventory;

    public VisualizationInput(List<SalesData> sales, List<InventoryData> inventory) {
        this.sales = sales;
        this.inventory = inventory;
    }

    public List<SalesData> getSales() { return sales; }
    public List<InventoryData> getInventory() { return inventory; }
}