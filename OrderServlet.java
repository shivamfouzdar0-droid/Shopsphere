package com.shopsphere.controller;

import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/order-details"})
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        String servletPath = request.getServletPath();
        if ("/order-details".equals(servletPath)) {
            String idParam = request.getParameter("id");
            if (idParam == null) {
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }
            try {
                int orderId = Integer.parseInt(idParam.trim());
                Order order = orderService.getOrderDetails(orderId);
                if (order == null || (order.getUserId() != user.getUserId() && !user.isAdmin())) {
                    response.sendRedirect(request.getContextPath() + "/orders");
                    return;
                }
                request.setAttribute("order", order);
                request.getRequestDispatcher("/order-details.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/orders");
            }
        } else {
            List<Order> orders = orderService.getUserOrders(user.getUserId());
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/orders.jsp").forward(request, response);
        }
    }
}
