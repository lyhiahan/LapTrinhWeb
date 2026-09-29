# Bài Tập 9 - Ví Dụ 3 (BT9-VD3)

Dự án Spring Boot quản lý người dùng và sản phẩm với Spring Security, xác thực OTP qua Email, phân quyền Role (USER / ADMIN), MapStruct và Cloudinary.

---

## 🛠 Công nghệ sử dụng
- **Java**: JDK 21+ / JDK 26
- **Framework**: Spring Boot 4.1.1
- **Security**: Spring Security 7.1.x (Session-based Authentication, Method Security)
- **Database**: Microsoft SQL Server
- **ORM**: Spring Data JPA / Hibernate
- **Template Engine**: Thymeleaf
- **Object Mapping**: MapStruct 1.6.3
- **Email Service**: Spring Mail (Gửi mã OTP đăng ký và quên mật khẩu)
- **Image Storage**: Cloudinary API
- **Build Tool**: Maven

---

## 🚀 Các chức năng chính

### 1. Authentication & Security
- **Đăng ký tài khoản (Register)**: Kiểm tra thông tin, gửi mã OTP xác thực qua email trước khi kích hoạt tài khoản.
- **Xác thực OTP (Verify OTP)**: Kiểm tra mã OTP và thời hạn hiệu lực, hỗ trợ gửi lại mã (Resend OTP).
- **Đăng nhập (Login)**: Xác thực với Spring Security, mã hóa mật khẩu bằng BCrypt, lưu trữ phiên đăng nhập (Session).
- **Quên mật khẩu (Forgot Password)**: Gửi mã OTP xác nhận đặt lại mật khẩu qua email.
- **Đổi mật khẩu (Reset Password)**: Xác minh OTP hợp lệ và cập nhật mật khẩu mới.
- **Đăng xuất (Logout)**: Hủy session an toàn.

### 2. Quản lý Người dùng (User Management)
- CRUD thông tin người dùng.
- Tìm kiếm theo từ khóa tên hoặc email.
- Phân trang danh sách người dùng.
- Phân quyền: **ROLE_USER** và **ROLE_ADMIN**.
- Thống kê tổng số người dùng và đếm số lượng sản phẩm của từng người dùng.

### 3. Quản lý Sản phẩm (Product Management)
- CRUD sản phẩm (Tên sản phẩm, Giá, Mô tả, Hình ảnh).
- Upload hình ảnh trực tiếp lên **Cloudinary**.
- Tìm kiếm và phân trang danh sách sản phẩm.
- Liên kết quan hệ: Một User sở hữu nhiều Product (1 - N).
- Kiểm soát quyền hạn: Chỉ Admin hoặc chủ sở hữu sản phẩm mới có quyền sửa/xóa sản phẩm của mình.

### 4. Kiểm thử (Testing)
- Unit Test & Integration Test cho Service và Controller (`ProductServiceSecurityTest`, `AuthServiceOtpTest`, `ProductControllerMvcTest`).
- Kiểm tra tính bảo mật và phân quyền phương thức.

---

## ⚙️ Cấu hình môi trường (.env)
Dự án sử dụng file `.env` để cấu hình thông tin bảo mật và kết nối dịch vụ:
```properties
DB_URL=jdbc:sqlserver://localhost:1433;databaseName=shop_db;encrypt=true;trustServerCertificate=true;
DB_USERNAME=sa
DB_PASSWORD=your_password

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

---

## ▶️ Cách chạy dự án
1. Khởi động SQL Server và đảm bảo database đã được tạo.
2. Cấu hình đúng thông tin kết nối trong file `.env`.
3. Chạy lệnh:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Truy cập ứng dụng tại: `http://localhost:8080`
