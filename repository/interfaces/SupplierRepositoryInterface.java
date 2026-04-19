package repository.interfaces;

import model.SupplierData;
import java.util.List;

public interface SupplierRepositoryInterface {
    List<SupplierData> fetchAll();
}