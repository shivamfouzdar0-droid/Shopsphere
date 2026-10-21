package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.model.WishlistItem;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {

    public List<WishlistItem> getWishlistByUser(int userId) {
        List<WishlistItem> list = new ArrayList<>();
        String sql = "SELECT w.wishlist_id, w.user_id, w.product_id, w.created_at, " +
                     "p.name, p.brand, p.price, p.discount, p.stock, p.image_url, p.status, cat.name AS category_name " +
                     "FROM wishlist w " +
                     "JOIN products p ON w.product_id = p.product_id " +
                     "LEFT JOIN categories cat ON p.category_id = cat.category_id " +
                     "WHERE w.user_id = ? ORDER BY w.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setName(rs.getString("name"));
                    p.setBrand(rs.getString("brand"));
                    p.setPrice(rs.getDouble("price"));
                    p.setDiscount(rs.getDouble("discount"));
                    p.setStock(rs.getInt("stock"));
                    p.setImageUrl(rs.getString("image_url"));
                    p.setStatus(rs.getInt("status") == 1);
                    p.setCategoryName(rs.getString("category_name"));

                    WishlistItem item = new WishlistItem(
                            rs.getInt("wishlist_id"),
                            rs.getInt("user_id"),
                            rs.getInt("product_id"),
                            rs.getTimestamp("created_at"),
                            p
                    );
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addToWishlist(int userId, int productId) {
        String sql = "INSERT IGNORE INTO wishlist (user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeFromWishlist(int userId, int productId) {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isInWishlist(int userId, int productId) {
        String sql = "SELECT 1 FROM wishlist WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
