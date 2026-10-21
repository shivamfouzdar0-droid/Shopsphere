package com.shopsphere.dao;

import com.shopsphere.model.*;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {

    public int createOrder(Order order, Connection conn) throws SQLException {
        String sql = "INSERT INTO orders (user_id, address_id, total_amount, discount, payment_method, payment_status, order_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserId());
            if (order.getAddressId() != null && order.getAddressId() > 0) {
                ps.setInt(2, order.getAddressId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setDouble(3, order.getTotalAmount());
            ps.setDouble(4, order.getDiscount());
            ps.setString(5, order.getPaymentMethod());
            ps.setString(6, order.getPaymentStatus());
            ps.setString(7, order.getOrderStatus());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    order.setOrderId(orderId);
                    return orderId;
                }
            }
        }
        return 0;
    }

    public boolean createOrderItem(OrderItem item, Connection conn) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getPrice());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean recordPayment(Payment payment, Connection conn) throws SQLException {
        String sql = "INSERT INTO payments (order_id, payment_method, transaction_reference, amount, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setString(3, payment.getTransactionReference());
            ps.setDouble(4, payment.getAmount());
            ps.setString(5, payment.getPaymentStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        payment.setPaymentId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public Order findById(int orderId) {
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "WHERE o.order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(getOrderItems(orderId));
                    return order;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> findByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "WHERE o.user_id = ? ORDER BY o.order_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(getOrderItems(order.getOrderId()));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "ORDER BY o.order_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = mapOrder(rs);
                order.setItems(getOrderItems(order.getOrderId()));
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.name AS product_name, p.brand AS product_brand, p.image_url AS product_image " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.product_id " +
                     "WHERE oi.order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                            rs.getInt("order_item_id"),
                            rs.getInt("order_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getDouble("price")
                    );
                    item.setProductName(rs.getString("product_name"));
                    item.setProductBrand(rs.getString("product_brand"));
                    item.setProductImageUrl(rs.getString("product_image"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
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

    public boolean updatePaymentStatus(int orderId, String status) {
        String sql = "UPDATE orders SET payment_status = ? WHERE order_id = ?";
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

    public Map<String, Object> getSalesSummary() {
        Map<String, Object> summary = new HashMap<>();
        String sql = "SELECT " +
                     "COUNT(*) AS total_orders, " +
                     "COALESCE(SUM(total_amount - discount), 0) AS total_revenue, " +
                     "(SELECT COUNT(*) FROM products) AS total_products, " +
                     "(SELECT COUNT(*) FROM users WHERE role = 'CUSTOMER') AS total_customers, " +
                     "(SELECT COUNT(*) FROM products WHERE stock <= 10) AS low_stock_count " +
                     "FROM orders";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                summary.put("totalOrders", rs.getInt("total_orders"));
                summary.put("totalRevenue", rs.getDouble("total_revenue"));
                summary.put("totalProducts", rs.getInt("total_products"));
                summary.put("totalCustomers", rs.getInt("total_customers"));
                summary.put("lowStockCount", rs.getInt("low_stock_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return summary;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order(
                rs.getInt("order_id"),
                rs.getInt("user_id"),
                rs.getInt("address_id"),
                rs.getDouble("total_amount"),
                rs.getDouble("discount"),
                rs.getString("payment_method"),
                rs.getString("payment_status"),
                rs.getString("order_status"),
                rs.getTimestamp("created_at")
        );
        o.setCustomerName(rs.getString("user_name"));
        o.setCustomerEmail(rs.getString("user_email"));

        if (rs.getString("address_line") != null) {
            Address addr = new Address();
            addr.setAddressId(rs.getInt("address_id"));
            addr.setAddressLine(rs.getString("address_line"));
            addr.setCity(rs.getString("city"));
            addr.setState(rs.getString("state"));
            addr.setPincode(rs.getString("pincode"));
            addr.setAddressType(rs.getString("address_type"));
            o.setShippingAddress(addr);
        }
        return o;
    }
}
