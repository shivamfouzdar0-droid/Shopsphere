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

@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private static final int PAGE_SIZE = 9;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String search = request.getParameter("search");
        String categoryParam = request.getParameter("category");
        String sort = request.getParameter("sort");
        String pageParam = request.getParameter("page");

        Integer categoryId = null;
        if (categoryParam != null && !categoryParam.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryParam.trim());
            } catch (NumberFormatException ignored) {
            }
        }

        int page = 1;
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Math.max(1, Integer.parseInt(pageParam.trim()));
            } catch (NumberFormatException ignored) {
            }
        }

        List<Product> products = productService.getProducts(search, categoryId, sort, page, PAGE_SIZE);
        int totalProducts = productService.getProductCount(search, categoryId);
        int totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);

        List<Category> categories = productService.getActiveCategories();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", Math.max(1, totalPages));
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("selectedCategory", categoryId);
        request.setAttribute("searchQuery", search);
        request.setAttribute("currentSort", sort);

        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }
}
