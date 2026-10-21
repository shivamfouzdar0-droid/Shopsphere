package com.shopsphere.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int cartId;
    private int userId;
    private int productId;
    private int quantity;
    private Product product;

    public CartItem() {
    }

    public CartItem(int cartId, int userId, int productId, int quantity, Product product) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.product = product;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getSubtotal() {
        if (product != null) {
            return Math.round(product.getDiscountedPrice() * quantity * 100.0) / 100.0;
        }
        return 0.0;
    }
}
