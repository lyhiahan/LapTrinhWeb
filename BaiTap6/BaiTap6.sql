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
    (N'Điện thoại & Phụ kiện', 'https://cdn-icons-png.flaticon.com/512/644/644458.png', 15000000),
    (N'Laptop & Máy tính văn phòng', 'https://cdn-icons-png.flaticon.com/512/428/428001.png', 22000000),
    (N'Đồng hồ thông minh Smartwatch', 'https://cdn-icons-png.flaticon.com/512/865/865969.png', 4500000),
    (N'Thiết bị âm thanh & Tai nghe', 'https://cdn-icons-png.flaticon.com/512/3659/3659898.png', 1800000),
    (N'Máy ảnh & Thiết bị quay phim', 'https://cdn-icons-png.flaticon.com/512/685/685655.png', 18500000),
    (N'Thời trang Nam cao cấp', 'https://cdn-icons-png.flaticon.com/512/2503/2503380.png', 650000),
    (N'Thời trang Nữ thanh lịch', 'https://cdn-icons-png.flaticon.com/512/3050/3050239.png', 750000),
    (N'Mỹ phẩm & Chăm sóc sắc đẹp', 'https://cdn-icons-png.flaticon.com/512/2701/2701724.png', 520000),
    (N'Đồ gia dụng thông minh', 'https://cdn-icons-png.flaticon.com/512/1261/1261106.png', 3200000),
    (N'Phụ kiện Gaming & Bàn phím cơ', 'https://cdn-icons-png.flaticon.com/512/3076/3076949.png', 1250000);
END;
GO
