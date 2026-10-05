CREATE DATABASE WebProject_24133016;
GO
USE WebProject_24133016;
GO

-- 1. Bảng Users
CREATE TABLE Users (
    Username NVARCHAR(50) PRIMARY KEY,
    Password NVARCHAR(50) NOT NULL,
    Phone NVARCHAR(15),
    Fullname NVARCHAR(50),
    Email NVARCHAR(150),
    Admin BIT DEFAULT 0,
    Active BIT DEFAULT 1,
    Images NVARCHAR(500),
    otp NVARCHAR(10),
    otpExpiry DATETIME
);

-- 2. Bảng Category
CREATE TABLE Category (
    CategoryId INT IDENTITY(1,1) PRIMARY KEY,
    Categoryname NVARCHAR(100),
    Categorycode NVARCHAR(100),
    Images NVARCHAR(500),
    Status BIT DEFAULT 1
);

-- 3. Bảng Videos
CREATE TABLE Videos (
    VideoId NVARCHAR(50) PRIMARY KEY,
    Title NVARCHAR(200),
    Poster NVARCHAR(500),
    Views INT DEFAULT 0,
    Description NVARCHAR(500),
    Active BIT DEFAULT 1,
    Price DECIMAL(18,2) NOT NULL DEFAULT 0,
    Stock INT NOT NULL DEFAULT 0 CHECK (Stock >= 0),
    CategoryId INT FOREIGN KEY REFERENCES Category(CategoryId)
);

-- 4. Bảng Favorites
CREATE TABLE Favorites (
    FavoriteId INT IDENTITY(1,1) PRIMARY KEY,
    LikedDate DATE DEFAULT GETDATE(),
    VideoId NVARCHAR(50) FOREIGN KEY REFERENCES Videos(VideoId),
    Username NVARCHAR(50) FOREIGN KEY REFERENCES Users(Username)
);

-- 5. Bảng Shares
CREATE TABLE Shares (
    ShareId INT IDENTITY(1,1) PRIMARY KEY,
    Emails NVARCHAR(50),
    SharedDate DATE DEFAULT GETDATE(),
    Username NVARCHAR(50) FOREIGN KEY REFERENCES Users(Username),
    VideoId NVARCHAR(50) FOREIGN KEY REFERENCES Videos(VideoId)
);

-- 6. Đơn hàng COD và chi tiết đơn hàng
CREATE TABLE Orders (
    OrderId BIGINT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(50) NOT NULL FOREIGN KEY REFERENCES Users(Username),
    CreatedAt DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    Status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    PaymentMethod VARCHAR(20) NOT NULL DEFAULT 'COD',
    ReceiverName NVARCHAR(100) NOT NULL,
    Phone NVARCHAR(15) NOT NULL,
    Address NVARCHAR(500) NOT NULL,
    Note NVARCHAR(500),
    TotalAmount DECIMAL(18,2) NOT NULL CHECK (TotalAmount >= 0),
    CONSTRAINT CK_Orders_Status CHECK (Status IN
        ('NEW','CONFIRMED','PREPARING','SHIPPING','DELIVERING','DELIVERED','CANCELLED','RETURNED')),
    CONSTRAINT CK_Orders_Payment CHECK (PaymentMethod = 'COD')
);

CREATE TABLE OrderItems (
    OrderItemId BIGINT IDENTITY(1,1) PRIMARY KEY,
    OrderId BIGINT NOT NULL FOREIGN KEY REFERENCES Orders(OrderId) ON DELETE CASCADE,
    VideoId NVARCHAR(50) NOT NULL,
    ProductName NVARCHAR(200) NOT NULL,
    Poster NVARCHAR(500),
    UnitPrice DECIMAL(18,2) NOT NULL CHECK (UnitPrice >= 0),
    Quantity INT NOT NULL CHECK (Quantity > 0)
);

CREATE INDEX IX_Orders_Username_Status_CreatedAt ON Orders(Username, Status, CreatedAt DESC);
CREATE INDEX IX_OrderItems_OrderId ON OrderItems(OrderId);

-- Chèn dữ liệu mẫu ban đầu để chạy kiểm thử
INSERT INTO Users (Username, Password, Phone, Fullname, Email, Admin, Active)
VALUES 
('admin', '123', '0901234567', N'Quản Trị Viên', 'admin@gmail.com', 1, 1),
('user1', '123', '0912345678', N'Lý Gia Hân', 'giahan@gmail.com', 0, 1);

INSERT INTO Category (Categoryname, Categorycode, Status)
VALUES 
(N'Lập Trình Web', 'WEB', 1),
(N'Âm Nhạc', 'MUSIC', 1);

INSERT INTO Videos (VideoId, Title, Poster, Views, Description, Active, Price, Stock, CategoryId)
VALUES 
('V01', N'Lập trình JSP Servlet và JPA', 'https://picsum.photos/300/180?random=1', 150, N'Hướng dẫn chi tiết từ A-Z', 1, 199000, 20, 1),
('V02', N'Xây dựng RESTful API với Spring', 'https://picsum.photos/300/180?random=2', 230, N'Spring MVC & Spring Data JPA', 1, 249000, 15, 1),
('V03', N'Quản trị CSDL SQL Server', 'https://picsum.photos/300/180?random=3', 95, N'Tối ưu hóa bảng và chỉ mục', 1, 179000, 12, 1),
('V04', N'Thiết kế giao diện Bootstrap 5', 'https://picsum.photos/300/180?random=4', 310, N'Tạo web responsive dễ dàng', 1, 159000, 25, 1),
('V05', N'Nhạc Lofi Học Bài Đêm Muộn', 'https://picsum.photos/300/180?random=5', 850, N'Giai điệu thư giãn giảm căng thẳng', 1, 99000, 30, 2),
('V06', N'Tuyển Tập Nhạc Acoustic Bất Hủ', 'https://picsum.photos/300/180?random=6', 420, N'Những bản tình ca nhẹ nhàng', 1, 129000, 18, 2),
('V07', N'Piano Không Lời Tập Trung Làm Việc', 'https://picsum.photos/300/180?random=7', 620, N'Nhạc sóng não tập trung cao độ', 1, 89000, 22, 2);

-- Thay đổi trạng thái để kiểm tra màn hình lịch sử, ví dụ:
-- UPDATE Orders SET Status = 'CONFIRMED' WHERE OrderId = 1;
-- Các giá trị hợp lệ: NEW, CONFIRMED, PREPARING, SHIPPING, DELIVERING,
-- DELIVERED, CANCELLED, RETURNED.
