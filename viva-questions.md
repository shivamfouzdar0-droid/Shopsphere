# ShopSphere — RTU Advanced Java Viva Preparation

## 1. Servlet Lifecycle & Web Container Architecture
**Q1: Explain the lifecycle of a Jakarta Servlet.**
- **Loading & Instantiation:** The Web Container (Apache Tomcat 11) loads the servlet class bytecode using the classloader and creates an instance.
- **Initialization:** Container calls `init(ServletConfig config)`. Executed only once during the servlet's lifespan to load parameters.
- **Request Servicing:** For each client HTTP request, the container invokes `service(ServletRequest, ServletResponse)`, which dispatches to `doGet()`, `doPost()`, `doPut()`, or `doDelete()`.
- **Destruction:** Before unloading the servlet, the container invokes `destroy()` to release active system resources, database connections, and background threads.

**Q2: What is the difference between `ServletConfig` and `ServletContext`?**
- `ServletConfig` is specific to one individual servlet instance (configured via `<init-param>` or `@WebInitParam`).
- `ServletContext` is shared application-wide across all servlets, filters, and JSPs in the web module (configured via `<context-param>`).

---

## 2. Filters & Listeners
**Q3: How do Filters work in ShopSphere?**
- Filters implement `jakarta.servlet.Filter` and intercept HTTP requests before they reach target servlets, and responses before returning to clients.
- In ShopSphere, `AuthenticationFilter` intercepts protected paths (`/cart`, `/checkout`, `/orders`) to verify user session presence, while `AdminFilter` guards `/admin/*` ensuring the user holds the `ADMIN` role.

**Q4: What is the purpose of `AppListener`?**
- `AppListener` implements `ServletContextListener` and `HttpSessionListener`.
- On application startup (`contextInitialized`), it pre-loads active categories into the application scope.
- On session creation/destruction (`sessionCreated`/`sessionDestroyed`), it maintains an atomic count of currently active online shoppers.

---

## 3. JSP, Expression Language (EL), & JSTL
**Q5: Why are Scriptlets (`<% %>`) avoided in modern MVC web development?**
- Scriptlets mix Java business logic with HTML view presentation, violating separation of concerns and making maintenance error-prone.
- ShopSphere uses **Expression Language (`${...}`)** and **JSTL Core (`<c:forEach>`, `<c:if>`)** to render data prepared by Servlet controllers in request/session scopes.

**Q6: What are JSP Custom Tag Files?**
- Tag files (`.tag`) allow developers to create modular, reusable UI components written in JSP syntax without writing complex Java TagHandler classes.
- ShopSphere implements `productCard.tag` (rendering product cards with discounts and carts) and `rating.tag` (rendering star rating icons).

---

## 4. JDBC & Transactional Integrity
**Q7: Why are `PreparedStatement`s mandatory instead of `Statement`?**
- **Security:** `PreparedStatement` uses parameterized inputs, preventing SQL Injection attacks by pre-compiling query structures.
- **Performance:** Pre-compiled SQL statements are cached by the database engine for faster subsequent executions.

**Q8: How does ShopSphere achieve transactional checkout?**
- In `OrderService.java`, the JDBC connection is set to `conn.setAutoCommit(false)`.
- Stock is validated, the order and order items are created, inventory is deducted, simulated payment is logged, coupon usage is recorded, and the cart is cleared.
- If all steps succeed, `conn.commit()` is called. If an error occurs, `conn.rollback()` restores all records to their original state.

---

## 5. Distributed Computing: Sockets vs Java RMI
**Q9: What is the architectural difference between `java.net.Socket` and Java RMI?**
- **Sockets (Low-Level):** TCP stream-based communication where the client and server exchange raw bytes or serialized objects over an agreed communication protocol.
- **Java RMI (High-Level Object-Oriented):** Remote Method Invocation allows a JVM to invoke methods on an object living in another JVM as if it were a local object, using an interface extending `Remote` and an RMI Registry for naming resolution.

**Q10: What is the role of `UnicastRemoteObject` and `RemoteException`?**
- `UnicastRemoteObject` provides support for point-to-point remote references and exports the remote object to listen for incoming client calls on an anonymous TCP port.
- Every method in a remote interface must declare `throws RemoteException` to handle distributed network failures (e.g., packet loss, server downtime).

---

## 6. JNDI & Internationalization
**Q11: What is JNDI and why is it used?**
- Java Naming and Directory Interface (JNDI) decouples application code from external resources (like database connection pools and message queues).
- Instead of hardcoding database hostnames, ports, and credentials in Java source code, the application retrieves resources by lookup names such as `java:comp/env/jdbc/ShopSphereDS`.

**Q12: How is Internationalization (i18n) implemented?**
- ShopSphere utilizes `java.util.ResourceBundle` with `messages_en.properties` (English) and `messages_hi.properties` (Hindi).
- Users can switch their preferred language dynamically via `LanguageServlet`, which sets `userLocale` in the `HttpSession`.
