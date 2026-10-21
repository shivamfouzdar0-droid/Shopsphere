package com.shopsphere.controller;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminUserServlet", urlPatterns = {"/admin/users"})
public class AdminUserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");
        String userIdParam = request.getParameter("userId");

        if (userIdParam != null) {
            try {
                int userId = Integer.parseInt(userIdParam.trim());
                if ("toggleStatus".equalsIgnoreCase(action)) {
                    boolean newStatus = "1".equals(request.getParameter("status"));
                    userDAO.updateStatus(userId, newStatus);
                    session.setAttribute("toastSuccess", "Customer status updated.");
                }
            } catch (Exception e) {
                session.setAttribute("toastError", "User status update failed: " + e.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
