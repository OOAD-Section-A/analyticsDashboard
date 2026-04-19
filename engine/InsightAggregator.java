package engine;

import internal.KPIResultInternal;

import java.util.ArrayList;
import java.util.List;

public class InsightAggregator {

    public List<String> generate(KPIResultInternal kpis) {
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
        if (kpis.getForecastAccuracyRate() > 0 && kpis.getForecastAccuracyRate() < 75) {
            insights.add("Forecast accuracy is " + String.format("%.1f", kpis.getForecastAccuracyRate())
                    + "%. Revisit forecasting assumptions.");
        }
        if (kpis.getOnTimeShipmentRate() < 90) {
            insights.add("On-time shipment rate is only " + String.format("%.1f", kpis.getOnTimeShipmentRate())
                    + "%. Delivery coordination needs attention.");
        }
        if (kpis.getInventoryCoverageDays() > 0 && kpis.getInventoryCoverageDays() < 7) {
            insights.add("Inventory coverage is below 7 days. Replenishment risk is rising.");
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
