package com.shopsphere.admin.view;

import com.shopsphere.admin.dao.AdminDAO;
import com.shopsphere.admin.model.Category;
import com.shopsphere.admin.model.Order;
import com.shopsphere.admin.model.Product;
import com.shopsphere.admin.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * ShopSphere Desktop Admin Dashboard
 * Demonstrates RTU syllabus: JFrame, JTabbedPane, JTable, JScrollPane, JComboBox, JButton, JTextField, JOptionPane.
 */
public class AdminDashboard extends JFrame {

    private final User currentUser;
    private final AdminDAO adminDAO = new AdminDAO();

    // Table Models
    private DefaultTableModel productTableModel;
    private DefaultTableModel categoryTableModel;
    private DefaultTableModel orderTableModel;
    private DefaultTableModel customerTableModel;

    // Overview Metric Labels
    private JLabel revenueLabel;
    private JLabel ordersCountLabel;
    private JLabel productsCountLabel;
    private JLabel customersCountLabel;

    public AdminDashboard(User user) {
        this.currentUser = user;
        setTitle("ShopSphere Admin Control Center - Logged in as: " + user.getName());
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Top Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(15, 23, 42));

        JLabel titleLabel = new JLabel("  ShopSphere Desktop Admin  |  ");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        toolBar.add(titleLabel);

        JButton refreshBtn = new JButton("Refresh Data");
        refreshBtn.addActionListener(e -> refreshAllData());
        toolBar.add(refreshBtn);

        toolBar.add(Box.createHorizontalGlue());

        JButton logoutBtn = new JButton("Sign Out");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        toolBar.add(logoutBtn);
        toolBar.add(new JLabel("  "));

        add(toolBar, BorderLayout.NORTH);

