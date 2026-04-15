package model;

public class Supplier {
    public String supplierId;
    public String name;
    public String contactInfo;

    public Supplier(String supplierId, String name) {
        this(supplierId, name, null);
    }

    public Supplier(String supplierId, String name, String contactInfo) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactInfo = contactInfo;
    }
}
