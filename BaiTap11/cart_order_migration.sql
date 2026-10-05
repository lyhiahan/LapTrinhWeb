USE WebProject_24133016;
GO

-- Chạy file này nếu database cũ đã có các bảng Users/Videos.
IF COL_LENGTH('Videos', 'Price') IS NULL
    ALTER TABLE Videos ADD Price DECIMAL(18,2) NOT NULL CONSTRAINT DF_Videos_Price DEFAULT 0;
GO
IF COL_LENGTH('Videos', 'Stock') IS NULL
    ALTER TABLE Videos ADD Stock INT NOT NULL CONSTRAINT DF_Videos_Stock DEFAULT 0;
GO
IF NOT EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = 'CK_Videos_Stock')
    ALTER TABLE Videos ADD CONSTRAINT CK_Videos_Stock CHECK (Stock >= 0);
GO

UPDATE Videos SET Price = 99000 WHERE Price = 0;
UPDATE Videos SET Stock = 20 WHERE Stock = 0;
GO

IF OBJECT_ID('Orders', 'U') IS NULL
BEGIN
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
    CREATE INDEX IX_Orders_Username_Status_CreatedAt ON Orders(Username, Status, CreatedAt DESC);
END;
GO

IF OBJECT_ID('OrderItems', 'U') IS NULL
BEGIN
    CREATE TABLE OrderItems (
        OrderItemId BIGINT IDENTITY(1,1) PRIMARY KEY,
        OrderId BIGINT NOT NULL FOREIGN KEY REFERENCES Orders(OrderId) ON DELETE CASCADE,
        VideoId NVARCHAR(50) NOT NULL,
        ProductName NVARCHAR(200) NOT NULL,
        Poster NVARCHAR(500),
        UnitPrice DECIMAL(18,2) NOT NULL CHECK (UnitPrice >= 0),
        Quantity INT NOT NULL CHECK (Quantity > 0)
    );
    CREATE INDEX IX_OrderItems_OrderId ON OrderItems(OrderId);
END;
GO

-- Đổi trạng thái thủ công để quan sát bộ lọc lịch sử:
-- UPDATE Orders SET Status = 'NEW'        WHERE OrderId = 1;
-- UPDATE Orders SET Status = 'CONFIRMED'  WHERE OrderId = 1;
-- UPDATE Orders SET Status = 'PREPARING'  WHERE OrderId = 1;
-- UPDATE Orders SET Status = 'SHIPPING'   WHERE OrderId = 1;
-- UPDATE Orders SET Status = 'DELIVERING' WHERE OrderId = 1;
-- UPDATE Orders SET Status = 'DELIVERED'  WHERE OrderId = 1;
-- UPDATE Orders SET Status = 'CANCELLED'  WHERE OrderId = 1;
-- UPDATE Orders SET Status = 'RETURNED'   WHERE OrderId = 1;
