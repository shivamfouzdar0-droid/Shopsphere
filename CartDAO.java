package com.shopsphere.dao;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public List<CartItem> getCartByUser(int userId) {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT c.cart_id, c.user_id, c.product_id, c.quantity, " +
                     "p.name, p.brand, p.price, p.discount, p.stock, p.image_url, p.status, cat.name AS category_name " +
                     "FROM cart c " +
                     "JOIN products p ON c.product_id = p.product_id " +
                     "LEFT JOIN categories cat ON p.category_id = cat.category_id " +
                     "WHERE c.user_id = ?";
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

                    CartItem item = new CartItem(
                            rs.getInt("cart_id"),
                            rs.getInt("user_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
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

    public boolean addToCart(int userId, int productId, int quantity) {
        String checkSql = "SELECT cart_id, quantity FROM cart WHERE user_id = ? AND product_id = ?";
        String updateSql = "UPDATE cart SET quantity = quantity + ? WHERE cart_id = ?";
        String insertSql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                psCheck.setInt(1, userId);
                psCheck.setInt(2, productId);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        int cartId = rs.getInt("cart_id");
                        try (PreparedStatement psUp = conn.prepareStatement(updateSql)) {
                            psUp.setInt(1, quantity);
                            psUp.setInt(2, cartId);
                            return psUp.executeUpdate() > 0;
                        }
                    } else {
                        try (PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                            psIns.setInt(1, userId);
                            psIns.setInt(2, productId);
                            psIns.setInt(3, quantity);
                            return psIns.executeUpdate() > 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateQuantity(int userId, int productId, int quantity) {
        if (quantity <= 0) {
            return removeFromCart(userId, productId);
        }
        String sql = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeFromCart(int userId, int productId) {
        String sql = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
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

    public boolean clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean clearCart(int userId, Connection conn) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() >= 0;
        }
    }

    public int getCartCount(int userId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
