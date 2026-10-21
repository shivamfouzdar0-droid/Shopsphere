package com.shopsphere.dao;

import com.shopsphere.model.Review;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public List<Review> findByProductId(int productId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.*, u.name AS user_name, p.name AS product_name " +
                     "FROM reviews r " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "JOIN products p ON r.product_id = p.product_id " +
                     "WHERE r.product_id = ? ORDER BY r.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review(
                            rs.getInt("review_id"),
                            rs.getInt("user_id"),
                            rs.getInt("product_id"),
                            rs.getInt("rating"),
                            rs.getString("review_text"),
                            rs.getTimestamp("created_at")
                    );
                    r.setUserName(rs.getString("user_name"));
                    r.setProductName(rs.getString("product_name"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(Review review) {
        String sql = "INSERT INTO reviews (user_id, product_id, rating, review_text) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getReviewText());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        review.setReviewId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getAverageRating(int productId) {
        String sql = "SELECT COALESCE(AVG(rating), 0.0) FROM reviews WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Math.round(rs.getDouble(1) * 10.0) / 10.0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
