package engine;

import dto.KPIResult;
import model.*;

import java.util.List;

public class AnalyticsEngine {

    public KPIResult compute(
            List<InventoryData> inventory,
            List<SalesData> sales,
            List<OrderData> orders,
            List<ShipmentData> shipments,
            List<WarehouseData> warehouses,
            List<SupplierData> suppliers,
            List<ForecastData> forecasts
    ) {
        double totalRevenue = sales.stream().mapToDouble(SalesData::getRevenue).sum();
        int totalOrders = orders.size();
        long pendingOrders = orders.stream().filter(o -> "PENDING".equalsIgnoreCase(o.getStatus())).count();
        int totalInventoryUnits = inventory.stream().mapToInt(InventoryData::getQuantity).sum();
        long delayedShipments = shipments.stream()
                .filter(s -> s.getActualDate() != null && s.getExpectedDate() != null
                        && s.getActualDate().isAfter(s.getExpectedDate()))
                .count();
        double avgWarehouseUtilization = warehouses.stream()
                .mapToDouble(w -> (double) w.getUsedCapacity() / w.getTotalCapacity() * 100)
                .average().orElse(0);
        double avgSupplierReliability = suppliers.stream()
                .mapToDouble(SupplierData::getReliabilityScore)
                .average().orElse(0);

        return new KPIResult(totalRevenue, totalOrders, (int) pendingOrders,
                totalInventoryUnits, (int) delayedShipments,
                avgWarehouseUtilization, avgSupplierReliability);
    }
}