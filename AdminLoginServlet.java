package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "AdminLoginServlet", urlPatterns = {"/admin/login"})
public class AdminLoginServlet extends HttpServlet {

    private final AuthenticationService authService = new AuthenticationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null && user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return;
            }
        }
        request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Administrator email and password required.");
            request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
            return;
        }

        User user = authService.login(email.trim(), password.trim());
        if (user != null && user.isAdmin()) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            request.setAttribute("errorMessage", "Access Denied: Invalid credentials or insufficient administrative privileges.");
            request.setAttribute("enteredEmail", email);
            request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
        }
    }
}
