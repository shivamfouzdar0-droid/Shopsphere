package com.shopsphere.rmi.client;

import com.shopsphere.rmi.remote.InventoryService;
import com.shopsphere.rmi.remote.Product;
import com.shopsphere.rmi.server.RmiServer;

import java.rmi.Naming;
import java.util.List;

/**
 * RTU Syllabus: Java RMI Remote Client.
 * Performs lookup on RMI Registry and executes distributed method calls.
 */
public class RmiClient {

    public static void main(String[] args) {
        String rmiUrl = "rmi://localhost:" + RmiServer.RMI_PORT + "/" + RmiServer.BIND_NAME;

        System.out.println("==================================================");
        System.out.println("   SHOPSPHERE JAVA RMI REMOTE CLIENT DEMO         ");
        System.out.println("==================================================");
        System.out.println("Connecting to: " + rmiUrl);

        try {
            InventoryService service = (InventoryService) Naming.lookup(rmiUrl);
            System.out.println(">>> Remote Reference obtained successfully!\n");

            // 1. Remote call getProduct(1)
            System.out.println("[RMI Call 1] getProduct(1):");
            Product p1 = service.getProduct(1);
            System.out.println("  Returned Remote Object: " + p1);

            // 2. Remote call getStock(2)
            System.out.println("\n[RMI Call 2] getStock(2):");
            int stock = service.getStock(2);
            System.out.println("  Returned Stock Count: " + stock + " units");

            // 3. Remote call updateStock(1, -2)
            System.out.println("\n[RMI Call 3] updateStock(1, -2):");
            boolean updated = service.updateStock(1, -2);
            System.out.println("  Stock update status: " + updated);
            System.out.println("  New stock after deduction: " + service.getStock(1));

            // 4. Remote call getAllProducts()
            System.out.println("\n[RMI Call 4] getAllProducts():");
            List<Product> list = service.getAllProducts();
            for (Product p : list) {
                System.out.println("  -> " + p);
            }

            System.out.println("\n==================================================");
            System.out.println("   RMI REMOTE INVOCATION COMPLETED WITH SUCCESS   ");
            System.out.println("==================================================");

        } catch (Exception e) {
            System.err.println("RMI Client lookup or invocation failed: " + e.getMessage());
            System.err.println("Ensure RmiServer is running on localhost:" + RmiServer.RMI_PORT);
        }
    }
}
