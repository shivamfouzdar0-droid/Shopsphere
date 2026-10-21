package com.shopsphere.controller;

import com.shopsphere.dao.WishlistDAO;
import com.shopsphere.model.User;
import com.shopsphere.model.WishlistItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "WishlistServlet", urlPatterns = {"/wishlist"})
public class WishlistServlet extends HttpServlet {

    private final WishlistDAO wishlistDAO = new WishlistDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        List<WishlistItem> wishlist = wishlistDAO.getWishlistByUser(user.getUserId());
        request.setAttribute("wishlist", wishlist);

        request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        String action = request.getParameter("action");
        String productIdParam = request.getParameter("productId");

        if (productIdParam != null) {
            try {
                int productId = Integer.parseInt(productIdParam.trim());
                if ("add".equalsIgnoreCase(action)) {
                    wishlistDAO.addToWishlist(user.getUserId(), productId);
                    session.setAttribute("toastSuccess", "Item added to your Wishlist!");
                } else if ("remove".equalsIgnoreCase(action)) {
                    wishlistDAO.removeFromWishlist(user.getUserId(), productId);
                    session.setAttribute("toastSuccess", "Item removed from Wishlist.");
                }
            } catch (NumberFormatException ignored) {
            }
        }

        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.trim().isEmpty()) {
            response.sendRedirect(redirect);
        } else {
            response.sendRedirect(request.getContextPath() + "/wishlist");
        }
    }
}
