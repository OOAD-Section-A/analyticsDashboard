package model;

public class InventoryData {
    private String productId;
    private String productName;
    private int quantity;
    private String warehouseId;
    private double unitCost;

    public InventoryData(String productId, String productName, int quantity, String warehouseId, double unitCost) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.warehouseId = warehouseId;
        this.unitCost = unitCost;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public String getWarehouseId() { return warehouseId; }
    public double getUnitCost() { return unitCost; }
}