package repository.interfaces;

import model.InventoryData;
import java.util.List;

public interface InventoryRepositoryInterface {
    List<InventoryData> fetchAll();
}