        // Main Tabbed Interface
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 12));

        tabbedPane.addTab("Store Overview", createOverviewPanel());
        tabbedPane.addTab("Products & Stock", createProductsPanel());
        tabbedPane.addTab("Categories", createCategoriesPanel());
        tabbedPane.addTab("Orders", createOrdersPanel());
        tabbedPane.addTab("Customers", createCustomersPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Load initial data
        refreshAllData();
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(248, 250, 252));

        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 15, 15));
        statsGrid.setOpaque(false);

        statsGrid.add(createCard("Total Store Revenue", revenueLabel = new JLabel("₹0.00"), new Color(79, 70, 229)));
        statsGrid.add(createCard("Processed Orders", ordersCountLabel = new JLabel("0"), new Color(16, 185, 129)));
        statsGrid.add(createCard("Catalog Products", productsCountLabel = new JLabel("0"), new Color(14, 165, 233)));
        statsGrid.add(createCard("Registered Customers", customersCountLabel = new JLabel("0"), new Color(245, 158, 11)));

        panel.add(statsGrid, BorderLayout.NORTH);

        // Academic RTU information panel
        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("RTU Advanced Java Architecture & System Diagnostics"));
        infoPanel.setBackground(Color.WHITE);

        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setText("=== SHOPSPHERE DESKTOP SWING ADMINISTRATION ===\n" +
                   "• GUI Architecture: Swing MVC (View: AdminDashboard / LoginFrame, Model: POJO entities, Controller/DAO: AdminDAO)\n" +
                   "• Persistence Layer: JDBC MySQL Connector/J with PreparedStatements\n" +
                   "• Concurrent Access: Shared database instance with Jakarta EE Web Storefront\n" +
                   "• Pluggable Look and Feel: " + UIManager.getLookAndFeel().getName() + "\n" +
                   "• Java Version: " + System.getProperty("java.version") + " (" + System.getProperty("os.name") + ")\n" +
                   "• Target Evaluation: RTU Advanced Java Practical Examination / College Capstone\n");
        infoPanel.add(new JScrollPane(ta), BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titleLbl.setForeground(new Color(100, 116, 139));

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        valueLabel.setForeground(accentColor);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] cols = {"ID", "Name", "Brand", "Category", "Price (₹)", "Discount (%)", "Stock", "Status"};
        productTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(productTableModel);
        table.setRowHeight(24);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Actions
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton addBtn = new JButton("Add Product");
        JButton updateStockBtn = new JButton("Update Stock");
        JButton deleteBtn = new JButton("Delete Product");

        addBtn.addActionListener(e -> showAddProductDialog());
        updateStockBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) productTableModel.getValueAt(selectedRow, 0);
                String currentStock = productTableModel.getValueAt(selectedRow, 6).toString();
                String input = JOptionPane.showInputDialog(this, "Enter new stock level for Product #" + id + ":", currentStock);
                if (input != null && !input.trim().isEmpty()) {
                    try {
                        int newStock = Integer.parseInt(input.trim());
                        adminDAO.updateStock(id, newStock);
                        refreshAllData();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid quantity", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a product row first.");
            }
        });
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) productTableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Delete product #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    adminDAO.deleteProduct(id);
                    refreshAllData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a product row first.");
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(updateStockBtn);
        btnPanel.add(deleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showAddProductDialog() {
        JTextField nameF = new JTextField();
        JTextField brandF = new JTextField();
        JTextField priceF = new JTextField("999.00");
        JTextField discountF = new JTextField("0.0");
        JTextField stockF = new JTextField("20");
        JTextField imgF = new JTextField("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600");
        JTextField descF = new JTextField("Standard retail item");

        List<Category> categories = adminDAO.getAllCategories();
        JComboBox<Category> catCombo = new JComboBox<>(categories.toArray(new Category[0]));

        Object[] fields = {
                "Product Name:", nameF,
                "Brand:", brandF,
                "Category:", catCombo,
                "Base Price (₹):", priceF,
                "Discount (%):", discountF,
                "Stock Quantity:", stockF,
                "Image URL:", imgF,
                "Description:", descF
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add New Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Category selectedCat = (Category) catCombo.getSelectedItem();
                int catId = (selectedCat != null) ? selectedCat.getCategoryId() : 1;
                Product p = new Product(
                        0, catId, nameF.getText().trim(), brandF.getText().trim(), descF.getText().trim(),
                        Double.parseDouble(priceF.getText().trim()), Double.parseDouble(discountF.getText().trim()),
                        Integer.parseInt(stockF.getText().trim()), imgF.getText().trim(), true
                );
                adminDAO.addProduct(p);
                refreshAllData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] cols = {"ID", "Category Name", "Description", "Status"};
        categoryTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(categoryTableModel);
        table.setRowHeight(24);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addBtn = new JButton("Add Category");
        addBtn.addActionListener(e -> {
            JTextField nameF = new JTextField();
            JTextField descF = new JTextField();
            Object[] fields = {"Category Name:", nameF, "Description:", descF};
            int res = JOptionPane.showConfirmDialog(this, fields, "Create Category", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION && !nameF.getText().trim().isEmpty()) {
                adminDAO.addCategory(new Category(0, nameF.getText().trim(), descF.getText().trim(), true));
                refreshAllData();
            }
        });
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(addBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] cols = {"Order #", "Customer", "Amount (₹)", "Payment", "Payment Status", "Order Status", "Date"};
        orderTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(orderTableModel);
        table.setRowHeight(24);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"});
        JButton updateStatusBtn = new JButton("Update Selected Order Status");

        updateStatusBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int orderId = (int) orderTableModel.getValueAt(selectedRow, 0);
                String newStatus = (String) statusCombo.getSelectedItem();
                adminDAO.updateOrderStatus(orderId, newStatus);
                refreshAllData();
                JOptionPane.showMessageDialog(this, "Order #" + orderId + " updated to " + newStatus);
            } else {
                JOptionPane.showMessageDialog(this, "Select an order row first.");
            }
        });

        btnPanel.add(new JLabel("Change Status:"));
        btnPanel.add(statusCombo);
        btnPanel.add(updateStatusBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] cols = {"User ID", "Name", "Email", "Mobile", "Role", "Status"};
        customerTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(customerTableModel);
        table.setRowHeight(24);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private void refreshAllData() {
        // 1. Overview metrics
        Map<String, Object> metrics = adminDAO.getSalesMetrics();
        if (metrics.containsKey("revenue")) {
            revenueLabel.setText("₹" + metrics.get("revenue"));
            ordersCountLabel.setText(String.valueOf(metrics.get("orders")));
            productsCountLabel.setText(String.valueOf(metrics.get("products")));
            customersCountLabel.setText(String.valueOf(metrics.get("customers")));
        }

        // 2. Products Table
        productTableModel.setRowCount(0);
        List<Product> products = adminDAO.getAllProducts();
        for (Product p : products) {
            productTableModel.addRow(new Object[]{
                    p.getProductId(), p.getName(), p.getBrand(), p.getCategoryName(),
                    p.getPrice(), p.getDiscount(), p.getStock(), p.isStatus() ? "Active" : "Hidden"
            });
        }

        // 3. Categories Table
        categoryTableModel.setRowCount(0);
        List<Category> categories = adminDAO.getAllCategories();
        for (Category c : categories) {
            categoryTableModel.addRow(new Object[]{
                    c.getCategoryId(), c.getName(), c.getDescription(), c.isStatus() ? "Active" : "Disabled"
            });
        }

        // 4. Orders Table
        orderTableModel.setRowCount(0);
        List<Order> orders = adminDAO.getAllOrders();
        for (Order o : orders) {
            orderTableModel.addRow(new Object[]{
                    o.getOrderId(), o.getCustomerName(), o.getFinalPayable(),
                    o.getPaymentMethod(), o.getPaymentStatus(), o.getOrderStatus(),
                    o.getCreatedAt() != null ? o.getCreatedAt().toString() : ""
            });
        }

        // 5. Customers Table
        customerTableModel.setRowCount(0);
        List<User> customers = adminDAO.getAllCustomers();
        for (User u : customers) {
            customerTableModel.addRow(new Object[]{
                    u.getUserId(), u.getName(), u.getEmail(), u.getMobile(), u.getRole(), u.isStatus() ? "Active" : "Disabled"
            });
        }
    }
}
