package com.shopsphere.rmi.server;

import com.shopsphere.rmi.remote.InventoryService;
import com.shopsphere.rmi.remote.Product;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Implementation of RMI Remote Interface extending UnicastRemoteObject.
 */
public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(InventoryServiceImpl.class.getName());
    private final Map<Integer, Product> inMemoryProducts = new ConcurrentHashMap<>();

    public InventoryServiceImpl() throws RemoteException {
        super();
        inMemoryProducts.put(1, new Product(1, "ProSound Wireless ANC Headphones", "AuraSound", 4999.00, 25));
        inMemoryProducts.put(2, new Product(2, "UltraBook Pro 15.6\" Retina Display", "NovaTech", 62999.00, 12));
        inMemoryProducts.put(3, new Product(3, "SmartWatch Chrono X Series", "Pulse", 2999.00, 40));
        inMemoryProducts.put(4, new Product(4, "Compact 4K Action Camera 60FPS", "VeloCam", 7499.00, 18));
    }

    @Override
    public Product getProduct(int id) throws RemoteException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT product_id, name, brand, price, stock FROM products WHERE product_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5));
                }
            }
        } catch (Exception ignored) {}
        return inMemoryProducts.get(id);
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        Product p = getProduct(productId);
        return (p != null) ? p.getStock() : -1;
    }

    @Override
    public boolean updateStock(int productId, int deltaQuantity) throws RemoteException {
        // Prevent negative final stock
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE products SET stock = stock + ? WHERE product_id = ? AND (stock + ?) >= 0")) {
            ps.setInt(1, deltaQuantity);
            ps.setInt(2, productId);
            ps.setInt(3, deltaQuantity);
            int affected = ps.executeUpdate();
            if (affected > 0) return true;
        } catch (Exception ignored) {}

        Product p = inMemoryProducts.get(productId);
        if (p != null) {
            int newStock = p.getStock() + deltaQuantity;
            if (newStock >= 0) {
                p.setStock(newStock);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Product> getAllProducts() throws RemoteException {
        List<Product> list = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT product_id, name, brand, price, stock FROM products")) {
            while (rs.next()) {
                list.add(new Product(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5)));
            }
        } catch (Exception ignored) {}

        if (list.isEmpty()) {
            list.addAll(inMemoryProducts.values());
        }
        return list;
    }

    private Connection getConnection() throws SQLException {
        String url = System.getenv("SHOPSPHERE_DB_URL");
        if (url == null || url.isEmpty()) url = "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = System.getenv("SHOPSPHERE_DB_USER");
        if (user == null || user.isEmpty()) user = "root";
        String pass = System.getenv("SHOPSPHERE_DB_PASS");
        if (pass == null) pass = "root";
        return DriverManager.getConnection(url, user, pass);
    }
}
