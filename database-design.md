# ShopSphere — Database Design & Entity Relationship

## 1. Entity-Relationship (ER) Diagram

```mermaid
erDiagram
    USERS ||--o{ ADDRESSES : "has many"
    USERS ||--o{ CART : "owns"
    USERS ||--o{ WISHLIST : "saves"
    USERS ||--o{ ORDERS : "places"
    USERS ||--o{ REVIEWS : "writes"
    USERS ||--o{ COUPON_USAGE : "redeems"

    CATEGORIES ||--o{ PRODUCTS : "categorizes"
    PRODUCTS ||--o{ CART : "contained in"
    PRODUCTS ||--o{ WISHLIST : "added to"
    PRODUCTS ||--o{ ORDER_ITEMS : "included in"
    PRODUCTS ||--o{ REVIEWS : "reviewed in"

    ORDERS ||--|{ ORDER_ITEMS : "contains"
    ORDERS ||--|| PAYMENTS : "generates"
    ORDERS ||--o| ADDRESSES : "delivers to"

    COUPONS ||--o{ COUPON_USAGE : "tracked by"

    USERS {
        int user_id PK
        string name
        string email UK
        string password
        string mobile
        string role
        tinyint status
        timestamp created_at
    }

    ADDRESSES {
        int address_id PK
        int user_id FK
        string address_line
        string city
        string state
        string pincode
        string address_type
    }

    CATEGORIES {
        int category_id PK
        string name UK
        string description
        tinyint status
    }

    PRODUCTS {
        int product_id PK
        int category_id FK
        string name
        string brand
        text description
        decimal price
        decimal discount
        int stock
        string image_url
        tinyint status
        timestamp created_at
    }

    CART {
        int cart_id PK
        int user_id FK
        int product_id FK
        int quantity
    }

    WISHLIST {
        int wishlist_id PK
        int user_id FK
        int product_id FK
        timestamp created_at
    }

    ORDERS {
        int order_id PK
        int user_id FK
        int address_id FK
        decimal total_amount
        decimal discount
        string payment_method
        string payment_status
        string order_status
        timestamp created_at
    }

    ORDER_ITEMS {
        int order_item_id PK
        int order_id FK
        int product_id FK
        int quantity
        decimal price
    }

    PAYMENTS {
        int payment_id PK
        int order_id FK
        string payment_method
        string transaction_reference
        decimal amount
        string payment_status
        timestamp created_at
    }

    REVIEWS {
        int review_id PK
        int user_id FK
        int product_id FK
        int rating
        text review_text
        timestamp created_at
    }

    COUPONS {
        int coupon_id PK
        string code UK
        string discount_type
        decimal discount_value
        decimal minimum_order
        decimal maximum_discount
        date expiry_date
        tinyint status
    }

    COUPON_USAGE {
        int usage_id PK
        int coupon_id FK
        int user_id FK
        int order_id FK
        timestamp used_at
    }
```

---

## 2. Table Data Dictionary

### `users`
- Stores registered customer and staff administrator accounts.
- `password`: Stored as a 64-character SHA-256 cryptographic hash (never plaintext).
- `role`: Restricted to `'CUSTOMER'` or `'ADMIN'`.

### `products`
- Catalogs physical inventory with current pricing, discount percentage, and warehouse stock.
- `stock`: Checked prior to order confirmation and deducted atomically within the checkout transaction.

### `orders` & `order_items`
- Maintains immutable historical records of placed customer orders.
- Stores historical purchase price per unit inside `order_items` so that future product price modifications do not alter completed order statements.
