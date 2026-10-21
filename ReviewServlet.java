package com.shopsphere.controller;

import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Review;
import com.shopsphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ReviewServlet", urlPatterns = {"/review"})
public class ReviewServlet extends HttpServlet {

    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        String productIdParam = request.getParameter("productId");
        String ratingParam = request.getParameter("rating");
        String reviewText = request.getParameter("reviewText");

        if (productIdParam != null && ratingParam != null) {
            try {
                int productId = Integer.parseInt(productIdParam.trim());
                int rating = Math.min(5, Math.max(1, Integer.parseInt(ratingParam.trim())));

                Review review = new Review();
                review.setUserId(user.getUserId());
                review.setProductId(productId);
                review.setRating(rating);
                review.setReviewText(reviewText != null ? reviewText.trim() : "");

                reviewDAO.create(review);
                session.setAttribute("toastSuccess", "Thank you for your valuable feedback!");

                response.sendRedirect(request.getContextPath() + "/product-details?id=" + productId);
                return;
            } catch (NumberFormatException ignored) {
            }
        }

        response.sendRedirect(request.getContextPath() + "/products");
    }
}
