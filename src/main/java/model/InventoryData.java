package model;

public class InventoryData {
    private String productId;
    private String productName;
    private String warehouseId;
    private int quantity;
    private double unitCost;

    public InventoryData() {}

    public InventoryData(String productId, String productName, String warehouseId, int quantity, double unitCost) {
        this.productId = productId;
        this.productName = productName;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.unitCost = unitCost;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getUnitPrice() {
        return unitCost;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitCost = unitPrice;
    }
}
