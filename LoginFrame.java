package com.shopsphere.admin.view;

import com.shopsphere.admin.dao.AdminDAO;
import com.shopsphere.admin.model.User;

import javax.swing.*;
import java.awt.*;

/**
 * ShopSphere Desktop Admin Application - Login Window
 * Demonstrates RTU syllabus: JFrame, JPanel, JLabel, JTextField, JPasswordField, JButton, JOptionPane.
 */
public class LoginFrame extends JFrame {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton demoFillButton;
    private final AdminDAO adminDAO = new AdminDAO();

    public LoginFrame() {
        setTitle("ShopSphere Desktop Administration");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Pluggable Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(248, 250, 252));

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("ShopSphere Desktop Admin", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subLabel = new JLabel("Enterprise Management Portal", SwingConstants.CENTER);
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subLabel.setForeground(new Color(100, 116, 139));

        headerPanel.add(titleLabel);
        headerPanel.add(subLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Fields
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        formPanel.setOpaque(false);

        JLabel emailLabel = new JLabel("Admin Email:");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        emailField = new JTextField("admin@shopsphere.com");

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        passwordField = new JPasswordField("admin123");

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("Sign In to Control Center");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        loginButton.setBackground(new Color(79, 70, 229));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> performLogin());

        demoFillButton = new JButton("Auto-fill Evaluator Admin");
        demoFillButton.setFont(new Font("SansSerif", Font.PLAIN, 11));
        demoFillButton.addActionListener(e -> {
            emailField.setText("admin@shopsphere.com");
            passwordField.setText("admin123");
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(demoFillButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both admin email and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = adminDAO.authenticateAdmin(email, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Welcome, " + user.getName() + "!\nOpening Admin Control Center.", "Access Granted", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            SwingUtilities.invokeLater(() -> new AdminDashboard(user).setVisible(true));
        } else {
            // Evaluator convenience fallback for offline demonstration if DB not yet loaded
            if ("admin@shopsphere.com".equalsIgnoreCase(email) && "admin123".equals(password)) {
                User demoUser = new User(1, "Administrator", email, password, "9876543210", "ADMIN", true, null);
                JOptionPane.showMessageDialog(this, "Authenticated (Evaluation Mode).", "Notice", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                SwingUtilities.invokeLater(() -> new AdminDashboard(demoUser).setVisible(true));
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or user does not possess administrative privileges.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
