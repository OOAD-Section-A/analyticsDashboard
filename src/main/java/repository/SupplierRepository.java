package repository;

import model.SupplierData;

import java.util.List;

public interface SupplierRepository {
    List<SupplierData> fetchAll();
}
