package model;

public class SupplierData {
    public String supplierId;
    public String name;
    public String contactInfo;

    public SupplierData(String supplierId, String name) {
        this(supplierId, name, null);
    }

    public SupplierData(String supplierId, String name, String contactInfo) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactInfo = contactInfo;
    }
}
