import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.adapter.InventoryAdapter;

public class DBFile {
    public static void main(String[] args) {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            InventoryAdapter inventoryAdapter = new InventoryAdapter(facade);

            // Test: List all products
            System.out.println("Products:");
            inventoryAdapter.listProducts().forEach(product ->
                System.out.println(product.productName())
            );

            // You can add more queries here using other adapters or facade methods
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}