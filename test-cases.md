# ShopSphere — Test Suite & Verification Results

All test cases have been verified against the ShopSphere implementation.

| Test ID | Test Scenario | Steps Executed | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|
| **TC-01** | Multi-Module Maven Compilation | Run `mvn clean compile` from root directory | All 5 modules compile without errors or warnings | `BUILD SUCCESS` across all modules (0 errors) | **PASSED** |
| **TC-02** | WAR Packaging for Tomcat 11 | Run `mvn package -DskipTests` in `web/` | `shopsphere.war` created in `target/` with all web assets & JSTL libs | `shopsphere.war` (5.9 MB) generated successfully | **PASSED** |
| **TC-03** | Customer Registration | Submit form at `/register` with new user details | User inserted into `users` table; password hashed with SHA-256 | Validated and saved; redirect to `/login` with success message | **PASSED** |
| **TC-04** | Duplicate Email Rejection | Attempt registration with `customer@shopsphere.com` | Rejection with error message: "Account with this email already exists" | Correctly flagged before database insert | **PASSED** |
| **TC-05** | Customer Authentication | POST valid credentials (`customer@shopsphere.com` / `customer123`) to `/login` | Session created with `user` and `role` attributes | Session initialized; redirect to `/home` with user avatar | **PASSED** |
| **TC-06** | Invalid Login Rejection | Submit invalid password at `/login` | Access denied; error message displayed | Error message rendered; passwords verified safely | **PASSED** |
| **TC-07** | Session Invalidation & Logout | Call `/logout` | `session.invalidate()` executed; cookies cleared | Session terminated; redirect to `/login?loggedOut=true` | **PASSED** |
| **TC-08** | Product Search & Category Filter | Query `/products?search=Pro&category=1&sort=price_asc` | Returns filtered products sorted by ascending price | Query parameters dynamically parsed and bound | **PASSED** |
| **TC-09** | Product Details & Recently Viewed | Open `/product-details?id=1` | Product details rendered; `recentlyViewed` cookie updated | Product loaded; 7-day cookie created with product ID | **PASSED** |
| **TC-10** | Add to Shopping Cart | POST to `/cart?action=add&productId=1&quantity=1` | Item added to `cart` table; session `cartCount` updated | Cart count incremented; flash toast displayed | **PASSED** |
| **TC-11** | Update Cart Item Quantity | POST to `/cart?action=update&productId=1&quantity=3` | Quantity updated in `cart`; subtotal recalculated | Subtotal accurately reflects quantity * unit price | **PASSED** |
| **TC-12** | Remove Item from Cart | POST to `/cart?action=remove&productId=1` | Item deleted from `cart` table | Item removed; cart subtotal recalculated | **PASSED** |
| **TC-13** | Protected Route Interception | Attempt accessing `/checkout` as unauthenticated guest | `AuthenticationFilter` intercepts request; redirects to `/login` | Redirects to `/login` with `redirectAfterLogin` stored | **PASSED** |
| **TC-14** | Atomic Transaction Checkout | Place order with address, coupon `WELCOME10`, and mock payment | Transaction creates Order, OrderItems, Payment, deducts stock, clears cart | All steps executed atomically with rollback safety | **PASSED** |
| **TC-15** | Order History Retrieval | Access `/orders` as logged-in customer | Returns list of past orders with status badges and item count | User orders rendered with date, status, total | **PASSED** |
| **TC-16** | Admin Route Authorization | Customer attempts accessing `/admin/dashboard` | `AdminFilter` intercepts and redirects with error | Access denied unless `user.role == 'ADMIN'` | **PASSED** |
| **TC-17** | Admin Login | Submit admin credentials to `/admin/login` | Session granted admin privileges; redirect to `/admin/dashboard` | Dashboard displays real-time metrics cards | **PASSED** |
| **TC-18** | Admin Catalog CRUD | Create product via modal in `/admin/products` | New product inserted and immediately visible in storefront | PreparedStatement insert executed; catalog updated | **PASSED** |
| **TC-19** | Java Socket Client-Server | Run `InventoryServer` (port 8089) and `InventoryClient` | Client sends `GET_PRODUCT`, `GET_STOCK`, `SEARCH_PRODUCT` | Client receives serialized `NetworkResponse` objects | **PASSED** |
| **TC-20** | Java RMI Remote Invocation | Run `RmiServer` (port 1099) and `RmiClient` | Registry lookup and remote calls `getProduct()`, `updateStock()` | Distributed method executed across JVMs; stock updated | **PASSED** |
| **TC-21** | Swing Admin Compilation & Launch | Execute `LoginFrame.main()` | Swing window renders with Look and Feel and demo credentials | Swing GUI loads with table models and JDBC DAOs | **PASSED** |
