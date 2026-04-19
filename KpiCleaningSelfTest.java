import dto.KPIResult;
import engine.AnalyticsEngine;
import exception.AnalyticsExceptionSource;
import model.ForecastData;
import model.InventoryData;
import model.OrderData;
import model.SalesData;
import model.ShipmentData;
import model.SupplierData;
import model.WarehouseData;
import repository.ForecastRepository;
import repository.InventoryRepository;
import repository.OrderRepository;
import repository.SalesRepository;
import repository.ShipmentRepository;
import repository.SupplierRepository;
import repository.WarehouseRepository;
import service.ForecastService;
import service.InventoryService;
import service.OrderService;
import service.SalesService;
import service.ShipmentService;
import service.SupplierService;
import service.WarehouseService;

import java.time.LocalDate;
import java.util.List;

public class KpiCleaningSelfTest {

    public static void main(String[] args) {
        AnalyticsExceptionSource exceptionSource = new AnalyticsExceptionSource();

        List<InventoryData> inventory = new InventoryService(new StubInventoryRepository(exceptionSource), exceptionSource).getCleanedData();
        List<SalesData> sales = new SalesService(new StubSalesRepository(exceptionSource), exceptionSource).getCleanedData();
        List<OrderData> orders = new OrderService(new StubOrderRepository(exceptionSource), exceptionSource).getCleanedData();
        List<ShipmentData> shipments = new ShipmentService(new StubShipmentRepository(exceptionSource), exceptionSource).getCleanedData();
        List<WarehouseData> warehouses = new WarehouseService(new StubWarehouseRepository(exceptionSource), exceptionSource).getCleanedData();
        List<SupplierData> suppliers = new SupplierService(new StubSupplierRepository(exceptionSource), exceptionSource).getCleanedData();
        List<ForecastData> forecasts = new ForecastService(new StubForecastRepository(exceptionSource), exceptionSource).getCleanedData();

        assertEquals(2, inventory.size(), "Inventory cleaning");
        assertEquals(2, sales.size(), "Sales cleaning");
        assertEquals(2, orders.size(), "Order cleaning");
        assertEquals(2, shipments.size(), "Shipment cleaning");
        assertEquals(2, warehouses.size(), "Warehouse cleaning");
        assertEquals(2, suppliers.size(), "Supplier cleaning");
        assertEquals(2, forecasts.size(), "Forecast cleaning");

        // Create AnalyticsInput and use new compute signature
        internal.input.AnalyticsInput input = new internal.input.AnalyticsInput(
            inventory, sales, orders, shipments, warehouses, suppliers, forecasts
        );
        internal.KPIResultInternal kpis = new AnalyticsEngine().compute(input);

        assertDoubleEquals(700.0, kpis.getTotalRevenue(), "Total revenue");
        assertEquals(2, kpis.getTotalOrders(), "Total orders");
        assertEquals(1, kpis.getPendingOrders(), "Pending orders");
        assertEquals(1, kpis.getCompletedOrders(), "Completed orders");
        assertEquals(80, kpis.getTotalInventoryUnits(), "Inventory units");
        assertEquals(1, kpis.getDelayedShipments(), "Delayed shipments");
        assertDoubleEquals(72.5, kpis.getAvgWarehouseUtilization(), "Warehouse utilization");
        assertDoubleEquals(85.0, kpis.getAvgSupplierReliability(), "Supplier reliability");
        assertDoubleEquals(350.0, kpis.getRevenuePerOrder(), "Revenue per order");
        assertDoubleEquals(0.13, kpis.getInventoryTurnoverRatio(), "Inventory turnover");
        assertDoubleEquals(78.33, kpis.getForecastAccuracyRate(), "Forecast accuracy");
        assertDoubleEquals(50.0, kpis.getOnTimeShipmentRate(), "On-time shipment rate");
        assertDoubleEquals(50.0, kpis.getOrderCompletionRate(), "Order completion rate");
        assertDoubleEquals(4.57, kpis.getInventoryCoverageDays(), "Inventory coverage days");

        System.out.println("Cleaning and KPI self-test passed.");
    }

    private static void assertEquals(int expected, int actual, String label) {
        if (expected != actual) {
            throw new IllegalStateException(label + " expected " + expected + " but found " + actual);
        }
    }

