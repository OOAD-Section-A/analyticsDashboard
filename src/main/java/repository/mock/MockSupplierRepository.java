package repository.mock;

import model.SupplierData;
import repository.SupplierRepository;

import java.util.List;

public class MockSupplierRepository implements SupplierRepository {
    @Override
    public List<SupplierData> fetchAll() {
        return List.of(
                new SupplierData("SUP001", "Acme Supplies", "contact@acme.example", "Widget Pro", 91.0),
                new SupplierData("SUP002", "Global Components", "sales@global.example", "Gadget Mini", 68.5),
                new SupplierData("SUP003", "Logistics Inc.", "info@logistics.example", "Sprocket Pack", 74.0)
        );
    }
}
