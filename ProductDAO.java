package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public Product findById(int productId) {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProduct(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getAll(String search, Integer categoryId, String sort, int offset, int limit) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.name AS category_name FROM products p " +
                "LEFT JOIN categories c ON p.category_id = c.category_id WHERE p.status = 1 "
        );

        List<Object> params = new ArrayList<>();
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) ");
            String keyword = "%" + search.trim() + "%";
            params.add(keyword);
            params.add(keyword);
            params.add(keyword);
        }

        if (categoryId != null && categoryId > 0) {
            sql.append(" AND p.category_id = ? ");
            params.add(categoryId);
        }

        if ("price_asc".equalsIgnoreCase(sort)) {
            sql.append(" ORDER BY (p.price - (p.price * p.discount / 100)) ASC ");
        } else if ("price_desc".equalsIgnoreCase(sort)) {
            sql.append(" ORDER BY (p.price - (p.price * p.discount / 100)) DESC ");
        } else if ("discount".equalsIgnoreCase(sort)) {
            sql.append(" ORDER BY p.discount DESC ");
        } else {
            sql.append(" ORDER BY p.product_id DESC ");
        }

        if (limit > 0) {
            sql.append(" LIMIT ? OFFSET ? ");
            params.add(limit);
            params.add(offset);
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int count(String search, Integer categoryId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products p WHERE p.status = 1 ");
        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) ");
            String keyword = "%" + search.trim() + "%";
            params.add(keyword);
            params.add(keyword);
            params.add(keyword);
        }

        if (categoryId != null && categoryId > 0) {
            sql.append(" AND p.category_id = ? ");
            params.add(categoryId);
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
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

    public List<Product> getFeatured(int limit) {
        return getAll(null, null, "discount", 0, limit);
    }

    public List<Product> getAllForAdmin() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "ORDER BY p.product_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getLowStockProducts(int threshold) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.stock <= ? ORDER BY p.stock ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(Product product) {
        String sql = "INSERT INTO products (category_id, name, brand, description, price, discount, stock, image_url, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setDouble(5, product.getPrice());
            ps.setDouble(6, product.getDiscount());
            ps.setInt(7, product.getStock());
            ps.setString(8, product.getImageUrl());
            ps.setInt(9, product.isStatus() ? 1 : 0);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        product.setProductId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Product product) {
        String sql = "UPDATE products SET category_id = ?, name = ?, brand = ?, description = ?, " +
                     "price = ?, discount = ?, stock = ?, image_url = ?, status = ? WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setDouble(5, product.getPrice());
            ps.setDouble(6, product.getDiscount());
            ps.setInt(7, product.getStock());
            ps.setString(8, product.getImageUrl());
            ps.setInt(9, product.isStatus() ? 1 : 0);
            ps.setInt(10, product.getProductId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStock(int productId, int deltaQuantity, Connection conn) throws SQLException {
        String sql = "UPDATE products SET stock = stock + ? WHERE product_id = ? AND (stock + ?) >= 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deltaQuantity);
            ps.setInt(2, productId);
            ps.setInt(3, deltaQuantity);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int productId) {
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

    private Product mapProduct(ResultSet rs) throws SQLException {
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
                rs.getInt("status") == 1,
                rs.getTimestamp("created_at")
        );
        try {
            p.setCategoryName(rs.getString("category_name"));
        } catch (SQLException ignored) {
        }
        return p;
    }
}
