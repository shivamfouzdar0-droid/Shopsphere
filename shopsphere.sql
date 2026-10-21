-- ====================================================================
-- ShopSphere E-Commerce System - Database Schema & Seed Data
-- Target Database: MySQL 8.x / 9.x
-- RTU Advanced Java / Java Enterprise Project
-- ====================================================================

CREATE DATABASE IF NOT EXISTS shopsphere CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopsphere;

-- Disable foreign key checks for clean recreation
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS coupon_usage;
DROP TABLE IF EXISTS coupons;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- --------------------------------------------------------------------
-- 1. USERS TABLE
-- --------------------------------------------------------------------
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    mobile VARCHAR(20),
    role ENUM('CUSTOMER', 'ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    status TINYINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_email (email),
    INDEX idx_user_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
-- 2. ADDRESSES TABLE
-- --------------------------------------------------------------------
CREATE TABLE addresses (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_line VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(20) NOT NULL,
    address_type VARCHAR(50) DEFAULT 'HOME',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_address_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
-- 3. CATEGORIES TABLE
-- --------------------------------------------------------------------
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    status TINYINT NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
-- 4. PRODUCTS TABLE
-- --------------------------------------------------------------------
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    brand VARCHAR(100),
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(5, 2) DEFAULT 0.00,
    stock INT NOT NULL DEFAULT 0,
    image_url VARCHAR(500),
    status TINYINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT,
    INDEX idx_product_cat (category_id),
    INDEX idx_product_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
-- 5. CART TABLE
-- --------------------------------------------------------------------
CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE KEY uq_user_product (user_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
-- 6. WISHLIST TABLE
-- --------------------------------------------------------------------
CREATE TABLE wishlist (
    wishlist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE KEY uq_wishlist_user_prod (user_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
-- 7. ORDERS TABLE
-- --------------------------------------------------------------------
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_id INT,
    total_amount DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(10, 2) DEFAULT 0.00,
    payment_method VARCHAR(50) NOT NULL DEFAULT 'COD',
    payment_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    order_status VARCHAR(50) NOT NULL DEFAULT 'CONFIRMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES addresses(address_id) ON DELETE SET NULL,
    INDEX idx_order_user (user_id),
    INDEX idx_order_status (order_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
-- 8. ORDER ITEMS TABLE
-- --------------------------------------------------------------------
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT,
    INDEX idx_order_items (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
-- 9. PAYMENTS TABLE
-- --------------------------------------------------------------------
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_reference VARCHAR(100) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(50) NOT NULL DEFAULT 'SUCCESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
-- 10. REVIEWS TABLE
-- --------------------------------------------------------------------
CREATE TABLE reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
-- 11. COUPONS TABLE
-- --------------------------------------------------------------------
CREATE TABLE coupons (
    coupon_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    discount_type ENUM('PERCENTAGE', 'FIXED') NOT NULL DEFAULT 'PERCENTAGE',
    discount_value DECIMAL(10, 2) NOT NULL,
    minimum_order DECIMAL(10, 2) DEFAULT 0.00,
    maximum_discount DECIMAL(10, 2) DEFAULT 0.00,
    expiry_date DATE NOT NULL,
    status TINYINT NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
-- 12. COUPON USAGE TABLE
-- --------------------------------------------------------------------
CREATE TABLE coupon_usage (
    usage_id INT AUTO_INCREMENT PRIMARY KEY,
    coupon_id INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT NOT NULL,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ====================================================================
-- SEED DATA FOR DEMONSTRATION & EVALUATION
-- ====================================================================

-- 1. Users (Default Passwords hashed with SHA-256)
-- admin@shopsphere.com     -> admin123    (240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa82280f1a30e1ea6)
-- customer@shopsphere.com  -> customer123 (55a5e9e78207b4ae8970e7901fb5447ec2e76f4e17ef1bc248fb565268c22749)
INSERT INTO users (user_id, name, email, password, mobile, role, status) VALUES
(1, 'Administrator', 'admin@shopsphere.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa82280f1a30e1ea6', '9876543210', 'ADMIN', 1),
(2, 'Rahul Sharma', 'customer@shopsphere.com', '55a5e9e78207b4ae8970e7901fb5447ec2e76f4e17ef1bc248fb565268c22749', '9812345678', 'CUSTOMER', 1),
(3, 'Priya Patel', 'priya@shopsphere.com', '55a5e9e78207b4ae8970e7901fb5447ec2e76f4e17ef1bc248fb565268c22749', '9823456789', 'CUSTOMER', 1);

-- 2. Addresses
INSERT INTO addresses (address_id, user_id, address_line, city, state, pincode, address_type) VALUES
(1, 2, 'Flat 402, Royal Palms, M.G. Road', 'Jaipur', 'Rajasthan', '302001', 'HOME'),
(2, 2, 'Tech Park Phase 1, Sitapura', 'Jaipur', 'Rajasthan', '302022', 'WORK'),
(3, 3, '12B Silver Crest, Malviya Nagar', 'Jaipur', 'Rajasthan', '302017', 'HOME');

-- 3. Categories
INSERT INTO categories (category_id, name, description, status) VALUES
(1, 'Electronics', 'Smartphones, Laptops, Audio Gear, and Smart Wearables', 1),
(2, 'Fashion', 'Apparel, Footwear, and Designer Accessories for Men and Women', 1),
(3, 'Home & Kitchen', 'Modern Furniture, Cookware, and Smart Home Appliances', 1),
(4, 'Books & Stationery', 'Academic Textbooks, Engineering References, and Fiction', 1);

-- 4. Products
INSERT INTO products (product_id, category_id, name, brand, description, price, discount, stock, image_url, status) VALUES
(1, 1, 'ProSound Wireless Noise-Cancelling Headphones', 'AuraSound', 'Premium 40mm drivers with active hybrid ANC and 45-hour battery life.', 4999.00, 10.00, 25, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80', 1),
(2, 1, 'UltraBook Pro 15.6" Retina Display', 'NovaTech', 'Intel Core i7 13th Gen, 16GB LPDDR5, 512GB NVMe SSD, backlit keyboard.', 62999.00, 5.00, 12, 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600&auto=format&fit=crop&q=80', 1),
(3, 1, 'SmartWatch Chrono X Series', 'Pulse', 'AMOLED display, continuous SpO2, heart rate tracking and Bluetooth calling.', 2999.00, 15.00, 40, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80', 1),
(4, 1, 'Compact 4K Action Camera 60FPS', 'VeloCam', 'Waterproof up to 30m with dual color screens and ultra EIS stabilization.', 7499.00, 8.00, 18, 'https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600&auto=format&fit=crop&q=80', 1),
(5, 2, 'Classic Tailored Oxford Cotton Shirt', 'UrbanThread', '100% combed breathable cotton with modern slim fit cut and collar stays.', 1499.00, 20.00, 50, 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&auto=format&fit=crop&q=80', 1),
(6, 2, 'AirMotion Lightweight Running Shoes', 'Stride', 'Responsive foam cushioning sole with engineered breathable mesh upper.', 2499.00, 12.00, 35, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop&q=80', 1),
(7, 3, 'Smart Aroma Diffuser & Humidifier', 'PureLiving', 'Ultrasonic quiet misting with RGB ambient lighting and auto shut-off.', 1299.00, 15.00, 30, 'https://images.unsplash.com/photo-1608248597359-5f7fcb19335c?w=600&auto=format&fit=crop&q=80', 1),
(8, 3, 'Precision Pour-Over Coffee Maker Set', 'BaristaCraft', 'Borosilicate glass server with reusable stainless steel double-layer filter.', 1899.00, 10.00, 22, 'https://images.unsplash.com/photo-1517256064527-09c73fc73e38?w=600&auto=format&fit=crop&q=80', 1),
(9, 4, 'Core Java: Advanced Features (Vol 2)', 'PrenticeHall', 'Comprehensive guide to enterprise Java, networking, RMI, concurrency and JDBC.', 899.00, 5.00, 60, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80', 1),
(10, 4, 'Handcrafted Leather Bound Journal', 'HeritageArt', '200 pages handmade virgin paper with antique brass lock and satin bookmark.', 649.00, 10.00, 45, 'https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=600&auto=format&fit=crop&q=80', 1);

-- 5. Coupons
INSERT INTO coupons (coupon_id, code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) VALUES
(1, 'WELCOME10', 'PERCENTAGE', 10.00, 1000.00, 500.00, '2027-12-31', 1),
(2, 'FLAT500', 'FIXED', 500.00, 5000.00, 500.00, '2027-12-31', 1),
(3, 'FESTIVE20', 'PERCENTAGE', 20.00, 2000.00, 1000.00, '2027-12-31', 1);

-- 6. Sample Cart for Rahul Sharma
INSERT INTO cart (cart_id, user_id, product_id, quantity) VALUES
(1, 2, 1, 1),
(2, 2, 6, 1);

-- 7. Sample Wishlist for Rahul Sharma
INSERT INTO wishlist (wishlist_id, user_id, product_id) VALUES
(1, 2, 3),
(2, 2, 8);

-- 8. Sample Order for Rahul Sharma
INSERT INTO orders (order_id, user_id, address_id, total_amount, discount, payment_method, payment_status, order_status, created_at) VALUES
(1001, 2, 1, 6299.00, 699.00, 'ONLINE', 'PAID', 'DELIVERED', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(1002, 2, 1, 2499.00, 0.00, 'COD', 'PENDING', 'SHIPPED', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 9. Sample Order Items
INSERT INTO order_items (order_item_id, order_id, product_id, quantity, price) VALUES
(1, 1001, 1, 1, 4499.10),
(2, 1001, 7, 1, 1104.15),
(3, 1001, 10, 1, 584.10),
(4, 1002, 6, 1, 2199.12);

-- 10. Sample Payments
INSERT INTO payments (payment_id, order_id, payment_method, transaction_reference, amount, payment_status) VALUES
(1, 1001, 'ONLINE', 'TXN_SHOPSPHERE_9821341', 6299.00, 'SUCCESS');

-- 11. Sample Reviews
INSERT INTO reviews (review_id, user_id, product_id, rating, review_text) VALUES
(1, 2, 1, 5, 'Exceptional audio clarity and deep bass response. Very comfortable for long study sessions!'),
(2, 3, 6, 4, 'Very comfortable running shoes, lightweight and great grip.');

-- 12. Sample Coupon Usage
INSERT INTO coupon_usage (usage_id, coupon_id, user_id, order_id) VALUES
(1, 1, 2, 1001);
