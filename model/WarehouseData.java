package com.jackfruit.scm.reporting.model;

public class WarehouseData {
    private String warehouseId;
    private String location;
    private int totalCapacity;
    private int usedCapacity;

    public WarehouseData(String warehouseId, String location, int totalCapacity, int usedCapacity) {
        this.warehouseId = warehouseId;
        this.location = location;
        this.totalCapacity = totalCapacity;
        this.usedCapacity = usedCapacity;
    }

    public String getWarehouseId() { return warehouseId; }
    public String getLocation() { return location; }
    public int getTotalCapacity() { return totalCapacity; }
    public int getUsedCapacity() { return usedCapacity; }
}