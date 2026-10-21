package com.shopsphere.network.client;

import com.shopsphere.network.model.NetworkRequest;
import com.shopsphere.network.model.NetworkResponse;
import com.shopsphere.network.model.Product;
import com.shopsphere.network.server.InventoryServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Demonstrates RTU syllabus: java.net.Socket client interacting with ServerSocket.
 * Sends serialized requests, deserializes responses over TCP streams.
 */
public class InventoryClient {

    private final String host;
    private final int port;

    public InventoryClient() {
        this("localhost", InventoryServer.DEFAULT_PORT);
    }

    public InventoryClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public NetworkResponse sendRequest(String command, String argument) throws Exception {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            NetworkRequest req = new NetworkRequest(command, argument);
            out.writeObject(req);
            out.flush();

            Object res = in.readObject();
            if (res instanceof NetworkResponse) {
                return (NetworkResponse) res;
            }
            throw new IllegalStateException("Unexpected response type from server: " + res);
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   SHOPSPHERE JAVA.NET TCP SOCKET CLIENT DEMO     ");
        System.out.println("==================================================");

        InventoryClient client = new InventoryClient();

        try {
            System.out.println("\n[Test 1] Querying Product #1 details (GET_PRODUCT):");
            NetworkResponse r1 = client.sendRequest("GET_PRODUCT", "1");
            System.out.println("Server Response: " + r1.getMessage());
            if (r1.getProduct() != null) {
                System.out.println("Received Object: " + r1.getProduct());
            }

            System.out.println("\n[Test 2] Querying Stock Level for Product #2 (GET_STOCK):");
            NetworkResponse r2 = client.sendRequest("GET_STOCK", "2");
            System.out.println("Server Response: " + r2.getMessage());

            System.out.println("\n[Test 3] Searching Catalog by Keyword (SEARCH_PRODUCT 'Pro'):");
            NetworkResponse r3 = client.sendRequest("SEARCH_PRODUCT", "Pro");
            System.out.println("Server Response: " + r3.getMessage());
            if (r3.getProductList() != null) {
                for (Product p : r3.getProductList()) {
                    System.out.println(" -> " + p);
                }
            }

            System.out.println("\n==================================================");
            System.out.println("   SOCKET CLIENT-SERVER TEST FINISHED SUCCESSFULLY");
            System.out.println("==================================================");

        } catch (Exception e) {
            System.err.println("Could not connect to InventoryServer on port " + InventoryServer.DEFAULT_PORT + ": " + e.getMessage());
            System.err.println("Ensure InventoryServer is running before starting InventoryClient.");
        }
    }
}
