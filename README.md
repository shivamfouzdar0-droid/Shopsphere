# ShopSphere — RTU Advanced Java E-Commerce Management System

**ShopSphere** is an enterprise-grade Java E-Commerce Management System developed for the Rajasthan Technical University (RTU) Advanced Java curriculum. It combines a modern, responsive web store deployed on **Apache Tomcat 11** with a desktop **Java Swing** administration application and distributed **Java Sockets & RMI** inventory engines.

---

## 1. Project Modules & Structure

```
ShopSphere/
├── pom.xml                               # Root Maven reactor build
├── README.md                             # Master project guide
├── PROGRESS.md                           # Progress and completion checklist
├── .gitignore                            # Git exclusions
├── database/
│   ├── shopsphere.sql                    # Complete MySQL schema & seed data
│   └── README.md                         # Database setup guide
├── docs/
│   ├── system-architecture.md            # Architecture & sequence diagrams (Mermaid)
│   ├── database-design.md                # ER diagram & data dictionary (Mermaid)
│   ├── rtu-syllabus-mapping.md           # Syllabus-to-code mapping matrix
│   ├── test-cases.md                     # Test scenarios and execution results
│   ├── viva-questions.md                 # 25+ Viva preparation Q&A
│   └── project-report.md                 # Academic college project report
├── web/                                  # Jakarta EE 10 / Servlet 6.1 Web Application (WAR)
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/shopsphere/          # Controllers, Services, DAOs, Models, Filters, Listeners
│       ├── resources/                    # db.properties, messages_en/hi.properties
│       └── webapp/                       # JSPs, fragments, admin portal, CSS, JS
├── swing-admin/                          # Java Swing Desktop Management Application (JAR)
│   ├── pom.xml
│   └── src/main/java/com/shopsphere/admin/
├── network/                              # java.net TCP Socket Server & Client (JAR)
│   ├── pom.xml
│   └── src/main/java/com/shopsphere/network/
└── rmi/                                  # Java RMI Distributed Remote Inventory Module (JAR)
    ├── pom.xml
    └── src/main/java/com/shopsphere/rmi/
```

---

## 2. Technology Stack

- **Language:** Java 17+ (Tested on JDK 25)
- **Build System:** Apache Maven 3.9+
- **Web Container:** Apache Tomcat 11.x
- **Web Framework:** Jakarta Servlet API 6.1.0, JSP 3.1, JSTL 3.0, Expression Language (EL)
- **Database:** MySQL 8.x / 9.x (InnoDB)
- **Data Access:** JDBC with MySQL Connector/J 8.3.0
- **Security:** SHA-256 cryptographic password hashing, PreparedStatement SQL injection protection
- **Desktop GUI:** Java Swing (JFrame, JTable, JTabbedPane) with Pluggable Look and Feel
- **Distributed Computing:** `java.net.ServerSocket`, `java.net.Socket`, Object Streams, Java RMI

---

## 3. Database Setup (MySQL)

### Step 1: Create Database and Import Schema
Run the provided SQL script using the MySQL Command Line Client or MySQL Workbench:

```bash
mysql -u root -p < database/shopsphere.sql
```

### Step 2: Configure Database Credentials
Edit `web/src/main/resources/db.properties` (or set environment variables `SHOPSPHERE_DB_USER` and `SHOPSPHERE_DB_PASS`):
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=root
```

---

## 4. Pre-seeded Demo Accounts

| Role | Name | Email | Password |
|---|---|---|---|
| **ADMIN** | Administrator | `admin@shopsphere.com` | `admin123` |
| **CUSTOMER** | Rahul Sharma | `customer@shopsphere.com` | `customer123` |
| **CUSTOMER** | Priya Patel | `priya@shopsphere.com` | `customer123` |

### Active Discount Coupons
- `WELCOME10`: 10% off on orders above ₹1,000 (Max discount ₹500)
- `FLAT500`: Flat ₹500 off on orders above ₹5,000
- `FESTIVE20`: 20% off on orders above ₹2,000 (Max discount ₹1,000)

---

## 5. Build & Deployment Instructions

### 1. Build the Entire Multi-Module Project
From the `ShopSphere/` root directory:
```bash
mvn clean package -DskipTests
```
This builds all four modules:
- `web/target/shopsphere.war` (Web Archive)
- `swing-admin/target/shopsphere-swing-admin-1.0.0.jar`
- `network/target/shopsphere-network-1.0.0.jar`
- `rmi/target/shopsphere-rmi-1.0.0.jar`

### 2. Deploy to Apache Tomcat 11
1. Copy `web/target/shopsphere.war` to `<TOMCAT_HOME>/webapps/`.
2. Start Tomcat using `<TOMCAT_HOME>/bin/startup.bat` (Windows) or `startup.sh` (Linux).
3. Access the web store in your browser:
   - **Customer Storefront:** `http://localhost:8080/shopsphere/`
   - **Admin Web Dashboard:** `http://localhost:8080/shopsphere/admin/dashboard`
   - **RTU Syllabus Demonstration:** `http://localhost:8080/shopsphere/jsp-demo.jsp`

---

## 6. Running Advanced Java Modules

### A. Java Swing Desktop Administration
Run the desktop administrative GUI:
```bash
java -jar swing-admin/target/shopsphere-swing-admin-1.0.0.jar
```
*Or via Maven:*
```bash
cd swing-admin
mvn compile exec:java -Dexec.mainClass="com.shopsphere.admin.view.LoginFrame"
```

### B. Java Networking Socket Client-Server Module
1. **Start the TCP Inventory Server (port 8089):**
   ```bash
   cd network
   java -cp target/classes com.shopsphere.network.server.InventoryServer
   ```
2. **In a second terminal, execute the Socket Client:**
   ```bash
   cd network
   java -cp target/classes com.shopsphere.network.client.InventoryClient
   ```

### C. Java RMI Distributed Remote Inventory Module
1. **Start the RMI Server & Registry (port 1099):**
   ```bash
   cd rmi
   java -cp target/classes com.shopsphere.rmi.server.RmiServer
   ```
2. **In a second terminal, run the RMI Remote Client:**
   ```bash
   cd rmi
   java -cp target/classes com.shopsphere.rmi.client.RmiClient
   ```

### D. JNDI & URLConnection Demonstrations
```bash
cd network
java -cp target/classes com.shopsphere.network.demo.JndiDemo
java -cp target/classes com.shopsphere.network.demo.UrlConnectionDemo
```

---

## 7. RTU Laboratory & Viva Defense
All viva preparation questions, architecture diagrams, and syllabus mappings are available in the `docs/` folder:
- [System Architecture](docs/system-architecture.md)
- [Database ER Design](docs/database-design.md)
- [RTU Syllabus to Code Matrix](docs/rtu-syllabus-mapping.md)
- [Test Case Results](docs/test-cases.md)
- [Viva Questions & Answers](docs/viva-questions.md)
- [College Project Report](docs/project-report.md)
