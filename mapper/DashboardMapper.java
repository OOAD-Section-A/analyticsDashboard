package mapper;

import dto.KPIResult;
import dto.ReportDTO;
import dto.VisualizationDTO;
import internal.KPIResultInternal;
import internal.ReportDataInternal;
import internal.VisualizationDataInternal;

public class DashboardMapper {

    public KPIResult toKPIResult(KPIResultInternal internal) {
        return new KPIResult(
                internal.getTotalRevenue(),
                internal.getTotalOrders(),
                internal.getPendingOrders(),
                internal.getCompletedOrders(),
                internal.getTotalInventoryUnits(),
                internal.getDelayedShipments(),
                internal.getAvgWarehouseUtilization(),
                internal.getAvgSupplierReliability(),
                internal.getRevenuePerOrder(),
                internal.getInventoryTurnoverRatio(),
                internal.getForecastAccuracyRate(),
                internal.getOnTimeShipmentRate(),
                internal.getOrderCompletionRate(),
                internal.getInventoryCoverageDays()
        );
    }

    public VisualizationDTO toVisualizationDTO(VisualizationDataInternal internal) {
        return new VisualizationDTO(internal.getRevenueByProduct(), internal.getInventoryLevels());
    }

    public ReportDTO toReportDTO(ReportDataInternal internal) {
        return new ReportDTO(internal.getSummary(), internal.getInsights(), internal.getAlerts(), internal.getGeneratedAt());
    }
}