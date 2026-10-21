package com.shopsphere.rmi.server;

import com.shopsphere.rmi.remote.InventoryService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Logger;

/**
 * Bootstraps the RMI Registry and binds the remote InventoryService.
 */
public class RmiServer {

    private static final Logger LOGGER = Logger.getLogger(RmiServer.class.getName());
    public static final int RMI_PORT = 1099;
    public static final String BIND_NAME = "InventoryService";

    public static void main(String[] args) {
        try {
            // Create RMI Registry programmatically on port 1099
            try {
                LocateRegistry.createRegistry(RMI_PORT);
                LOGGER.info("RMI Registry created on port " + RMI_PORT);
            } catch (Exception e) {
                LOGGER.info("RMI Registry already active on port " + RMI_PORT);
            }

            InventoryService service = new InventoryServiceImpl();
            String rmiUrl = "rmi://localhost:" + RMI_PORT + "/" + BIND_NAME;
            Naming.rebind(rmiUrl, service);

            System.out.println("==================================================");
            System.out.println("   SHOPSPHERE RMI REMOTE SERVER RUNNING           ");
            System.out.println("   Bound URL: " + rmiUrl);
            System.out.println("==================================================");

        } catch (Exception e) {
            LOGGER.severe("Failed to start RMI Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
