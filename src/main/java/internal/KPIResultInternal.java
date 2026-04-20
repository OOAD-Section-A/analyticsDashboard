package internal;

public class KPIResultInternal {
    private final double totalRevenue;
    private final int totalOrders;
    private final int pendingOrders;
    private final int completedOrders;
    private final int totalInventoryUnits;
    private final int delayedShipments;
    private final double avgWarehouseUtilization;
    private final double avgSupplierReliability;
    private final double revenuePerOrder;
    private final double inventoryTurnoverRatio;
    private final double forecastAccuracyRate;
    private final double onTimeShipmentRate;
    private final double orderCompletionRate;
    private final double inventoryCoverageDays;

    public KPIResultInternal(double totalRevenue,
                             int totalOrders,
                             int pendingOrders,
                             int completedOrders,
                             int totalInventoryUnits,
                             int delayedShipments,
                             double avgWarehouseUtilization,
                             double avgSupplierReliability,
                             double revenuePerOrder,
                             double inventoryTurnoverRatio,
                             double forecastAccuracyRate,
                             double onTimeShipmentRate,
                             double orderCompletionRate,
                             double inventoryCoverageDays) {
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.pendingOrders = pendingOrders;
        this.completedOrders = completedOrders;
        this.totalInventoryUnits = totalInventoryUnits;
        this.delayedShipments = delayedShipments;
        this.avgWarehouseUtilization = avgWarehouseUtilization;
        this.avgSupplierReliability = avgSupplierReliability;
        this.revenuePerOrder = revenuePerOrder;
        this.inventoryTurnoverRatio = inventoryTurnoverRatio;
        this.forecastAccuracyRate = forecastAccuracyRate;
        this.onTimeShipmentRate = onTimeShipmentRate;
        this.orderCompletionRate = orderCompletionRate;
        this.inventoryCoverageDays = inventoryCoverageDays;
    }

    public double getTotalRevenue() { return totalRevenue; }
    public int getTotalOrders() { return totalOrders; }
    public int getPendingOrders() { return pendingOrders; }
    public int getCompletedOrders() { return completedOrders; }
    public int getTotalInventoryUnits() { return totalInventoryUnits; }
    public int getDelayedShipments() { return delayedShipments; }
    public double getAvgWarehouseUtilization() { return avgWarehouseUtilization; }
    public double getAvgSupplierReliability() { return avgSupplierReliability; }
    public double getRevenuePerOrder() { return revenuePerOrder; }
    public double getInventoryTurnoverRatio() { return inventoryTurnoverRatio; }
    public double getForecastAccuracyRate() { return forecastAccuracyRate; }
    public double getOnTimeShipmentRate() { return onTimeShipmentRate; }
    public double getOrderCompletionRate() { return orderCompletionRate; }
    public double getInventoryCoverageDays() { return inventoryCoverageDays; }
}