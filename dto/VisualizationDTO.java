package com.jackfruit.scm.reporting.dto;

import java.util.Map;

public class VisualizationDTO {
    private final Map<String, Double> revenueByProduct;
    private final Map<String, Integer> inventoryLevels;

    public VisualizationDTO(Map<String, Double> revenueByProduct, Map<String, Integer> inventoryLevels) {
        this.revenueByProduct = revenueByProduct;
        this.inventoryLevels = inventoryLevels;
    }

    public Map<String, Double> getRevenueByProduct() { return revenueByProduct; }
    public Map<String, Integer> getInventoryLevels() { return inventoryLevels; }
}