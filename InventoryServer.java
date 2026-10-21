package com.shopsphere.network.server;

import com.shopsphere.network.model.NetworkRequest;
import com.shopsphere.network.model.NetworkResponse;
import com.shopsphere.network.model.Product;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Demonstrates RTU syllabus: java.net.ServerSocket, Socket, Object Serialization.
 * Multi-threaded TCP inventory server servicing client requests.
 */
public class InventoryServer {

    private static final Logger LOGGER = Logger.getLogger(InventoryServer.class.getName());
    public static final int DEFAULT_PORT = 8089;
    private static final Map<Integer, Product> DEMO_INVENTORY = new ConcurrentHashMap<>();

    static {
        DEMO_INVENTORY.put(1, new Product(1, "ProSound Wireless ANC Headphones", "AuraSound", 4999.00, 25));
        DEMO_INVENTORY.put(2, new Product(2, "UltraBook Pro 15.6\" Retina Display", "NovaTech", 62999.00, 12));
        DEMO_INVENTORY.put(3, new Product(3, "SmartWatch Chrono X Series", "Pulse", 2999.00, 40));
        DEMO_INVENTORY.put(4, new Product(4, "Compact 4K Action Camera 60FPS", "VeloCam", 7499.00, 18));
        DEMO_INVENTORY.put(5, new Product(5, "Classic Tailored Oxford Shirt", "UrbanThread", 1499.00, 50));
    }

    private final int port;
    private volatile boolean running = true;

    public InventoryServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LOGGER.info("ShopSphere Inventory TCP Server started on port " + port);
            System.out.println(">>> ShopSphere ServerSocket listening on port " + port + "...");

            while (running) {
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("Client connected: " + clientSocket.getRemoteSocketAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            LOGGER.severe("Server error: " + e.getMessage());
        }
    }

    public void stop() {
        this.running = false;
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                Object received = in.readObject();
                if (received instanceof NetworkRequest) {
                    NetworkRequest req = (NetworkRequest) received;
                    NetworkResponse res = handleRequest(req);
                    out.writeObject(res);
                    out.flush();
                }

            } catch (Exception e) {
                LOGGER.warning("Error processing client request: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }

        private NetworkResponse handleRequest(NetworkRequest req) {
            String cmd = (req.getCommand() != null) ? req.getCommand().toUpperCase().trim() : "";
            String arg = (req.getArgument() != null) ? req.getArgument().trim() : "";

            NetworkResponse res = new NetworkResponse();

            switch (cmd) {
                case "GET_PRODUCT": {
                    try {
                        int id = Integer.parseInt(arg);
                        Product p = findProductById(id);
                        if (p != null) {
                            res.setSuccess(true);
                            res.setMessage("Product retrieved successfully.");
                            res.setProduct(p);
                        } else {
                            res.setSuccess(false);
                            res.setMessage("Product with ID " + id + " not found.");
                        }
                    } catch (NumberFormatException e) {
                        res.setSuccess(false);
                        res.setMessage("Invalid product ID: " + arg);
                    }
                    break;
                }
                case "GET_STOCK": {
                    try {
                        int id = Integer.parseInt(arg);
                        Product p = findProductById(id);
                        if (p != null) {
                            res.setSuccess(true);
                            res.setStockLevel(p.getStock());
                            res.setMessage("Available stock for Product #" + id + ": " + p.getStock() + " units.");
                        } else {
                            res.setSuccess(false);
                            res.setMessage("Product with ID " + id + " not found.");
                        }
                    } catch (NumberFormatException e) {
                        res.setSuccess(false);
                        res.setMessage("Invalid product ID: " + arg);
                    }
                    break;
                }
                case "SEARCH_PRODUCT": {
                    List<Product> matches = searchProducts(arg);
                    res.setSuccess(true);
                    res.setProductList(matches);
                    res.setMessage("Found " + matches.size() + " matching products.");
                    break;
                }
                case "LIST_ALL": {
                    List<Product> all = getAllProducts();
                    res.setSuccess(true);
                    res.setProductList(all);
                    res.setMessage("Retrieved " + all.size() + " inventory items.");
                    break;
                }
                default:
                    res.setSuccess(false);
                    res.setMessage("Unknown command: " + cmd + ". Valid: GET_PRODUCT, GET_STOCK, SEARCH_PRODUCT, LIST_ALL");
            }
            return res;
        }

        private Product findProductById(int id) {
            // First attempt MySQL
            try (Connection conn = getDatabaseConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT product_id, name, brand, price, stock FROM products WHERE product_id = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Product(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5));
                    }
                }
            } catch (Exception ignored) {}

            // Fallback to in-memory inventory
            return DEMO_INVENTORY.get(id);
        }

        private List<Product> searchProducts(String keyword) {
            List<Product> list = new ArrayList<>();
            try (Connection conn = getDatabaseConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT product_id, name, brand, price, stock FROM products WHERE name LIKE ? OR brand LIKE ?")) {
                ps.setString(1, "%" + keyword + "%");
                ps.setString(2, "%" + keyword + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(new Product(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5)));
                    }
                }
            } catch (Exception ignored) {}

            if (list.isEmpty()) {
                String kw = keyword.toLowerCase();
                for (Product p : DEMO_INVENTORY.values()) {
                    if (p.getName().toLowerCase().contains(kw) || p.getBrand().toLowerCase().contains(kw)) {
                        list.add(p);
                    }
                }
            }
            return list;
        }

        private List<Product> getAllProducts() {
            List<Product> list = new ArrayList<>();
            try (Connection conn = getDatabaseConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT product_id, name, brand, price, stock FROM products")) {
                while (rs.next()) {
                    list.add(new Product(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5)));
                }
            } catch (Exception ignored) {}

            if (list.isEmpty()) {
                list.addAll(DEMO_INVENTORY.values());
            }
            return list;
        }

        private Connection getDatabaseConnection() throws SQLException {
            String url = System.getenv("SHOPSPHERE_DB_URL");
            if (url == null || url.isEmpty()) url = "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String user = System.getenv("SHOPSPHERE_DB_USER");
            if (user == null || user.isEmpty()) user = "root";
            String pass = System.getenv("SHOPSPHERE_DB_PASS");
            if (pass == null) pass = "root";
            return DriverManager.getConnection(url, user, pass);
        }
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }
        new InventoryServer(port).start();
    }
}
