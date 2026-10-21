package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Protects customer-only routes such as cart, checkout, orders, profile, and wishlist.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
        "/cart", "/checkout", "/orders", "/order-details", "/profile", "/wishlist", "/review"
})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            String requestURI = httpRequest.getRequestURI();
            String queryString = httpRequest.getQueryString();
            String redirectUrl = requestURI + (queryString != null ? "?" + queryString : "");

            session = httpRequest.getSession(true);
            session.setAttribute("errorMessage", "Please log in to continue.");
            session.setAttribute("redirectAfterLogin", redirectUrl);

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
