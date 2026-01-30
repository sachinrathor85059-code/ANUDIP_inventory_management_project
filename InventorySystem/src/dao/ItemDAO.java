// src/dao/ItemDAO.java
package dao;

import db.DatabaseConnection;
import model.Item;

import java.sql.*;
import java.util.*;

public class ItemDAO {
    public void addItem(Item item ,int id) throws SQLException {
        String sql = "INSERT INTO items(item_code, item_name, category_id, unit, sku, user_id, purchase_price, sale_price, quantity ,  gst, expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getItemCode());
            stmt.setString(2, item.getItemName());
            stmt.setInt(3, item.getCategoryId());
            stmt.setString(4, item.getUnit());
            stmt.setString(5, item.getSku());
            stmt.setInt(5, item.getUserId());

            stmt.setDouble(6, item.getPurchasePrice());
            stmt.setDouble(7, item.getSalePrice());
//            stmt.setInt(8, item.getReorderLevel());
            stmt.setDouble(8, item.getQuantity());
            stmt.setDouble(9, item.getGst());
            if (item.getExpiryDate() != null) {
                stmt.setDate(10, new java.sql.Date(item.getExpiryDate().getTime()));
            } else {
                stmt.setNull(10, Types.DATE);
            }
            stmt.setBoolean(10, item.isActive());
            stmt.executeUpdate();
        }
    }

    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Item item = new Item();
                item.setItemId(rs.getInt("item_id"));
                item.setItemCode(rs.getString("item_code"));
                item.setItemName(rs.getString("item_name"));
                item.setCategoryId(rs.getInt("category_id"));
                item.setUnit(rs.getString("unit"));
                item.setPurchasePrice(rs.getDouble("purchase_price"));
                item.setSalePrice(rs.getDouble("sale_price"));
                items.add(item);
            }
        }
        return items;
    }
    public boolean deleteItem(int id) throws SQLException {
        String sql = "DELETE FROM items WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            return affected > 0; // true if at least one row deleted
        }
    }

    public List<Item> getItemsByUser(int userId) throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE user_id = ? ORDER BY item_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRow(rs));
                }
            }
        }
        return items;
    }

    // Helper to map ResultSet → Item object
    private Item mapRow(ResultSet rs) throws SQLException {
        Item i = new Item();
        i.setItemId(rs.getInt("item_id"));
        i.setItemCode(rs.getString("item_code"));
        i.setItemName(rs.getString("item_name"));
        i.setCategoryId(rs.getInt("category_id"));
        i.setUnit(rs.getString("unit"));
        i.setPurchasePrice(rs.getDouble("purchase_price"));
        i.setSalePrice(rs.getDouble("sale_price"));
        i.setQuantity(rs.getDouble("quantity"));
        i.setActive(rs.getBoolean("is_active"));
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) i.setCreatedAt(new java.util.Date(created.getTime()));
        return i;
    }


}
