package com.shopsphere.service;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.Coupon;

import java.util.List;

public class CouponService {

    private final CouponDAO couponDAO;

    public CouponService() {
        this.couponDAO = new CouponDAO();
    }

    public CouponService(CouponDAO couponDAO) {
        this.couponDAO = couponDAO;
    }

    /**
     * Validates a coupon code for a user against their subtotal.
     * @return valid Coupon, or null if invalid/expired/already used
     */
    public Coupon validateCoupon(String code, int userId, double subtotal) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        Coupon coupon = couponDAO.findByCode(code.trim());
        if (coupon == null || !coupon.isStatus()) {
            return null;
        }

        if (subtotal < coupon.getMinimumOrder()) {
            return null;
        }

        if (couponDAO.hasUserUsedCoupon(coupon.getCouponId(), userId)) {
            return null; // Already redeemed once
        }

        return coupon;
    }

    public List<Coupon> getAllCoupons() {
        return couponDAO.getAllCoupons();
    }

    public boolean createCoupon(Coupon coupon) {
        return couponDAO.create(coupon);
    }

    public boolean toggleStatus(int couponId, boolean status) {
        return couponDAO.updateStatus(couponId, status);
    }
}
