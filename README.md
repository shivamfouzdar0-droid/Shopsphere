# ShopSphere Database Documentation

## Database Information
- **Database Engine:** MySQL 8.x / 9.x (InnoDB engine)
- **Database Name:** `shopsphere`
- **Default Character Set:** `utf8mb4`
- **Collation:** `utf8mb4_unicode_ci`

---

## Quick Setup Instructions

### 1. Via MySQL Command Line Client:
```bash
mysql -u root -p < database/shopsphere.sql
```

### 2. Via MySQL Workbench:
1. Open MySQL Workbench and connect to your local MySQL instance.
2. Go to **File -> Open SQL Script...** and select `database/shopsphere.sql`.
3. Click the lightning bolt icon (Execute) to execute the entire script.
4. Refresh the **SCHEMAS** panel in MySQL Workbench to see the `shopsphere` database and its 12 tables.

---

## Pre-seeded Demo Accounts
All passwords are encrypted with SHA-256 standard hashing:

| Role | Name | Email | Password |
|---|---|---|---|
| **ADMIN** | Administrator | `admin@shopsphere.com` | `admin123` |
| **CUSTOMER** | Rahul Sharma | `customer@shopsphere.com` | `customer123` |
| **CUSTOMER** | Priya Patel | `priya@shopsphere.com` | `customer123` |

---

## Active Discount Coupons

| Coupon Code | Discount Type | Discount Value | Min Order | Max Cap |
|---|---|---|---|---|
| `WELCOME10` | Percentage | 10% | ₹1,000 | ₹500 |
| `FLAT500` | Fixed | ₹500 | ₹5,000 | ₹500 |
| `FESTIVE20` | Percentage | 20% | ₹2,000 | ₹1,000 |
