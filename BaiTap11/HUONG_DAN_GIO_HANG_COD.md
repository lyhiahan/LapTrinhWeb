# Hướng dẫn chạy chức năng giỏ hàng và COD

## 1. Cập nhật database

- Database đã tồn tại: chạy toàn bộ file `cart_order_migration.sql` bằng SQL Server Management Studio.
- Tạo database mới: chạy toàn bộ file `Web.sql`.
- Dự án đang dùng `spring.jpa.hibernate.ddl-auto=none`, vì vậy bước cập nhật database là bắt buộc.

## 2. Luồng kiểm thử

1. Chạy ứng dụng, mở `/home`.
2. Thêm sản phẩm vào giỏ. Thử tăng/giảm số lượng, nhập `0` để xóa, và nhập lớn hơn tồn kho để kiểm tra giới hạn.
3. Đăng nhập bằng `user1 / 123`, vào `/cart`, chọn **Thanh toán COD**.
4. Nhập thông tin nhận hàng và xác nhận. Đơn mới có trạng thái `NEW`, tồn kho giảm đúng số lượng, giỏ hàng được làm trống.
5. Mở `/orders` và chọn các nút trạng thái để lọc.

## 3. Kiểm tra thay đổi trạng thái trong database

Thay `1` bằng mã đơn vừa tạo rồi chạy từng câu lệnh:

```sql
UPDATE Orders SET Status = 'NEW'        WHERE OrderId = 1;
UPDATE Orders SET Status = 'CONFIRMED'  WHERE OrderId = 1;
UPDATE Orders SET Status = 'PREPARING'  WHERE OrderId = 1;
UPDATE Orders SET Status = 'SHIPPING'   WHERE OrderId = 1;
UPDATE Orders SET Status = 'DELIVERING' WHERE OrderId = 1;
UPDATE Orders SET Status = 'DELIVERED'  WHERE OrderId = 1;
UPDATE Orders SET Status = 'CANCELLED'  WHERE OrderId = 1;
UPDATE Orders SET Status = 'RETURNED'   WHERE OrderId = 1;
```

Tải lại `/orders` sau mỗi lần cập nhật để xem nhãn và bộ lọc tương ứng.

## 4. Build

```powershell
mvn -DskipTests package
```

File triển khai được tạo tại `target/WebProject_24133016.war`.
