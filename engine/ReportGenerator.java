package engine;

import dto.KPIResult;
import dto.ReportDTO;
import com.pricingos.common.MarginProfitabilityResult; // <-- NEW IMPORT

import java.time.LocalDateTime;
import java.util.List;

public class ReportGenerator {

    // <-- UPDATED METHOD SIGNATURE TO ACCEPT THE MARGIN RESULT
    public ReportDTO generate(KPIResult kpis, List<String> insights, List<String> alerts, MarginProfitabilityResult marginResult) {
        String summary = String.format(
                "Total Revenue: %.2f | Total Orders: %d | Pending Orders: %d | Completed Orders: %d | " +
                "Revenue per Order: %.2f | Inventory Turnover: %.2f | Forecast Accuracy: %.1f%% | " +
                "On-Time Shipment Rate: %.1f%% | Avg Warehouse Utilization: %.1f%% | Avg Supplier Reliability: %.1f%% | " +
                "Margin Conceded: $%.2f | Margin Protected: $%.2f", // <-- NEW ADDITIONS TO THE STRING
                kpis.getTotalRevenue(), kpis.getTotalOrders(), kpis.getPendingOrders(), kpis.getCompletedOrders(),
                kpis.getRevenuePerOrder(), kpis.getInventoryTurnoverRatio(), kpis.getForecastAccuracyRate(),
                kpis.getOnTimeShipmentRate(), kpis.getAvgWarehouseUtilization(), kpis.getAvgSupplierReliability(),
                marginResult.marginConceded(), marginResult.marginProtected() // <-- NEW VARIABLES PASSED IN
        );
        return new ReportDTO(summary, insights, alerts, LocalDateTime.now());
    }
}