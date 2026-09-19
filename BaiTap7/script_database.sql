-- ===================================================
-- SCRIPT TẠO DATABASE VÀ BẢNG CHO DỰ ÁN CRUD API CATEGORY
-- ===================================================

CREATE DATABASE db_crud_api;
GO

USE db_crud_api;
GO

-- 1. Tạo bảng Danh mục (categories)
CREATE TABLE categories (
    category_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    category_name NVARCHAR(255) NOT NULL,
    icon NVARCHAR(255)
);
GO

-- 2. Tạo bảng Sản phẩm (products)
CREATE TABLE products (
    product_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    product_name NVARCHAR(500) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    unit_price FLOAT NOT NULL DEFAULT 0,
    images NVARCHAR(200),
    description NVARCHAR(500) NOT NULL,
    discount FLOAT NOT NULL DEFAULT 0,
    create_date DATETIME2(6) DEFAULT GETDATE(),
    status SMALLINT NOT NULL DEFAULT 1,
    category_id BIGINT,
    CONSTRAINT FK_product_category FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);
GO

-- 3. Dữ liệu mẫu ban đầu (Sample Data)
INSERT INTO categories (category_name, icon) VALUES 
(N'Điện thoại', N'p_phone.png'),
(N'Laptop', N'p_laptop.png'),
(N'Phụ kiện', N'p_accessory.png');
GO

INSERT INTO products (product_name, quantity, unit_price, images, description, discount, create_date, status, category_id) VALUES 
(N'iPhone 15 Pro Max', 10, 29990000, N'iphone15.jpg', N'Điện thoại flagship Apple', 5, GETDATE(), 1, 1),
(N'MacBook Air M2', 5, 24990000, N'macbook.jpg', N'Laptop mỏng nhẹ Apple', 10, GETDATE(), 1, 2);
GO
