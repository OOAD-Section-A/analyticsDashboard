package engine;

import internal.KPIResultInternal;
import internal.MarginResultInternal;
import internal.ReportDataInternal;

import java.time.LocalDateTime;
import java.util.List;

public class ReportGenerator {

    public ReportDataInternal generate(KPIResultInternal kpis, List<String> insights, List<String> alerts,
                                       MarginResultInternal marginResult) {
        String summary = String.format(
                "Total Revenue: %.2f | Total Orders: %d | Pending Orders: %d | Completed Orders: %d | " +
                "Revenue per Order: %.2f | Inventory Turnover: %.2f | Forecast Accuracy: %.1f%% | " +
                "On-Time Shipment Rate: %.1f%% | Avg Warehouse Utilization: %.1f%% | Avg Supplier Reliability: %.1f%% | " +
                "Margin Conceded: $%.2f | Margin Protected: $%.2f",
                kpis.getTotalRevenue(), kpis.getTotalOrders(), kpis.getPendingOrders(), kpis.getCompletedOrders(),
                kpis.getRevenuePerOrder(), kpis.getInventoryTurnoverRatio(), kpis.getForecastAccuracyRate(),
                kpis.getOnTimeShipmentRate(), kpis.getAvgWarehouseUtilization(), kpis.getAvgSupplierReliability(),
                marginResult.getMarginConceded(), marginResult.getMarginProtected()
        );
        return new ReportDataInternal(summary, insights, alerts, LocalDateTime.now());
    }
}
