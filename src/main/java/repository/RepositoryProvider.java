package repository;

import repository.db.DbForecastRepository;
import repository.db.DbInventoryRepository;
import repository.db.DbOrderRepository;
import repository.db.DbSalesRepository;
import repository.db.DbShipmentRepository;
import repository.db.DbSupplierRepository;
import repository.db.DbWarehouseRepository;
import repository.mock.MockForecastRepository;
import repository.mock.MockInventoryRepository;
import repository.mock.MockOrderRepository;
import repository.mock.MockSalesRepository;
import repository.mock.MockShipmentRepository;
import repository.mock.MockSupplierRepository;
import repository.mock.MockWarehouseRepository;

public class RepositoryProvider {
    public enum Mode {
        MOCK,
        DB
    }

    private final Mode mode;

    public RepositoryProvider() {
        this(Mode.MOCK);
    }

    public RepositoryProvider(Mode mode) {
        this.mode = mode;
    }

    public InventoryRepository inventoryRepository() {
        return mode == Mode.DB ? new DbInventoryRepository() : new MockInventoryRepository();
    }

    public SalesRepository salesRepository() {
        return mode == Mode.DB ? new DbSalesRepository() : new MockSalesRepository();
    }

    public OrderRepository orderRepository() {
        return mode == Mode.DB ? new DbOrderRepository() : new MockOrderRepository();
    }

    public ShipmentRepository shipmentRepository() {
        return mode == Mode.DB ? new DbShipmentRepository() : new MockShipmentRepository();
    }

    public WarehouseRepository warehouseRepository() {
        return mode == Mode.DB ? new DbWarehouseRepository() : new MockWarehouseRepository();
    }

    public SupplierRepository supplierRepository() {
        return mode == Mode.DB ? new DbSupplierRepository() : new MockSupplierRepository();
    }

    public ForecastRepository forecastRepository() {
        return mode == Mode.DB ? new DbForecastRepository() : new MockForecastRepository();
    }
}
