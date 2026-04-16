package dashboard;

import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import dto.*;
import engine.*;
import model.*;
import repository.*;
import service.*;

import java.util.List;

public class DashboardService {

    public DashboardDTO buildDashboard() {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {

            // --- Repositories ---
            InventoryRepository inventoryRepo = new InventoryRepository(facade);
            SalesRepository salesRepo = new SalesRepository(facade);
            OrderRepository orderRepo = new OrderRepository(facade);
            ShipmentRepository shipmentRepo = new ShipmentRepository(facade);
            WarehouseRepository warehouseRepo = new WarehouseRepository(facade);
            SupplierRepository supplierRepo = new SupplierRepository(facade);
            ForecastRepository forecastRepo = new ForecastRepository(facade);

            // --- Services ---
            List<InventoryData> inventory = new InventoryService(inventoryRepo).getCleanedData();
            List<SalesData> sales = new SalesService(salesRepo).getCleanedData();
            List<OrderData> orders = new OrderService(orderRepo).getCleanedData();
            List<ShipmentData> shipments = new ShipmentService(shipmentRepo).getCleanedData();
            List<WarehouseData> warehouses = new WarehouseService(warehouseRepo).getCleanedData();
            List<SupplierData> suppliers = new SupplierService(supplierRepo).getCleanedData();
            List<ForecastData> forecasts = new ForecastService(forecastRepo).getCleanedData();

            // --- Engines ---
            KPIResult kpis = new AnalyticsEngine()
                    .compute(inventory, sales, orders, shipments, warehouses, suppliers, forecasts);

            List<String> insights = new InsightAggregator().generate(kpis);
            List<String> alerts = new AlertGenerator().generate(inventory, shipments);
            VisualizationDTO visuals = new VisualizationEngine().buildCharts(sales, inventory);
            ReportDTO report = new ReportGenerator().generate(kpis, insights, alerts);

            return new DashboardDTO(kpis, insights, alerts, visuals, report);
        }
    }
}