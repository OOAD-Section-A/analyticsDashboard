package repository;

import model.InventoryData;

import java.util.List;

public interface InventoryRepository {
    List<InventoryData> fetchAll();
}
