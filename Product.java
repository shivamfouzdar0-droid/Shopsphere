package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private int productId;
    private int categoryId;
    private String categoryName;
    private String name;
    private String brand;
    private String description;
    private double price;
    private double discount; // Percentage discount, e.g., 10.0
    private int stock;
    private String imageUrl;
    private boolean status;
    private Timestamp createdAt;

    public Product() {
    }

    public Product(int productId, int categoryId, String name, String brand, String description,
                   double price, double discount, int stock, String imageUrl, boolean status, Timestamp createdAt) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return (imageUrl != null && !imageUrl.trim().isEmpty()) 
                ? imageUrl 
                : "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80";
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public double getDiscountedPrice() {
        if (discount > 0 && discount <= 100) {
            double finalPrice = price - (price * (discount / 100.0));
            return Math.round(finalPrice * 100.0) / 100.0;
        }
        return price;
    }

    public boolean isAvailable() {
        return status && stock > 0;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
