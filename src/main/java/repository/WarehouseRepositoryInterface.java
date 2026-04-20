package repository.interfaces;

import model.WarehouseData;
import java.util.List;

public interface WarehouseRepositoryInterface {
    List<WarehouseData> fetchAll();
}