package engine;

import dto.KPIResult;

import java.util.ArrayList;
import java.util.List;

public class InsightAggregator {

    public List<String> generate(KPIResult kpis) {
        List<String> insights = new ArrayList<>();

        if (kpis.getAvgWarehouseUtilization() > 85) {
            insights.add("Warehouse utilization is critically high at "
                    + String.format("%.1f", kpis.getAvgWarehouseUtilization()) + "%. Consider expanding capacity.");
        }
        if (kpis.getDelayedShipments() > 0) {
            insights.add(kpis.getDelayedShipments() + " shipment(s) are delayed. Review logistics pipeline.");
        }
        if (kpis.getPendingOrders() > kpis.getTotalOrders() * 0.3) {
            insights.add("More than 30% of orders are still pending. Check fulfillment capacity.");
        }
        if (kpis.getAvgSupplierReliability() < 70) {
            insights.add("Average supplier reliability is below 70%. Supplier evaluation recommended.");
        }
        if (insights.isEmpty()) {
            insights.add("All key metrics are within acceptable ranges.");
        }

        return insights;
    }
}