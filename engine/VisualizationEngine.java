package engine;

import dto.VisualizationDTO;
import model.InventoryData;
import model.SalesData;

import java.util.*;
import java.util.stream.Collectors;

public class VisualizationEngine {

    public VisualizationDTO buildCharts(List<SalesData> sales, List<InventoryData> inventory) {
        // Revenue by product
        Map<String, Double> revenueByProduct = sales.stream()
                .collect(Collectors.groupingBy(SalesData::getProductId,
                        Collectors.summingDouble(SalesData::getRevenue)));

        // Inventory levels by product
        Map<String, Integer> inventoryLevels = inventory.stream()
                .collect(Collectors.toMap(InventoryData::getProductId,
                        InventoryData::getQuantity, Integer::sum));

        return new VisualizationDTO(revenueByProduct, inventoryLevels);
    }
}