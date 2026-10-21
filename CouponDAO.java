package com.shopsphere.dao;

import com.shopsphere.model.Coupon;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouponDAO {

    public Coupon findByCode(String code) {
        String sql = "SELECT * FROM coupons WHERE UPPER(code) = UPPER(?) AND status = 1 AND expiry_date >= CURRENT_DATE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCoupon(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Coupon> getAllCoupons() {
        List<Coupon> list = new ArrayList<>();
        String sql = "SELECT * FROM coupons ORDER BY coupon_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapCoupon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(Coupon coupon) {
        String sql = "INSERT INTO coupons (code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, coupon.getCode().toUpperCase().trim());
            ps.setString(2, coupon.getDiscountType());
            ps.setDouble(3, coupon.getDiscountValue());
            ps.setDouble(4, coupon.getMinimumOrder());
            ps.setDouble(5, coupon.getMaximumDiscount());
            ps.setDate(6, coupon.getExpiryDate());
            ps.setInt(7, coupon.isStatus() ? 1 : 0);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        coupon.setCouponId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(int couponId, boolean status) {
        String sql = "UPDATE coupons SET status = ? WHERE coupon_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status ? 1 : 0);
            ps.setInt(2, couponId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasUserUsedCoupon(int couponId, int userId) {
        String sql = "SELECT 1 FROM coupon_usage WHERE coupon_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean recordUsage(int couponId, int userId, int orderId, Connection conn) throws SQLException {
        String sql = "INSERT INTO coupon_usage (coupon_id, user_id, order_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private Coupon mapCoupon(ResultSet rs) throws SQLException {
        return new Coupon(
                rs.getInt("coupon_id"),
                rs.getString("code"),
                rs.getString("discount_type"),
                rs.getDouble("discount_value"),
                rs.getDouble("minimum_order"),
                rs.getDouble("maximum_discount"),
                rs.getDate("expiry_date"),
                rs.getInt("status") == 1
        );
    }
}
