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

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private final AuthenticationService authService = new AuthenticationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String mobile = request.getParameter("mobile");

        if (name == null || email == null || password == null || confirmPassword == null ||
            name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "All required fields must be filled.");
            retainFormValues(request, name, email, mobile);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            retainFormValues(request, name, email, mobile);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (password.length() < 6) {
            request.setAttribute("errorMessage", "Password must be at least 6 characters long.");
            retainFormValues(request, name, email, mobile);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (authService.isEmailTaken(email)) {
            request.setAttribute("errorMessage", "An account with this email address already exists.");
            retainFormValues(request, name, email, mobile);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        User newUser = authService.register(name, email, password, mobile);
        if (newUser != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("successMessage", "Registration successful! You can now log in.");
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setAttribute("errorMessage", "Registration could not be completed. Please try again.");
            retainFormValues(request, name, email, mobile);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private void retainFormValues(HttpServletRequest request, String name, String email, String mobile) {
        request.setAttribute("nameVal", name);
        request.setAttribute("emailVal", email);
        request.setAttribute("mobileVal", mobile);
    }
}
