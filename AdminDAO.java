package com.shopsphere.admin.dao;

import com.shopsphere.admin.model.Category;
import com.shopsphere.admin.model.Order;
import com.shopsphere.admin.model.Product;
import com.shopsphere.admin.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDAO {

    public User authenticateAdmin(String email, String plainPassword) {
        String hashed = hashPassword(plainPassword);
        String sql = "SELECT * FROM users WHERE email = ? AND (password = ? OR password = ?) AND role = 'ADMIN' AND status = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim());
            ps.setString(2, hashed);
            ps.setString(3, plainPassword.trim()); // Supports test password fallback
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("mobile"),
                            rs.getString("role"),
                            rs.getInt("status") == 1,
                            rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p LEFT JOIN categories c ON p.category_id = c.category_id ORDER BY p.product_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("product_id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getDouble("discount"),
                        rs.getInt("stock"),
                        rs.getString("image_url"),
                        rs.getInt("status") == 1
                );
                p.setCategoryName(rs.getString("category_name"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addProduct(Product p) {
        String sql = "INSERT INTO products (category_id, name, brand, description, price, discount, stock, image_url, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getPrice());
            ps.setDouble(6, p.getDiscount());
            ps.setInt(7, p.getStock());
            ps.setString(8, p.getImageUrl());
            ps.setInt(9, p.isStatus() ? 1 : 0);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(Product p) {
        String sql = "UPDATE products SET category_id = ?, name = ?, brand = ?, description = ?, price = ?, discount = ?, stock = ?, image_url = ?, status = ? WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getPrice());
            ps.setDouble(6, p.getDiscount());
            ps.setInt(7, p.getStock());
            ps.setString(8, p.getImageUrl());
            ps.setInt(9, p.isStatus() ? 1 : 0);
            ps.setInt(10, p.getProductId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStock(int productId, int newStock) {
        String sql = "UPDATE products SET stock = ? WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("status") == 1
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addCategory(Category c) {
        String sql = "INSERT INTO categories (name, description, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setInt(3, c.isStatus() ? 1 : 0);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS customer_name FROM orders o JOIN users u ON o.user_id = u.user_id ORDER BY o.order_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Order(
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getString("customer_name"),
                        rs.getDouble("total_amount"),
                        rs.getDouble("discount"),
                        rs.getString("payment_method"),
                        rs.getString("payment_status"),
                        rs.getString("order_status"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getAllCustomers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("mobile"),
                        rs.getString("role"),
                        rs.getInt("status") == 1,
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, Object> getSalesMetrics() {
        Map<String, Object> map = new HashMap<>();
        String sql = "SELECT COUNT(*) AS orders_count, COALESCE(SUM(total_amount - discount), 0) AS total_revenue, " +
                     "(SELECT COUNT(*) FROM products) AS products_count, " +
                     "(SELECT COUNT(*) FROM users WHERE role = 'CUSTOMER') AS customers_count " +
                     "FROM orders";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                map.put("orders", rs.getInt("orders_count"));
                map.put("revenue", rs.getDouble("total_revenue"));
                map.put("products", rs.getInt("products_count"));
                map.put("customers", rs.getInt("customers_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) sb.append('0');
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }
}
