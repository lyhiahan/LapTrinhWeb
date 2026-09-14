# BÀI TẬP 6: SPRING BOOT THYMELEAF & THYMELEAF LAYOUT DIALECT

## THÔNG TIN SINH VIÊN THỰC HIỆN
- **Họ và tên:** Lý Gia Hân
- **MSSV:** 24133016
- **Email:** lygiahan.786@gmail.com
- **Môn học:** Lập trình Web (WEBPR330479)
- **Đơn vị:** Khoa Công nghệ Thông tin - Trường ĐH Sư Phạm Kỹ Thuật TP.HCM (HCMUTE)

---

## TỔNG QUAN DỰ ÁN
1. **Spring Boot Framework** (v3.3.4, Java 17).
2. **Thymeleaf Template Engine** kết hợp **Thymeleaf Layout Dialect** (`nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect`) để xây dựng bố cục trang dạng component/decorator.
3. **Bố cục chuẩn**:
   - **Header**: Chứa hình ảnh cá nhân sinh viên Lý Gia Hân (`myself.jpg`), hệ thống điều hướng và bộ chuyển đổi ngôn ngữ đa quốc gia (i18n).
   - **Content**: Vùng nội dung động (`layout:fragment="content"`) cho phép nhúng các trang Danh sách phân trang, Tìm kiếm, Thêm và Sửa danh mục.
   - **Footer**: Chứa thông tin đầy đủ của sinh viên thực hiện (Họ tên, MSSV, Email, Trường).
4. **Chức năng Quản lý Danh mục (Category CRUD)**:
   - **Xem danh sách**: Hiển thị bảng danh mục đẹp mắt với ảnh/icon preview, tên, đơn giá.
   - **Tìm kiếm**: Tìm kiếm tương đối không phân biệt hoa thường theo tên danh mục.
   - **Phân trang linh hoạt**: Cho phép chọn số lượng bản ghi hiển thị (5, 10, 20 dòng/trang), chuyển trang trước/sau, đầu/cuối và các số trang động.
   - **Thêm mới & Chỉnh sửa**: Form nhập liệu có xem trước ảnh trực tiếp (Live image preview) trước khi lưu, xử lý lưu file ảnh vào thư mục `upload/category/`.
   - **Xóa**: Modal cảnh báo xác nhận xóa an toàn Bootstrap 5 và dọn dẹp file ảnh khỏi ổ đĩa.
5. **Hỗ trợ Đa ngôn ngữ (i18n)**:
   - Hỗ trợ Tiếng Việt (`messages_vi.properties`) và Tiếng Anh (`messages_en.properties`).
   - Chuyển đổi ngôn ngữ tức thì qua tham số: `?language=vi_VN` hoặc `?language=en`.

---

## CẤU HÌNH VÀ KHỞI CHẠY DỰ ÁN

### 1. Cấu hình CSDL (Microsoft SQL Server / MySQL)
- Mặc định kết nối tới SQL Server:
  - Database: `ShoppingServiceMVC`
  - Port: SQL Server default (`1433` hoặc named instance `SQLEXPRESS01`)
  - Username: `sa` | Password: `giahan`
- Script `BaiTap6.sql` nạp sẵn 10 danh mục mẫu phong phú.

### 2. Khởi chạy dự án
- **Cách 1: Chạy từ Eclipse / Spring Tool Suite (STS) / IntelliJ IDEA:**
  - Nhấp chuột phải vào dự án `BaiTap6` -> **Run As** -> **Spring Boot App** (hoặc chạy class `vn.iotstar.BaiTap6Application.java`).
- **Cách 2: Chạy từ dòng lệnh:**
  ```bash
  cd d:\LAPTRINH\LAPTRINHWEB\BAITAP\BT\LapTrinhWeb\BaiTap6
  mvn spring-boot:run
  ```

### 3. Truy cập ứng dụng
- **Trang chủ quản trị danh mục:** [http://localhost:8088/admin/categories](http://localhost:8088/admin/categories)
- **Thêm danh mục mới:** [http://localhost:8088/admin/categories/add](http://localhost:8088/admin/categories/add)
- **Chuyển sang Tiếng Anh:** [http://localhost:8088/admin/categories?language=en](http://localhost:8088/admin/categories?language=en)
- **Chuyển sang Tiếng Việt:** [http://localhost:8088/admin/categories?language=vi_VN](http://localhost:8088/admin/categories?language=vi_VN)
