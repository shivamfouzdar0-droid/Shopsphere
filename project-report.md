# ShopSphere — RTU Advanced Java College Project Report

## 1. Introduction
ShopSphere is a full-featured enterprise E-Commerce Management System designed as a capstone practical demonstration of the Rajasthan Technical University (RTU) Advanced Java syllabus. The project connects modern web application engineering with distributed systems and desktop management.

---

## 2. Problem Statement
Many educational demonstration systems restrict students to trivial console scripts or basic CRUD pages that do not reflect real-world enterprise requirements. Students often lack exposure to how low-level networking (`java.net.Socket`), distributed interfaces (`java.rmi`), enterprise desktop systems (`Java Swing MVC`), and web containers (`Jakarta Servlet / Tomcat 11`) integrate within a unified business ecosystem.

---

## 3. Objectives
- Implement an N-Tier enterprise web architecture adhering to the Jakarta EE 10 specification.
- Provide a responsive storefront with product catalog browsing, cart management, discount coupons, and transactional checkout.
- Engineer a separate Java Swing administration desktop client for catalog and inventory management.
- Develop a multi-threaded TCP inventory server using `ServerSocket` and `Socket` demonstrating Java object serialization.
- Implement a distributed inventory service using Java Remote Method Invocation (RMI) and the RMI Registry.
- Enforce enterprise security standards using cryptographic password hashing (SHA-256) and parameterized SQL statements (`PreparedStatement`).

---

## 4. Technology Stack
- **Programming Language:** Java 17+ (Compiled on JDK 25)
- **Web Layer:** Jakarta Servlet API 6.1.0, JSP 3.1, JSTL 3.0, Expression Language (EL)
- **Web Container:** Apache Tomcat 11
- **Database & Storage:** MySQL 8.x / 9.x (InnoDB engine)
- **Data Access:** JDBC with MySQL Connector/J 8.3.0
- **Desktop Administration:** Java Swing with Pluggable Look and Feel
- **Networking:** `java.net.ServerSocket`, `java.net.Socket`, Object Streams
- **Distributed Computing:** Java RMI (`UnicastRemoteObject`, `Naming`, `LocateRegistry`)
- **Frontend Presentation:** HTML5, CSS3, JavaScript, Bootstrap 5.3.3, Bootstrap Icons
- **Build Automation:** Apache Maven 3.9.16 (Multi-Module reactor)

---

## 5. System Architecture
The application is structured into four primary modules under a parent Maven project:
1. **`shopsphere-web` (`.war`):** Customer shopping interface, admin web portal, filters, listeners, and servlets.
2. **`shopsphere-swing-admin` (`.jar`):** Standalone desktop GUI with `LoginFrame` and `AdminDashboard`.
3. **`shopsphere-network` (`.jar`):** Client-server TCP socket inventory engine with object serialization.
4. **`shopsphere-rmi` (`.jar`):** Remote method invocation interface and implementation bound to the RMI registry.

---

## 6. Database Design
The schema `shopsphere` comprises 12 normalized relational tables:
`users`, `addresses`, `categories`, `products`, `cart`, `wishlist`, `orders`, `order_items`, `payments`, `reviews`, `coupons`, and `coupon_usage`. Referential integrity is enforced with foreign keys, cascade deletes, and unique constraints.

---

## 7. Implementation Highlights
- **Layered Decoupling:** Servlets handle request dispatching, delegate business calculations to the Service layer (`OrderService`, `ProductService`, `CartService`), which query DAOs using pooled `DBConnection`s.
- **Transactional Checkout:** In `OrderService.placeOrder()`, database auto-commit is disabled. Order creation, item inserts, stock deductions, mock payment logging, coupon redemption, and cart clearing are executed atomically.
- **Role-Based Authorization:** `AuthenticationFilter` and `AdminFilter` inspect session tokens to guard restricted routes.
- **ResourceBundles:** Multi-lingual support for English and Hindi interfaces using `messages_*.properties`.

---

## 8. Verification and Testing
All 21 comprehensive test cases passed successfully, including multi-module Maven compilation, WAR packaging, customer shopping workflows, administrator operations, live TCP Socket communication, and RMI distributed invocations.

---

## 9. Conclusion
ShopSphere bridges theoretical academic concepts with commercial-grade software engineering. It provides an exemplary project demonstration for university laboratory evaluations, viva examinations, and enterprise Java development practice.
