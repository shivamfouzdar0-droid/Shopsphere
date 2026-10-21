package com.shopsphere.network.model;

import java.io.Serializable;
import java.util.List;

public class NetworkResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private Product product;
    private List<Product> productList;
    private Integer stockLevel;

    public NetworkResponse() {}

    public NetworkResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public List<Product> getProductList() { return productList; }
    public void setProductList(List<Product> productList) { this.productList = productList; }

    public Integer getStockLevel() { return stockLevel; }
    public void setStockLevel(Integer stockLevel) { this.stockLevel = stockLevel; }

    @Override
    public String toString() {
        return "NetworkResponse[success=" + success + ", message='" + message + "', product=" + product + ", stock=" + stockLevel + "]";
    }
}
