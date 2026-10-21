package com.shopsphere.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Locale;

@WebServlet(name = "LanguageServlet", urlPatterns = {"/language"})
public class LanguageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String lang = request.getParameter("lang");
        if (lang != null && (lang.equalsIgnoreCase("en") || lang.equalsIgnoreCase("hi"))) {
            HttpSession session = request.getSession(true);
            session.setAttribute("userLang", lang.toLowerCase());
            session.setAttribute("userLocale", new Locale(lang.toLowerCase()));
        }

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.trim().isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}
