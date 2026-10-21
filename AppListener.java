package com.shopsphere.listener;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.model.Category;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Demonstrates RTU syllabus ServletContextListener and HttpSessionListener.
 * Initializes application context attributes, caches categories, and tracks active sessions.
 */
@WebListener
public class AppListener implements ServletContextListener, HttpSessionListener {

    private static final Logger LOGGER = Logger.getLogger(AppListener.class.getName());
    private static final AtomicInteger ACTIVE_SESSIONS = new AtomicInteger(0);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute("appName", "ShopSphere");
        context.setAttribute("appTagline", "Modern Enterprise E-Commerce Platform");
        context.setAttribute("appVersion", "1.0.0-RTU");

        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> categories = categoryDAO.getActive();
            context.setAttribute("globalCategories", categories);
            LOGGER.info("ShopSphere initialized successfully. Loaded " + categories.size() + " active categories.");
        } catch (Exception e) {
            LOGGER.warning("Could not pre-load categories during context initialization: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("ShopSphere context destroyed. Releasing system resources.");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int count = ACTIVE_SESSIONS.incrementAndGet();
        se.getSession().getServletContext().setAttribute("activeSessionCount", count);
        LOGGER.fine("New HTTP session created: " + se.getSession().getId() + " | Active: " + count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int count = ACTIVE_SESSIONS.decrementAndGet();
        se.getSession().getServletContext().setAttribute("activeSessionCount", count);
        LOGGER.fine("HTTP session invalidated: " + se.getSession().getId() + " | Active: " + count);
    }

    public static int getActiveSessionCount() {
        return ACTIVE_SESSIONS.get();
    }
}
