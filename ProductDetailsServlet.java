package com.shopsphere.controller;

import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Product;
import com.shopsphere.model.Review;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "ProductDetailsServlet", urlPatterns = {"/product-details"})
public class ProductDetailsServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam.trim());
            Product product = productService.getProduct(productId);
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }

            List<Review> reviews = reviewDAO.findByProductId(productId);
            double avgRating = reviewDAO.getAverageRating(productId);

            // Handle Recently Viewed Cookie (RTU Cookie requirement)
            updateRecentlyViewedCookie(request, response, productId);

            request.setAttribute("product", product);
            request.setAttribute("reviews", reviews);
            request.setAttribute("avgRating", avgRating);
            request.setAttribute("reviewCount", reviews.size());

            request.getRequestDispatcher("/product-details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }

    private void updateRecentlyViewedCookie(HttpServletRequest request, HttpServletResponse response, int currentId) {
        String cookieName = "recentlyViewed";
        String existing = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (cookieName.equals(c.getName())) {
                    existing = c.getValue();
                    break;
                }
            }
        }

        List<String> ids = new ArrayList<>();
        ids.add(String.valueOf(currentId));
        if (!existing.isEmpty()) {
            String[] parts = existing.split("_");
            for (String p : parts) {
                if (!p.equals(String.valueOf(currentId)) && ids.size() < 5) {
                    ids.add(p);
                }
            }
        }

        Cookie updatedCookie = new Cookie(cookieName, String.join("_", ids));
        updatedCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        updatedCookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        response.addCookie(updatedCookie);
    }
}
