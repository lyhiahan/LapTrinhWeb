IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'ShoppingServiceMVC')
BEGIN
    CREATE DATABASE ShoppingServiceMVC;
END;
GO

USE ShoppingServiceMVC;
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Category')
BEGIN
    CREATE TABLE Category (
        cate_id INT IDENTITY(1,1) PRIMARY KEY,
        cate_name NVARCHAR(255) NOT NULL,
        icons NVARCHAR(255) NULL,
        price FLOAT DEFAULT 0
    );
END;
GO

IF NOT EXISTS (SELECT 1 FROM Category WHERE cate_name = N'Điện thoại & Phụ kiện')
BEGIN
    INSERT INTO Category(cate_name, icons, price) VALUES
    (N'Điện thoại & Phụ kiện', NULL, 15000000),
    (N'Laptop & Máy tính văn phòng', NULL, 22000000),
    (N'Đồng hồ thông minh Smartwatch', NULL, 4500000),
    (N'Thiết bị âm thanh & Tai nghe', NULL, 1800000),
    (N'Máy ảnh & Thiết bị quay phim', NULL, 18500000),
    (N'Thời trang Nam cao cấp', NULL, 650000),
    (N'Thời trang Nữ thanh lịch', NULL, 750000),
    (N'Mỹ phẩm & Chăm sóc sắc đẹp', NULL, 520000),
    (N'Đồ gia dụng thông minh', NULL, 3200000),
    (N'Phụ kiện Gaming & Bàn phím cơ', NULL, 1250000);
END;
GO
