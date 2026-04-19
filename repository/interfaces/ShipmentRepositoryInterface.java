package repository.interfaces;

import model.ShipmentData;
import java.util.List;

public interface ShipmentRepositoryInterface {
    List<ShipmentData> fetchAll();
}