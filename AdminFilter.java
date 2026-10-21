package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Protects administrative routes (/admin/*).
 * Grants access exclusively to authenticated users possessing the ADMIN role.
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        // Allow static resources inside admin if any or admin login endpoint
        if (path.endsWith("/admin/login") || path.endsWith("/admin/login.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !user.isAdmin()) {
            if (session != null) {
                session.setAttribute("adminErrorMessage", "Administrator credentials required.");
            }
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/login");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
