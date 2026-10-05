# Cấu hình email OTP và mật khẩu

## 0. Cấu hình kết nối database

Thông tin nhạy cảm không còn được lưu trong source. Trước khi chạy ứng dụng bằng
PowerShell, thiết lập mật khẩu SQL Server (và thay username/URL nếu máy khác):

```powershell
$env:DB_USERNAME="sa"
$env:DB_PASSWORD="your-sql-server-password"
# Tùy chọn: $env:DB_URL="jdbc:sqlserver://..."
```

## 1. Nâng cấp database cũ

Chạy `auth_security_migration.sql` trên database hiện có. Script nâng cột
`Users.Password` từ `NVARCHAR(50)` lên `NVARCHAR(255)` để đủ chỗ cho chuỗi
PBKDF2. Database tạo mới bằng `Web.sql` không cần bước này.

Tài khoản cũ có mật khẩu plaintext vẫn đăng nhập được. Sau lần đăng
nhập thành công đầu tiên, hệ thống tự chuyển mật khẩu đó sang PBKDF2.

## 2. Cấu hình SMTP

Không lưu tài khoản hoặc App Password Gmail trong source code. Trước khi chạy
ứng dụng bằng PowerShell, thiết lập:

```powershell
$env:SMTP_USERNAME="your-account@gmail.com"
$env:SMTP_PASSWORD="your-google-app-password"
$env:SMTP_FROM="your-account@gmail.com"
mvn spring-boot:run
```

Có thể thay máy chủ/cổng bằng `SMTP_HOST`, `SMTP_PORT`. Nếu SMTP chưa được
cấu hình hoặc gửi thất bại, thao tác đăng ký/gửi lại OTP sẽ báo thất bại;
mã OTP không bị ghi ra console.

Nếu App Password cũ từng được commit hoặc chia sẻ, hãy thu hồi nó trong
tài khoản Google và tạo App Password mới.