    private static void assertDoubleEquals(double expected, double actual, String label) {
        if (Math.abs(expected - actual) > 0.01) {
            throw new IllegalStateException(label + " expected " + expected + " but found " + actual);
        }
    }

    private static class StubInventoryRepository extends InventoryRepository {
        StubInventoryRepository(AnalyticsExceptionSource exceptionSource) {
            super(exceptionSource);
        }

        @Override
        public List<InventoryData> fetchAll() {
            return List.of(
                    new InventoryData("P-1", "Jackfruit Chips", 50, "W-1", 15.0),
                    new InventoryData("P-2", "Jackfruit Flour", 30, "W-2", 20.0),
                    new InventoryData("P-3", "", 12, "W-3", 10.0),
                    new InventoryData("P-4", "Damaged Stock", -2, "W-4", 8.0)
            );
        }
    }

    private static class StubSalesRepository extends SalesRepository {
        StubSalesRepository(AnalyticsExceptionSource exceptionSource) {
            super(exceptionSource);
        }

        @Override
        public List<SalesData> fetchAll() {
            return List.of(
                    new SalesData("S-1", "P-1", 6, 300.0, LocalDate.of(2026, 4, 10)),
                    new SalesData("S-2", "P-2", 4, 400.0, LocalDate.of(2026, 4, 11)),
                    new SalesData("S-3", "P-3", 0, 50.0, LocalDate.of(2026, 4, 11)),
                    new SalesData("S-4", "", 2, 40.0, LocalDate.of(2026, 4, 11))
            );
        }
    }

    private static class StubOrderRepository extends OrderRepository {
        StubOrderRepository(AnalyticsExceptionSource exceptionSource) {
            super(exceptionSource);
        }

        @Override
        public List<OrderData> fetchAll() {
            return List.of(
                    new OrderData("O-1", "C-1", "PENDING", 300.0, LocalDate.of(2026, 4, 10)),
                    new OrderData("O-2", "C-2", "COMPLETED", 400.0, LocalDate.of(2026, 4, 11)),
                    new OrderData("O-3", "", "PENDING", 100.0, LocalDate.of(2026, 4, 11))
            );
        }
    }

    private static class StubShipmentRepository extends ShipmentRepository {
        StubShipmentRepository(AnalyticsExceptionSource exceptionSource) {
            super(exceptionSource);
        }

        @Override
        public List<ShipmentData> fetchAll() {
            return List.of(
                    new ShipmentData("SH-1", "O-1", "IN_TRANSIT", LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 12), false),
                    new ShipmentData("SH-2", "O-2", "DELAYED", LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 13), true),
                    new ShipmentData("SH-3", "O-3", "INVALID", null, LocalDate.of(2026, 4, 11), false)
            );
        }
    }

    private static class StubWarehouseRepository extends WarehouseRepository {
        StubWarehouseRepository(AnalyticsExceptionSource exceptionSource) {
            super(exceptionSource);
        }

        @Override
        public List<WarehouseData> fetchAll() {
            return List.of(
                    new WarehouseData("W-1", "North Hub", 100, 70),
                    new WarehouseData("W-2", "South Hub", 100, 75),
                    new WarehouseData("W-3", "Broken Hub", 0, 10)
            );
        }
    }

    private static class StubSupplierRepository extends SupplierRepository {
        StubSupplierRepository(AnalyticsExceptionSource exceptionSource) {
            super(exceptionSource);
        }

        @Override
        public List<SupplierData> fetchAll() {
            return List.of(
                    new SupplierData("SUP-1", "Fresh Farms", "North", 90.0),
                    new SupplierData("SUP-2", "Green Valley", "South", 80.0),
                    new SupplierData("SUP-3", "Unstable Source", "West", 120.0)
            );
        }
    }

    private static class StubForecastRepository extends ForecastRepository {
        StubForecastRepository(AnalyticsExceptionSource exceptionSource) {
            super(exceptionSource);
        }

        @Override
        public List<ForecastData> fetchAll() {
            return List.of(
                    new ForecastData("P-1", 20.0, 18.0, LocalDate.of(2026, 4, 15), LocalDate.of(2026, 4, 15)),
                    new ForecastData("P-2", 15.0, 10.0, LocalDate.of(2026, 4, 15), LocalDate.of(2026, 4, 15)),
                    new ForecastData("P-3", -5.0, 4.0, LocalDate.of(2026, 4, 16), LocalDate.of(2026, 4, 16))
            );
        }
    }
}
