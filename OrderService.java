package com.shopsphere.service;

import com.shopsphere.dao.*;
import com.shopsphere.model.*;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderService {

    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;
    private final CouponDAO couponDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
        this.couponDAO = new CouponDAO();
    }

    public OrderService(OrderDAO orderDAO, CartDAO cartDAO, ProductDAO productDAO, CouponDAO couponDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.couponDAO = couponDAO;
    }

    /**
     * Executes a full transactional checkout workflow.
     * Validates stock, calculates server-side totals, creates order & items, records payment,
     * updates inventory, records coupon usage, and clears cart atomically.
     */
    public Order placeOrder(int userId, Integer addressId, String paymentMethod, String couponCode) throws Exception {
        List<CartItem> cartItems = cartDAO.getCartByUser(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Shopping cart is empty.");
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin Transaction

            // 1. Validate stock & calculate subtotal
            double subtotal = 0.0;
            for (CartItem item : cartItems) {
                Product p = productDAO.findById(item.getProductId());
                if (p == null || !p.isStatus() || p.getStock() < item.getQuantity()) {
                    throw new IllegalStateException("Insufficient inventory for: " + (p != null ? p.getName() : "Item"));
                }
                subtotal += item.getSubtotal();
            }

            // 2. Validate Coupon & Discount
            double discount = 0.0;
            Coupon appliedCoupon = null;
            if (couponCode != null && !couponCode.trim().isEmpty()) {
                appliedCoupon = couponDAO.findByCode(couponCode.trim());
                if (appliedCoupon != null && appliedCoupon.isStatus() && subtotal >= appliedCoupon.getMinimumOrder()) {
                    if (!couponDAO.hasUserUsedCoupon(appliedCoupon.getCouponId(), userId)) {
                        discount = appliedCoupon.calculateDiscount(subtotal);
                    }
                }
            }

            double finalPayable = Math.max(0.0, Math.round((subtotal - discount) * 100.0) / 100.0);
            String payStatus = "COD".equalsIgnoreCase(paymentMethod) ? "PENDING" : "PAID";
            String orderStatus = "CONFIRMED";

            // 3. Create Order
            Order order = new Order();
            order.setUserId(userId);
            order.setAddressId(addressId);
            order.setTotalAmount(subtotal);
            order.setDiscount(discount);
            order.setPaymentMethod(paymentMethod != null ? paymentMethod.toUpperCase() : "COD");
            order.setPaymentStatus(payStatus);
            order.setOrderStatus(orderStatus);

            int orderId = orderDAO.createOrder(order, conn);
            if (orderId <= 0) {
                throw new SQLException("Failed to generate order record.");
            }

            // 4. Create Order Items & Deduct Inventory
            for (CartItem item : cartItems) {
                OrderItem oi = new OrderItem();
                oi.setOrderId(orderId);
                oi.setProductId(item.getProductId());
                oi.setQuantity(item.getQuantity());
                oi.setPrice(item.getProduct().getDiscountedPrice());

                orderDAO.createOrderItem(oi, conn);

                // Deduct stock
                boolean stockUpdated = productDAO.updateStock(item.getProductId(), -item.getQuantity(), conn);
                if (!stockUpdated) {
                    throw new SQLException("Failed to allocate inventory for product ID: " + item.getProductId());
                }
            }

            // 5. Record Simulated Payment
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setPaymentMethod(paymentMethod != null ? paymentMethod.toUpperCase() : "COD");
            payment.setTransactionReference("TXN_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            payment.setAmount(finalPayable);
            payment.setPaymentStatus(payStatus.equals("PAID") ? "SUCCESS" : "PENDING");
            orderDAO.recordPayment(payment, conn);

            // 6. Record Coupon Usage
            if (appliedCoupon != null && discount > 0) {
                couponDAO.recordUsage(appliedCoupon.getCouponId(), userId, orderId, conn);
            }

            // 7. Clear Shopping Cart
            cartDAO.clearCart(userId, conn);

            // Commit Transaction
            conn.commit();
            LOGGER.info("Order #" + orderId + " placed successfully for user #" + userId);

            // Populate order items for return
            order.setItems(orderDAO.getOrderItems(orderId));
            return order;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    LOGGER.warning("Transaction rolled back during order placement: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    LOGGER.log(Level.SEVERE, "Failed to rollback connection", rollbackEx);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public List<Order> getUserOrders(int userId) {
        return orderDAO.findByUserId(userId);
    }

    public Order getOrderDetails(int orderId) {
        return orderDAO.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateOrderStatus(orderId, status);
    }

    public boolean updatePaymentStatus(int orderId, String status) {
        return orderDAO.updatePaymentStatus(orderId, status);
    }

    public Map<String, Object> getDashboardSummary() {
        return orderDAO.getSalesSummary();
    }
}
