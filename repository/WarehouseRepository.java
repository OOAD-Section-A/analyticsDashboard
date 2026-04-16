package repository;

import com.jackfruit.scm.database.adapter.WarehouseManagementAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import model.WarehouseData;

import java.util.List;
import java.util.stream.Collectors;

public class WarehouseRepository {

    private final WarehouseManagementAdapter warehouseAdapter;

    public WarehouseRepository(SupplyChainDatabaseFacade facade) {
        this.warehouseAdapter = new WarehouseManagementAdapter(facade);
    }

    public List<WarehouseData> fetchAll() {
        return warehouseAdapter.listWarehouses().stream()
                .map(w -> new WarehouseData(
                        w.warehouseId(),
                        w.location(),
                        w.totalCapacity(),
                        w.usedCapacity()
                ))
                .collect(Collectors.toList());
    }
}