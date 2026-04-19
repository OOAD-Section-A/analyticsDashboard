package internal;

import java.util.Map;

public class VisualizationDataInternal {
    private final Map<String, Double> revenueByProduct;
    private final Map<String, Integer> inventoryLevels;

    public VisualizationDataInternal(Map<String, Double> revenueByProduct, Map<String, Integer> inventoryLevels) {
        this.revenueByProduct = revenueByProduct;
        this.inventoryLevels = inventoryLevels;
    }

    public Map<String, Double> getRevenueByProduct() { return revenueByProduct; }
    public Map<String, Integer> getInventoryLevels() { return inventoryLevels; }
}