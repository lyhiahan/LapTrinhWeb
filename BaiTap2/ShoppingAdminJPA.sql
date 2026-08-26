
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'ShoppingServiceMVC')
BEGIN
    CREATE DATABASE ShoppingServiceMVC;
END;
GO

USE ShoppingServiceMVC;
GO
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
        createddate DATE
    );
END;
GO
INSERT INTO [User](email, username, fullname, password, roleid, createddate) 
VALUES ('admin@shop.com', 'admin', N'Quản Trị Viên', '123456', 1, GETDATE());

INSERT INTO [User](email, username, fullname, password, roleid, createddate) 
VALUES ('user@gmail.com', 'user', N'Người Dùng', '123456', 5, GETDATE());
GO

-- 4. Bảng Category sẽ do Hibernate tự tạo khi khởi động ứng dụng
-- Nếu muốn chèn dữ liệu mẫu Category, chạy ứng dụng trước rồi chạy lệnh sau:
-- INSERT INTO Category (cate_name, icons, price) 
-- VALUES (N'Điện thoại', 'https://i.imgur.com/vHqJ69m.png', 15000000),
--        (N'Laptop', 'https://i.imgur.com/vHqJ69m.png', 25000000);
