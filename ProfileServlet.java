package com.shopsphere.controller;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final AddressDAO addressDAO = new AddressDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        // Refresh user info
        User freshUser = userDAO.findById(user.getUserId());
        if (freshUser != null) {
            session.setAttribute("user", freshUser);
            user = freshUser;
        }

        List<Address> addresses = addressDAO.findByUserId(user.getUserId());
        int orderCount = orderDAO.findByUserId(user.getUserId()).size();

        request.setAttribute("addresses", addresses);
        request.setAttribute("orderCount", orderCount);

        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        String action = request.getParameter("action");

        if ("updateInfo".equalsIgnoreCase(action)) {
            String name = request.getParameter("name");
            String mobile = request.getParameter("mobile");

            if (name != null && !name.trim().isEmpty()) {
                user.setName(name.trim());
                user.setMobile(mobile != null ? mobile.trim() : "");
                userDAO.update(user);
                session.setAttribute("user", user);
                session.setAttribute("toastSuccess", "Profile updated successfully!");
            }
        } else if ("addAddress".equalsIgnoreCase(action)) {
            String addressLine = request.getParameter("addressLine");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String pincode = request.getParameter("pincode");
            String addressType = request.getParameter("addressType");

            if (addressLine != null && !addressLine.trim().isEmpty() &&
                city != null && !city.trim().isEmpty() &&
                pincode != null && !pincode.trim().isEmpty()) {
                Address addr = new Address();
                addr.setUserId(user.getUserId());
                addr.setAddressLine(addressLine.trim());
                addr.setCity(city.trim());
                addr.setState(state != null ? state.trim() : "Rajasthan");
                addr.setPincode(pincode.trim());
                addr.setAddressType(addressType != null ? addressType : "HOME");

                addressDAO.create(addr);
                session.setAttribute("toastSuccess", "New delivery address saved!");
            }
        } else if ("deleteAddress".equalsIgnoreCase(action)) {
            String addrIdParam = request.getParameter("addressId");
            if (addrIdParam != null) {
                try {
                    int addrId = Integer.parseInt(addrIdParam.trim());
                    addressDAO.delete(addrId, user.getUserId());
                    session.setAttribute("toastSuccess", "Address removed.");
                } catch (NumberFormatException ignored) {
                }
            }
        } else if ("changePassword".equalsIgnoreCase(action)) {
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            if (currentPassword != null && newPassword != null && newPassword.length() >= 6 &&
                newPassword.equals(confirmPassword)) {
                User freshUser = userDAO.findById(user.getUserId());
                if (freshUser != null && PasswordUtil.verifyPassword(currentPassword, freshUser.getPassword())) {
                    String hashed = PasswordUtil.hashPassword(newPassword);
                    userDAO.updatePassword(user.getUserId(), hashed);
                    session.setAttribute("toastSuccess", "Password changed successfully!");
                } else {
                    session.setAttribute("toastError", "Current password is incorrect.");
                }
            } else {
                session.setAttribute("toastError", "Password change failed. Check requirements.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
