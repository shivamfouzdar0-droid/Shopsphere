package com.shopsphere.rmi.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * RTU Syllabus: Java Remote Method Invocation (RMI) Remote Interface.
 * Declares distributed methods accessible across network JVMs.
 */
public interface InventoryService extends Remote {

    Product getProduct(int id) throws RemoteException;

    int getStock(int productId) throws RemoteException;

    boolean updateStock(int productId, int deltaQuantity) throws RemoteException;

    List<Product> getAllProducts() throws RemoteException;
}
