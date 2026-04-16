package repository;

import com.jackfruit.scm.database.adapter.InventoryAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.reporting.model.InventoryData;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryRepository {

    private final InventoryAdapter inventoryAdapter;

    public InventoryRepository(SupplyChainDatabaseFacade facade) {
        this.inventoryAdapter = new InventoryAdapter(facade);
    }

    public List<InventoryData> fetchAll() {
        return inventoryAdapter.listProducts().stream()
                .map(p -> new InventoryData(
                        p.productId(),
                        p.productName(),
                        p.quantity(),
                        p.warehouseId(),
                        p.unitCost()
                ))
                .collect(Collectors.toList());
    }
}