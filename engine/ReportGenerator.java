package engine;

import dto.KPIResult;
import dto.ReportDTO;

import java.time.LocalDateTime;
import java.util.List;

public class ReportGenerator {

    public ReportDTO generate(KPIResult kpis, List<String> insights, List<String> alerts) {
        String summary = String.format(
                "Total Revenue: %.2f | Total Orders: %d | Pending Orders: %d | " +
                "Delayed Shipments: %d | Avg Warehouse Utilization: %.1f%% | Avg Supplier Reliability: %.1f%%",
                kpis.getTotalRevenue(), kpis.getTotalOrders(), kpis.getPendingOrders(),
                kpis.getDelayedShipments(), kpis.getAvgWarehouseUtilization(), kpis.getAvgSupplierReliability()
        );
        return new ReportDTO(summary, insights, alerts, LocalDateTime.now());
    }
}