package repository;

import model.WarehouseData;

import java.util.List;

public interface WarehouseRepository {
    List<WarehouseData> fetchAll();
}
