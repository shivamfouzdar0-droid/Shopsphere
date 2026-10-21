# RTU Advanced Java Syllabus to Project Mapping

This matrix maps every topic in the Rajasthan Technical University (RTU) Advanced Java / Java Enterprise syllabus to its exact implementation in the **ShopSphere** project.

| RTU Syllabus Topic | Implementation in ShopSphere | File / Class Location |
|---|---|---|
| **Swing & Swing Components** | Complete desktop administration GUI using JFrame, JPanel, JLabel, JTextField, JPasswordField, JButton, JCheckBox, JComboBox, JTable, JScrollPane, JOptionPane | `swing-admin/.../view/LoginFrame.java`, `swing-admin/.../view/AdminDashboard.java` |
| **Pluggable Look & Feel** | UIManager LookAndFeel configuration | `LoginFrame.java` (lines 30-34) |
| **Swing MVC Architecture** | Model (Product/Order/User), View (Dashboard/LoginFrame), Controller/DAO (AdminDAO) | `swing-admin/.../model/`, `view/`, `dao/` |
| **JDBC / java.sql** | Connection, PreparedStatement, Statement, ResultSet, Statement.RETURN_GENERATED_KEYS, commit/rollback | `web/.../util/DBConnection.java`, `web/.../dao/*` |
| **JDBC Driver** | MySQL Connector/J 8.3.0 | `pom.xml`, `db.properties` |
| **java.net Networking** | Client-Server architecture using ServerSocket & Socket, multi-threaded request processing | `network/.../server/InventoryServer.java`, `network/.../client/InventoryClient.java` |
| **Serialization** | Transferring Product & NetworkRequest objects via ObjectOutputStream / ObjectInputStream | `network/.../model/Product.java`, `network/.../model/NetworkRequest.java` |
| **Java RMI Architecture** | Remote Interface, UnicastRemoteObject, RemoteException, distributed invocations | `rmi/.../remote/InventoryService.java`, `rmi/.../server/InventoryServiceImpl.java` |
| **RMI Registry & Naming** | Programmatic LocateRegistry.createRegistry(1099), Naming.rebind(), Naming.lookup() | `rmi/.../server/RmiServer.java`, `rmi/.../client/RmiClient.java` |
| **JNDI Resource Lookup** | Context, InitialContext, Environment properties, DataSource lookup concept | `network/.../demo/JndiDemo.java` |
| **Content & Protocol Handlers** | URL, URLConnection, stream parsing, HTTP header analysis | `network/.../demo/UrlConnectionDemo.java` |
| **Internationalization (i18n)** | ResourceBundle, Locale, English (`messages_en.properties`), Hindi (`messages_hi.properties`), dynamic switcher | `web/.../resources/messages_*.properties`, `web/.../controller/LanguageServlet.java` |
| **J2EE / Enterprise N-Tier** | Presentation -> Controller -> Service -> DAO -> Database | Layered package architecture across `web` module |
| **Web Container & HTTP** | Apache Tomcat 11, HTTP GET/POST handling, request/response lifecycle | `web/src/main/webapp/WEB-INF/web.xml`, Tomcat 11 WAR |
| **Jakarta Servlet 6.1** | doGet(), doPost(), init(), destroy(), ServletConfig, ServletContext | `web/.../controller/*Servlet.java` |
| **Session Management** | HttpSession (authentication, cart count, flash toasts, session timeout 30 min) | `LoginServlet.java`, `LogoutServlet.java`, `CartServlet.java` |
| **HTTP Cookies** | Tracking recently viewed items in 7-day persistent browser cookies | `ProductDetailsServlet.java` (lines 53-85) |
| **Event Handling & Listeners** | ServletContextListener, HttpSessionListener tracking active sessions & app attributes | `web/.../listener/AppListener.java` |
| **Servlet Filters** | Intercepting requests: AuthenticationFilter (protected pages), AdminFilter (/admin/*), CharacterEncodingFilter | `web/.../filter/AuthenticationFilter.java`, `AdminFilter.java`, `CharacterEncodingFilter.java` |
| **JSP Architecture & Elements** | Declarations (`<%! %>`), Scriptlets (`<% %>`), Expressions (`<%= %>`), Comments (`<%-- --%>`) | `web/src/main/webapp/jsp-demo.jsp` |
| **Expression Language (EL)** | Scoped access: `${product.name}`, `${sessionScope.user.name}`, `${applicationScope.globalCategories}` | Used throughout all `.jsp` files |
| **JSP Fragments** | Modular reusable components: header, navbar, footer, sidebar | `web/src/main/webapp/fragments/*.jsp` |
| **Custom Tag Files** | Tag files `<tag:productCard>`, `<tag:rating>` | `web/src/main/webapp/WEB-INF/tags/*.tag` |
| **JSTL Core** | `<c:forEach>`, `<c:if>`, `<c:choose>`, `<c:when>`, `<c:set>`, `<c:remove>` | Used across storefront and admin views |
| **JSTL Functions** | `fn:length()`, `fn:toUpperCase()`, `fn:split()` | `jsp-demo.jsp` |
| **JSTL XML & SQL** | XML parsing and feed exchange demo; explanation of DAO vs JSTL SQL | `jsp-demo.jsp` (Sections 3 & 4) |
| **Applets Historical Status** | Comprehensive documentation of Applet deprecation/removal and modern HTML5 replacement | `jsp-demo.jsp`, `docs/rtu-syllabus-mapping.md` |
