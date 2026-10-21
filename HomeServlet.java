package com.shopsphere.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> featured = productService.getFeaturedProducts(8);
        List<Category> categories = productService.getActiveCategories();

        request.setAttribute("featuredProducts", featured);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
