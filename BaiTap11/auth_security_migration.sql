-- Run once on an existing WebProject_24133016 database before using hashed passwords.
-- Safe to run repeatedly. New databases created from Web.sql already use NVARCHAR(255).

IF OBJECT_ID(N'dbo.Users', N'U') IS NOT NULL
   AND EXISTS (
       SELECT 1
       FROM sys.columns
       WHERE object_id = OBJECT_ID(N'dbo.Users')
         AND name = N'Password'
         AND max_length < 510 -- NVARCHAR stores two bytes per character
   )
BEGIN
    ALTER TABLE dbo.Users ALTER COLUMN Password NVARCHAR(255) NOT NULL;
END;
GO

-- findByEmail phải luôn trả tối đa một tài khoản. Dừng migration để người
-- quản trị xử lý dữ liệu nếu database cũ đã có email trùng.
IF OBJECT_ID(N'dbo.Users', N'U') IS NOT NULL
   AND NOT EXISTS (
       SELECT 1 FROM sys.indexes
       WHERE object_id = OBJECT_ID(N'dbo.Users') AND name = N'UX_Users_Email'
   )
BEGIN
    IF EXISTS (
        SELECT Email FROM dbo.Users
        WHERE Email IS NOT NULL
        GROUP BY Email HAVING COUNT(*) > 1
    )
        THROW 50001, N'Không thể tạo unique index: bảng Users đang có email trùng.', 1;

    CREATE UNIQUE INDEX UX_Users_Email ON dbo.Users(Email) WHERE Email IS NOT NULL;
END;
GO
