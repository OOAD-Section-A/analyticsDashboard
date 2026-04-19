package engine;

import internal.KPIResultInternal;
import internal.input.AnalyticsInput;
import model.ForecastData;
import model.InventoryData;
import model.OrderData;
import model.SalesData;
import model.ShipmentData;
import model.SupplierData;
import model.WarehouseData;

import java.util.List;

public class AnalyticsEngine {

    public KPIResultInternal compute(AnalyticsInput input) {
        List<InventoryData> inventory = input.getInventory();
        List<SalesData> sales = input.getSales();
        List<OrderData> orders = input.getOrders();
        List<ShipmentData> shipments = input.getShipments();
        List<WarehouseData> warehouses = input.getWarehouses();
        List<SupplierData> suppliers = input.getSuppliers();
        List<ForecastData> forecasts = input.getForecasts();
        double totalRevenue = sales.stream().mapToDouble(SalesData::getRevenue).sum();
        int totalOrders = orders.size();
        int pendingOrders = (int) orders.stream()
                .filter(order -> "PENDING".equalsIgnoreCase(order.getStatus()))
                .count();
        int completedOrders = (int) orders.stream()
                .filter(order -> isCompletedStatus(order.getStatus()))
                .count();
        int totalInventoryUnits = inventory.stream()
                .mapToInt(InventoryData::getQuantity)
                .sum();
        int delayedShipments = (int) shipments.stream()
                .filter(ShipmentData::isDelayed)
                .count();

        double avgWarehouseUtilization = warehouses.stream()
                .mapToDouble(warehouse -> (double) warehouse.getUsedCapacity() / warehouse.getTotalCapacity() * 100)
                .average()
                .orElse(0.0);
        double avgSupplierReliability = suppliers.stream()
                .mapToDouble(SupplierData::getReliabilityScore)
                .average()
                .orElse(0.0);

        int totalUnitsSold = sales.stream().mapToInt(SalesData::getQuantitySold).sum();
        double revenuePerOrder = totalOrders == 0 ? 0.0 : totalRevenue / totalOrders;
        double inventoryTurnoverRatio = totalInventoryUnits == 0 ? 0.0 : (double) totalUnitsSold / totalInventoryUnits;
        double forecastAccuracyRate = calculateForecastAccuracyRate(forecasts);
        double onTimeShipmentRate = shipments.isEmpty()
                ? 100.0
                : ((double) (shipments.size() - delayedShipments) / shipments.size()) * 100.0;
        double orderCompletionRate = totalOrders == 0 ? 0.0 : ((double) completedOrders / totalOrders) * 100.0;
        double averageDailyDemand = forecasts.stream()
                .mapToDouble(ForecastData::getForecastedDemand)
                .average()
                .orElse(0.0);
        double inventoryCoverageDays = averageDailyDemand <= 0.0 ? 0.0 : totalInventoryUnits / averageDailyDemand;

        return new KPIResultInternal(
                round(totalRevenue),
                totalOrders,
                pendingOrders,
                completedOrders,
                totalInventoryUnits,
                delayedShipments,
                round(avgWarehouseUtilization),
                round(avgSupplierReliability),
                round(revenuePerOrder),
                round(inventoryTurnoverRatio),
                round(forecastAccuracyRate),
                round(onTimeShipmentRate),
                round(orderCompletionRate),
                round(inventoryCoverageDays)
        );
    }

    private double calculateForecastAccuracyRate(List<ForecastData> forecasts) {
        if (forecasts.isEmpty()) {
            return 0.0;
        }

        double totalAccuracy = 0.0;
        int comparableForecasts = 0;

        for (ForecastData forecast : forecasts) {
            if (forecast.getForecastedDemand() <= 0.0) {
                continue;
            }

            double error = Math.abs(forecast.getForecastedDemand() - forecast.getActualDemand());
            double accuracy = Math.max(0.0, 1.0 - (error / forecast.getForecastedDemand()));
            totalAccuracy += accuracy * 100.0;
            comparableForecasts++;
        }

        return comparableForecasts == 0 ? 0.0 : totalAccuracy / comparableForecasts;
    }

    private boolean isCompletedStatus(String status) {
        return "COMPLETED".equalsIgnoreCase(status)
                || "DELIVERED".equalsIgnoreCase(status)
                || "FULFILLED".equalsIgnoreCase(status);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
