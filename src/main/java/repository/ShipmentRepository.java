package repository;

import model.ShipmentData;

import java.util.List;

public interface ShipmentRepository {
    List<ShipmentData> fetchAll();
}
