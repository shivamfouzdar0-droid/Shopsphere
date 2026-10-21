package com.shopsphere.controller;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.Coupon;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "AdminCouponServlet", urlPatterns = {"/admin/coupons"})
public class AdminCouponServlet extends HttpServlet {

    private final CouponDAO couponDAO = new CouponDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Coupon> coupons = couponDAO.getAllCoupons();
        request.setAttribute("coupons", coupons);
        request.getRequestDispatcher("/admin/coupons.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");

        try {
            if ("create".equalsIgnoreCase(action)) {
                String code = request.getParameter("code");
                String discountType = request.getParameter("discountType");
                double discountValue = Double.parseDouble(request.getParameter("discountValue"));
                double minimumOrder = Double.parseDouble(request.getParameter("minimumOrder"));
                double maximumDiscount = Double.parseDouble(request.getParameter("maximumDiscount"));
                Date expiryDate = Date.valueOf(request.getParameter("expiryDate"));
                boolean status = "1".equals(request.getParameter("status"));

                Coupon coupon = new Coupon(0, code, discountType, discountValue, minimumOrder, maximumDiscount, expiryDate, status);
                couponDAO.create(coupon);
                session.setAttribute("toastSuccess", "Coupon '" + code + "' created successfully!");

            } else if ("toggleStatus".equalsIgnoreCase(action)) {
                int couponId = Integer.parseInt(request.getParameter("couponId"));
                boolean status = "1".equals(request.getParameter("status"));
                couponDAO.updateStatus(couponId, status);
                session.setAttribute("toastSuccess", "Coupon status updated.");
            }
        } catch (Exception e) {
            session.setAttribute("toastError", "Error: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/coupons");
    }
}
