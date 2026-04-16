package com.jackfruit.scm.reporting.dto;

public class KPIResult {
    private final double totalRevenue;
    private final int totalOrders;
    private final int pendingOrders;
    private final int totalInventoryUnits;
    private final int delayedShipments;
    private final double avgWarehouseUtilization;
    private final double avgSupplierReliability;

    public KPIResult(double totalRevenue, int totalOrders, int pendingOrders,
                     int totalInventoryUnits, int delayedShipments,
                     double avgWarehouseUtilization, double avgSupplierReliability) {
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.pendingOrders = pendingOrders;
        this.totalInventoryUnits = totalInventoryUnits;
        this.delayedShipments = delayedShipments;
        this.avgWarehouseUtilization = avgWarehouseUtilization;
        this.avgSupplierReliability = avgSupplierReliability;
    }

    public double getTotalRevenue() { return totalRevenue; }
    public int getTotalOrders() { return totalOrders; }
    public int getPendingOrders() { return pendingOrders; }
    public int getTotalInventoryUnits() { return totalInventoryUnits; }
    public int getDelayedShipments() { return delayedShipments; }
    public double getAvgWarehouseUtilization() { return avgWarehouseUtilization; }
    public double getAvgSupplierReliability() { return avgSupplierReliability; }
}