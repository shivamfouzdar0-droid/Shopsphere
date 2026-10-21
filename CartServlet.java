package com.shopsphere.controller;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        List<CartItem> cartItems = cartService.getCart(user.getUserId());
        double subtotal = cartService.getCartSubtotal(user.getUserId());
        int count = cartService.getCartCount(user.getUserId());

        session.setAttribute("cartCount", count);
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("subtotal", subtotal);

        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        String action = request.getParameter("action");
        String productIdParam = request.getParameter("productId");
        String quantityParam = request.getParameter("quantity");

        int productId = 0;
        if (productIdParam != null) {
            try {
                productId = Integer.parseInt(productIdParam.trim());
            } catch (NumberFormatException ignored) {
            }
        }

        int quantity = 1;
        if (quantityParam != null) {
            try {
                quantity = Integer.parseInt(quantityParam.trim());
            } catch (NumberFormatException ignored) {
            }
        }

        if ("add".equalsIgnoreCase(action)) {
            boolean added = cartService.addToCart(user.getUserId(), productId, quantity);
            if (added) {
                session.setAttribute("toastSuccess", "Item added to your shopping cart!");
            } else {
                session.setAttribute("toastError", "Could not add item. Out of stock or limit reached.");
            }
        } else if ("update".equalsIgnoreCase(action)) {
            cartService.updateQuantity(user.getUserId(), productId, quantity);
        } else if ("remove".equalsIgnoreCase(action)) {
            cartService.removeFromCart(user.getUserId(), productId);
            session.setAttribute("toastSuccess", "Item removed from cart.");
        }

        // Update cart count in session
        session.setAttribute("cartCount", cartService.getCartCount(user.getUserId()));

        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.trim().isEmpty()) {
            response.sendRedirect(redirect);
        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}
