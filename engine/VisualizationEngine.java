package engine;

import internal.VisualizationDataInternal;
import internal.input.VisualizationInput;
import model.InventoryData;
import model.SalesData;

import java.util.*;
import java.util.stream.Collectors;

public class VisualizationEngine {

    public VisualizationDataInternal buildCharts(VisualizationInput input) {
        List<SalesData> sales = input.getSales();
        List<InventoryData> inventory = input.getInventory();
        // Revenue by product
        Map<String, Double> revenueByProduct = sales.stream()
                .collect(Collectors.groupingBy(SalesData::getProductId,
                        Collectors.summingDouble(SalesData::getRevenue)));

        // Inventory levels by product
        Map<String, Integer> inventoryLevels = inventory.stream()
                .collect(Collectors.toMap(InventoryData::getProductId,
                        InventoryData::getQuantity, Integer::sum));

        return new VisualizationDataInternal(revenueByProduct, inventoryLevels);
    }
}