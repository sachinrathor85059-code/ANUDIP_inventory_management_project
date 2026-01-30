CREATE DATABASE inventorydb;
USE inventorydb;

---- Categories
--CREATE TABLE categories (
--    category_id INT AUTO_INCREMENT PRIMARY KEY,
--    category_name VARCHAR(100) NOT NULL UNIQUE,
--    description VARCHAR(255) NULL
--);
--
---- Items
--CREATE TABLE items (
--    item_id INT AUTO_INCREMENT PRIMARY KEY,
--    item_code VARCHAR(50) UNIQUE NOT NULL,
--    item_name VARCHAR(200) NOT NULL,
--    category_id INT NOT NULL,
--    unit VARCHAR(20) NOT NULL,
--    sku VARCHAR(100),
--    barcode VARCHAR(100),
--    purchase_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
--    sale_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
--    reorder_level INT DEFAULT 0,
--    gst DECIMAL(5,2) DEFAULT 0.00,
--    expiry_date DATE,
--    is_active TINYINT(1) DEFAULT 1,
--    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
--    FOREIGN KEY (category_id) REFERENCES categories(category_id)
--);
--
------ Suppliers
--CREATE TABLE suppliers (
--    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
--    supplier_name VARCHAR(150) NOT NULL,
--    contact_person VARCHAR(100),
--    phone VARCHAR(20),
--    email VARCHAR(100),
--    address VARCHAR(255),
--    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
--);
--
---- Purchases
--CREATE TABLE purchases (
--    purchase_id INT AUTO_INCREMENT PRIMARY KEY,
--    supplier_id INT NOT NULL,
--    invoice_no VARCHAR(100) NOT NULL,
--    purchase_date DATE NOT NULL,
--    total_amount DECIMAL(12,2) NOT NULL,
--    tax_amount DECIMAL(10,2) DEFAULT 0,
--    discount DECIMAL(10,2) DEFAULT 0,
--    status VARCHAR(20) DEFAULT 'received',
--    created_by VARCHAR(50),
--    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
--    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
--);
--
---- Purchase Items
--CREATE TABLE purchase_items (
--    pi_id INT AUTO_INCREMENT PRIMARY KEY,
--    purchase_id INT NOT NULL,
--    item_id INT NOT NULL,
--    quantity DECIMAL(10,2) NOT NULL,
--    unit_price DECIMAL(10,2) NOT NULL,
--    amount DECIMAL(12,2) NOT NULL,
--    FOREIGN KEY (purchase_id) REFERENCES purchases(purchase_id),
--    FOREIGN KEY (item_id) REFERENCES items(item_id)
--);
--
---- Sales
--CREATE TABLE sales (
--    sale_id INT AUTO_INCREMENT PRIMARY KEY,
--    invoice_no VARCHAR(100) NOT NULL,
--    sale_date DATE NOT NULL,
--    customer_name VARCHAR(150),
--    total_amount DECIMAL(12,2) NOT NULL,
--    tax_amount DECIMAL(10,2) DEFAULT 0,
--    discount DECIMAL(10,2) DEFAULT 0,
--    status VARCHAR(20) DEFAULT 'completed',
--    created_by VARCHAR(50),
--    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
--);
--
---- Sales Items
--CREATE TABLE sales_items (
--    si_id INT AUTO_INCREMENT PRIMARY KEY,
--    sale_id INT NOT NULL,
--    item_id INT NOT NULL,
--    quantity DECIMAL(10,2) NOT NULL,
--    unit_price DECIMAL(10,2) NOT NULL,
--    amount DECIMAL(12,2) NOT NULL,
--    FOREIGN KEY (sale_id) REFERENCES sales(sale_id),
--    FOREIGN KEY (item_id) REFERENCES items(item_id)
--);
--
---- Stock Ledger
--CREATE TABLE stock_ledger (
--    ledger_id INT AUTO_INCREMENT PRIMARY KEY,
--    item_id INT NOT NULL,
--    transaction_type VARCHAR(20) NOT NULL,
--    reference_id INT,
--    quantity DECIMAL(10,2) NOT NULL,
--    balance_quantity DECIMAL(12,2) NOT NULL,
--    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
--    remarks VARCHAR(255),
--    FOREIGN KEY (item_id) REFERENCES items(item_id)
--);
--
---- Users
--CREATE TABLE users (
--    user_id INT AUTO_INCREMENT PRIMARY KEY,
--    username VARCHAR(50) UNIQUE NOT NULL,
--    password VARCHAR(255) NOT NULL,
--    role VARCHAR(30) DEFAULT 'storekeeper',
--    full_name VARCHAR(100),
--    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
--);



-- Categories
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255) NULL
);

-- Items
CREATE TABLE items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_code VARCHAR(50) UNIQUE NOT NULL,
    item_name VARCHAR(200) NOT NULL,
    category_id INT NOT NULL,
    unit VARCHAR(20) NOT NULL,
    sku VARCHAR(100),
    user_id INT NOT NULL,
    purchase_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    sale_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    quantity DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    gst DECIMAL(5,2) DEFAULT 0.00,
    expiry_date DATE,
    is_active TINYINT(1) DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


--Users
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- store hashed in real apps
    role VARCHAR(30) DEFAULT 'storekeeper',
    full_name VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


