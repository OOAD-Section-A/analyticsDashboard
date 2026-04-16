package model;

public class SupplierData {
    private String supplierId;
    private String supplierName;
    private String region;
    private double reliabilityScore;

    public SupplierData(String supplierId, String supplierName, String region, double reliabilityScore) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.region = region;
        this.reliabilityScore = reliabilityScore;
    }

    public String getSupplierId() { return supplierId; }
    public String getSupplierName() { return supplierName; }
    public String getRegion() { return region; }
    public double getReliabilityScore() { return reliabilityScore; }
}