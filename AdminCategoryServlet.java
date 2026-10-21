package com.shopsphere.controller;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminCategoryServlet", urlPatterns = {"/admin/categories"})
public class AdminCategoryServlet extends HttpServlet {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categories = categoryDAO.getAll();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/admin/categories.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");

        try {
            if ("create".equalsIgnoreCase(action)) {
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                boolean status = "1".equals(request.getParameter("status"));

                Category cat = new Category(0, name, description, status);
                categoryDAO.create(cat);
                session.setAttribute("toastSuccess", "Category created successfully!");

            } else if ("update".equalsIgnoreCase(action)) {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                boolean status = "1".equals(request.getParameter("status"));

                Category cat = new Category(categoryId, name, description, status);
                categoryDAO.update(cat);
                session.setAttribute("toastSuccess", "Category updated.");

            } else if ("delete".equalsIgnoreCase(action)) {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                categoryDAO.delete(categoryId);
                session.setAttribute("toastSuccess", "Category removed.");
            }
        } catch (Exception e) {
            session.setAttribute("toastError", "Error: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }
}
