package com.shopsphere.controller;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Order;
import com.shopsphere.model.Product;
import com.shopsphere.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> summary = orderService.getDashboardSummary();
        List<Order> recentOrders = orderDAO.getAllOrders();
        if (recentOrders.size() > 5) {
            recentOrders = recentOrders.subList(0, 5);
        }
        List<Product> lowStockProducts = productDAO.getLowStockProducts(10);

        request.setAttribute("summary", summary);
        request.setAttribute("recentOrders", recentOrders);
        request.setAttribute("lowStockProducts", lowStockProducts);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
