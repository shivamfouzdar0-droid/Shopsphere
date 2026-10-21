package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;

import java.util.List;

public class CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
    }

    public CartService(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public List<CartItem> getCart(int userId) {
        return cartDAO.getCartByUser(userId);
    }

    public boolean addToCart(int userId, int productId, int quantity) {
        Product p = productDAO.findById(productId);
        if (p == null || !p.isAvailable() || p.getStock() < quantity) {
            return false;
        }
        return cartDAO.addToCart(userId, productId, quantity);
    }

    public boolean updateQuantity(int userId, int productId, int quantity) {
        if (quantity > 0) {
            Product p = productDAO.findById(productId);
            if (p == null || p.getStock() < quantity) {
                return false;
            }
        }
        return cartDAO.updateQuantity(userId, productId, quantity);
    }

    public boolean removeFromCart(int userId, int productId) {
        return cartDAO.removeFromCart(userId, productId);
    }

    public int getCartCount(int userId) {
        return cartDAO.getCartCount(userId);
    }

    public double getCartSubtotal(int userId) {
        List<CartItem> items = getCart(userId);
        double total = 0.0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return Math.round(total * 100.0) / 100.0;
    }
}
