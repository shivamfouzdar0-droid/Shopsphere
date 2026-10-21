package com.shopsphere.controller;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminOrderServlet", urlPatterns = {"/admin/orders"})
public class AdminOrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Order> orders = orderDAO.getAllOrders();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");
        String orderIdParam = request.getParameter("orderId");

        if (orderIdParam != null) {
            try {
                int orderId = Integer.parseInt(orderIdParam.trim());
                if ("updateOrderStatus".equalsIgnoreCase(action)) {
                    String status = request.getParameter("orderStatus");
                    orderDAO.updateOrderStatus(orderId, status);
                    session.setAttribute("toastSuccess", "Order #" + orderId + " status updated to " + status);
                } else if ("updatePaymentStatus".equalsIgnoreCase(action)) {
                    String status = request.getParameter("paymentStatus");
                    orderDAO.updatePaymentStatus(orderId, status);
                    session.setAttribute("toastSuccess", "Order #" + orderId + " payment status updated to " + status);
                }
            } catch (Exception e) {
                session.setAttribute("toastError", "Failed to update order: " + e.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}
