package com.shopsphere.controller;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminProductServlet", urlPatterns = {"/admin/products"})
public class AdminProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productDAO.getAllForAdmin();
        List<Category> categories = categoryDAO.getAll();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");

        try {
            if ("create".equalsIgnoreCase(action)) {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                String name = request.getParameter("name");
                String brand = request.getParameter("brand");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                double discount = Double.parseDouble(request.getParameter("discount"));
                int stock = Integer.parseInt(request.getParameter("stock"));
                String imageUrl = request.getParameter("imageUrl");
                boolean status = "1".equals(request.getParameter("status"));

                Product p = new Product(0, categoryId, name, brand, description, price, discount, stock, imageUrl, status, null);
                productDAO.create(p);
                session.setAttribute("toastSuccess", "Product '" + name + "' created successfully!");

            } else if ("update".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                String name = request.getParameter("name");
                String brand = request.getParameter("brand");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                double discount = Double.parseDouble(request.getParameter("discount"));
                int stock = Integer.parseInt(request.getParameter("stock"));
                String imageUrl = request.getParameter("imageUrl");
                boolean status = "1".equals(request.getParameter("status"));

                Product p = new Product(productId, categoryId, name, brand, description, price, discount, stock, imageUrl, status, null);
                productDAO.update(p);
                session.setAttribute("toastSuccess", "Product updated successfully!");

            } else if ("delete".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                productDAO.delete(productId);
                session.setAttribute("toastSuccess", "Product deleted.");
            }
        } catch (Exception e) {
            session.setAttribute("toastError", "Operation failed: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
}
