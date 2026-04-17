import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.adapter.InventoryAdapter;

public class DBFile {
    public static void main(String[] args) {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            InventoryAdapter inventoryAdapter = new InventoryAdapter(facade);

            // Example: List all products
            inventoryAdapter.listProducts().forEach(product -> 
                System.out.println(product.productName())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}