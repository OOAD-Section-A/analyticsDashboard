package model;

public class WarehouseData {
    private String warehouseId;
    private String location;
    private int totalCapacity;
    private int usedCapacity;

    public WarehouseData() {}

    public WarehouseData(String warehouseId, String location, int totalCapacity, int usedCapacity) {
        this.warehouseId = warehouseId;
        this.location = location;
        this.totalCapacity = totalCapacity;
        this.usedCapacity = usedCapacity;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public int getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(int usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public int getCapacity() {
        return totalCapacity;
    }

    public void setCapacity(int capacity) {
        this.totalCapacity = capacity;
    }
}
