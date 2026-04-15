package core;

import model.*;
import java.sql.*;
import java.util.*;

public class SCMCoreModules {

    private Connection conn;

    public SCMCoreModules(Connection conn) {
        this.conn = conn;
    }

    public List<Inventory> getInventoryData() throws Exception {
        List<Inventory> list = new ArrayList<>();

        String query = "SELECT * FROM inventory";
        ResultSet rs = conn.createStatement().executeQuery(query);

        while (rs.next()) {
            list.add(new Inventory(
                    rs.getString("product_id"),
                    rs.getInt("quantity")
            ));
        }

        return list;
    }

    public List<Order> getOrderData() throws Exception {
        List<Order> list = new ArrayList<>();

        String query = "SELECT * FROM orders";
        ResultSet rs = conn.createStatement().executeQuery(query);

        while (rs.next()) {
            list.add(new Order(
                    rs.getString("order_id"),
                    rs.getString("product_id"),
                    rs.getInt("quantity")
            ));
        }

        return list;
    }

    public List<Supplier> getSupplierData() throws Exception {
        List<Supplier> list = new ArrayList<>();

        String query = "SELECT * FROM suppliers";
        ResultSet rs = conn.createStatement().executeQuery(query);

        while (rs.next()) {
            list.add(new Supplier(
                    rs.getString("supplier_id"),
                    rs.getString("name")
            ));
        }

        return list;
    }

    public List<Shipment> getShipmentData() throws Exception {
        List<Shipment> list = new ArrayList<>();

        String query = "SELECT * FROM shipments";
        ResultSet rs = conn.createStatement().executeQuery(query);

        while (rs.next()) {
            list.add(new Shipment(
                    rs.getString("shipment_id"),
                    rs.getString("order_id"),
                    rs.getString("status")
            ));
        }

        return list;
    }

    public List<Sales> getSalesData() throws Exception {
        List<Sales> list = new ArrayList<>();

        String query = "SELECT * FROM sales";
        ResultSet rs = conn.createStatement().executeQuery(query);

        while (rs.next()) {
            list.add(new Sales(
                    rs.getString("product_id"),
                    rs.getInt("units_sold")
            ));
        }

        return list;
    }
}