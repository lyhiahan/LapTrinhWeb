
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'ShoppingServiceMVC')
BEGIN
    CREATE DATABASE ShoppingServiceMVC;
END;
GO

USE ShoppingServiceMVC;
GO

-- 1. Bảng User
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'User')
BEGIN
    CREATE TABLE [User] (
        id INT IDENTITY(1,1) PRIMARY KEY,
        email VARCHAR(100) UNIQUE,
        username VARCHAR(50) UNIQUE NOT NULL,
        fullname NVARCHAR(100),
        password VARCHAR(50) NOT NULL,
        avatar VARCHAR(255) NULL,
        roleid INT DEFAULT 5,
        phone VARCHAR(20) NULL,
        createddate DATE,
        is_active BIT DEFAULT 0,
        otp VARCHAR(10) NULL,
        otp_expiry DATETIME NULL
    );
END;
GO

-- Xoá dữ liệu cũ nếu có
DELETE FROM [User];
GO

-- Thêm user mặc định (đã kích hoạt)
INSERT INTO [User](email, username, fullname, password, roleid, createddate, is_active) 
VALUES ('admin@shop.com', 'admin', N'Quản Trị Viên', '123456', 1, GETDATE(), 1);

INSERT INTO [User](email, username, fullname, password, roleid, createddate, is_active) 
VALUES ('user@gmail.com', 'user', N'Người Dùng', '123456', 5, GETDATE(), 1);
GO

-- 2. Bảng Category (Hibernate sẽ tự tạo, nhưng cung cấp script để tham khảo)
/*
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Category')
BEGIN
    CREATE TABLE Category (
        cate_id INT IDENTITY(1,1) PRIMARY KEY,
        cate_name NVARCHAR(255) NOT NULL,
        icons NVARCHAR(255) NULL,
        price FLOAT
    );
END;
GO
*/

-- 3. Bảng Product (Hibernate sẽ tự tạo, nhưng cung cấp script để tham khảo)
/*
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Product')
BEGIN
    CREATE TABLE Product (
        product_id INT IDENTITY(1,1) PRIMARY KEY,
        product_name NVARCHAR(255) NOT NULL,
        image VARCHAR(255) NULL,
        price FLOAT,
        description NTEXT,
        created_date DATE,
        cate_id INT,
        FOREIGN KEY (cate_id) REFERENCES Category(cate_id)
    );
END;
GO
*/
