package com.shopsphere.controller;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.CouponService;
import com.shopsphere.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private final CartService cartService = new CartService();
    private final AddressDAO addressDAO = new AddressDAO();
    private final CouponService couponService = new CouponService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        List<CartItem> cartItems = cartService.getCart(user.getUserId());
        if (cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        List<Address> addresses = addressDAO.findByUserId(user.getUserId());
        double subtotal = cartService.getCartSubtotal(user.getUserId());

        String couponCode = request.getParameter("couponCode");
        double discount = 0.0;
        Coupon appliedCoupon = null;
        if (couponCode != null && !couponCode.trim().isEmpty()) {
            appliedCoupon = couponService.validateCoupon(couponCode.trim(), user.getUserId(), subtotal);
            if (appliedCoupon != null) {
                discount = appliedCoupon.calculateDiscount(subtotal);
                request.setAttribute("appliedCoupon", appliedCoupon);
                request.setAttribute("couponSuccess", "Coupon '" + appliedCoupon.getCode() + "' applied successfully!");
            } else {
                request.setAttribute("couponError", "Invalid or expired coupon code.");
            }
        }

        double finalPayable = Math.max(0.0, Math.round((subtotal - discount) * 100.0) / 100.0);

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("addresses", addresses);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("discount", discount);
        request.setAttribute("finalPayable", finalPayable);

        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        String addressIdParam = request.getParameter("addressId");
        String paymentMethod = request.getParameter("paymentMethod");
        String couponCode = request.getParameter("couponCode");

        // Allow entering new address directly at checkout
        Integer addressId = null;
        if ("new".equalsIgnoreCase(addressIdParam) || addressIdParam == null || addressIdParam.trim().isEmpty()) {
            String addressLine = request.getParameter("addressLine");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String pincode = request.getParameter("pincode");
            String addressType = request.getParameter("addressType");

            if (addressLine != null && !addressLine.trim().isEmpty() &&
                city != null && !city.trim().isEmpty() &&
                pincode != null && !pincode.trim().isEmpty()) {
                Address newAddr = new Address();
                newAddr.setUserId(user.getUserId());
                newAddr.setAddressLine(addressLine.trim());
                newAddr.setCity(city.trim());
                newAddr.setState(state != null ? state.trim() : "Rajasthan");
                newAddr.setPincode(pincode.trim());
                newAddr.setAddressType(addressType != null ? addressType : "HOME");

                if (addressDAO.create(newAddr)) {
                    addressId = newAddr.getAddressId();
                }
            }
        } else {
            try {
                addressId = Integer.parseInt(addressIdParam.trim());
            } catch (NumberFormatException ignored) {
            }
        }

        try {
            Order order = orderService.placeOrder(user.getUserId(), addressId, paymentMethod, couponCode);
            session.setAttribute("cartCount", 0);
            response.sendRedirect(request.getContextPath() + "/order-success?orderId=" + order.getOrderId());
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Checkout failed: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
}
