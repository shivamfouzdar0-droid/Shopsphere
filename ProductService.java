package com.shopsphere.service;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;

import java.util.List;

public class ProductService {

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
    }

    public ProductService(ProductDAO productDAO, CategoryDAO categoryDAO) {
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
    }

    public Product getProduct(int id) {
        return productDAO.findById(id);
    }

    public List<Product> getProducts(String search, Integer categoryId, String sort, int page, int pageSize) {
        int offset = Math.max(0, (page - 1) * pageSize);
        return productDAO.getAll(search, categoryId, sort, offset, pageSize);
    }

    public int getProductCount(String search, Integer categoryId) {
        return productDAO.count(search, categoryId);
    }

    public List<Product> getFeaturedProducts(int limit) {
        return productDAO.getFeatured(limit);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAll();
    }

    public List<Category> getActiveCategories() {
        return categoryDAO.getActive();
    }

    public List<Product> getAllProductsForAdmin() {
        return productDAO.getAllForAdmin();
    }

    public List<Product> getLowStockProducts(int threshold) {
        return productDAO.getLowStockProducts(threshold);
    }

    public boolean createProduct(Product product) {
        if (product.getPrice() < 0 || product.getStock() < 0) {
            return false;
        }
        return productDAO.create(product);
    }

    public boolean updateProduct(Product product) {
        if (product.getPrice() < 0 || product.getStock() < 0) {
            return false;
        }
        return productDAO.update(product);
    }

    public boolean deleteProduct(int productId) {
        return productDAO.delete(productId);
    }
}
