# Bài Tập 8: Xây dựng API bằng GraphQL + Spring Boot & AJAX

## 1. Giới thiệu
Ứng dụng Spring Boot 3 kết hợp với Spring GraphQL và giao diện Web AJAX:
- **GraphQL API**:
  - `productsByPriceAsc`: Hiển thị tất cả sản phẩm sắp xếp theo giá từ thấp đến cao.
  - `productsByCategory(categoryId: ID!)`: Lấy danh sách sản phẩm theo danh mục.
  - CRUD bảng `Category` (`createCategory`, `updateCategory`, `deleteCategory`, `allCategories`, `categoryById`).
  - CRUD bảng `Product` (`createProduct`, `updateProduct`, `deleteProduct`, `allProducts`, `productById`).
- **Giao diện Web AJAX**:
  - Giao diện Dashboard hiện đại bằng HTML, CSS, JavaScript (jQuery AJAX) tại `src/main/resources/static/index.html`.
  - Quản lý CRUD Category và Product trực tiếp bằng AJAX gọi tới endpoint `/graphql`.
  - Sắp xếp giá tăng dần và lọc sản phẩm theo danh mục động.

## 2. Cách chạy ứng dụng
```bash
./mvnw spring-boot:run
```
- Giao diện Web: http://localhost:8080
- GraphiQL IDE: http://localhost:8080/graphiql
- H2 Database Console: http://localhost:8080/h2-console
