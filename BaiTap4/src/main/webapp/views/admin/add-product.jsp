<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm Sản Phẩm Mới - Admin</title>
</head>
<body>
    <div class="mb-4">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-1 small">
                <li class="breadcrumb-item"><a href="<c:url value='/admin/products'/>" class="text-decoration-none">Sản phẩm</a></li>
                <li class="breadcrumb-item active" aria-current="page">Thêm mới</li>
            </ol>
        </nav>
        <h3 class="fw-bold mb-1"><i class="fas fa-plus-circle text-primary me-2"></i>Thêm Sản Phẩm Mới</h3>
        <p class="text-muted small mb-0">Điền các thông tin chi tiết và tải ảnh đại diện cho sản phẩm</p>
    </div>

    <!-- Alert error từ server nếu có -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-triangle-exclamation me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row">
        <div class="col-lg-8 col-xl-7">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-4">
                    <form action="<c:url value='/admin/product/insert'/>" method="post" enctype="multipart/form-data" class="needs-validation" novalidate id="addProductForm">
                        <!-- Tên sản phẩm -->
                        <div class="mb-3">
                            <label for="productName" class="form-label fw-semibold text-dark">
                                <i class="fas fa-cube text-muted me-1"></i> Tên sản phẩm <span class="text-danger">*</span>
                            </label>
                            <input type="text"
                                   class="form-control"
                                   id="productName"
                                   name="productName"
                                   value="${not empty param.productName ? param.productName : ''}"
                                   placeholder="Ví dụ: iPhone 15 Pro Max 256GB..."
                                   minlength="2"
                                   maxlength="255"
                                   required>
                            <div class="invalid-feedback">
                                Vui lòng nhập tên sản phẩm hợp lệ (từ 2 đến 255 ký tự).
                            </div>
                        </div>

                        <!-- Danh mục & Giá -->
                        <div class="row g-3 mb-3">
                            <div class="col-sm-6">
                                <label for="categoryId" class="form-label fw-semibold text-dark">
                                    <i class="fas fa-folder text-muted me-1"></i> Danh mục phân loại <span class="text-danger">*</span>
                                </label>
                                <select class="form-select" id="categoryId" name="categoryId" required>
                                    <option value="">-- Chọn danh mục --</option>
                                    <c:forEach items="${cateList}" var="cate">
                                        <option value="${cate.id}" ${param.categoryId == cate.id ? 'selected' : ''}>
                                            ${cate.name}
                                        </option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">
                                    Vui lòng chọn một danh mục cho sản phẩm.
                                </div>
                            </div>

                            <div class="col-sm-6">
                                <label for="price" class="form-label fw-semibold text-dark">
                                    <i class="fas fa-money-bill-wave text-muted me-1"></i> Đơn giá (VNĐ) <span class="text-danger">*</span>
                                </label>
                                <div class="input-group has-validation">
                                    <input type="number"
                                           class="form-control"
                                           id="price"
                                           name="price"
                                           value="${not empty param.price ? param.price : ''}"
                                           placeholder="Ví dụ: 25000000"
                                           min="1000"
                                           step="1000"
                                           required>
                                    <span class="input-group-text bg-light text-muted">đ</span>
                                    <div class="invalid-feedback">
                                        Vui lòng nhập giá bán hợp lệ (lớn hơn hoặc bằng 1,000 VNĐ).
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Mô tả chi tiết -->
                        <div class="mb-3">
                            <label for="description" class="form-label fw-semibold text-dark">
                                <i class="fas fa-align-left text-muted me-1"></i> Mô tả tóm tắt sản phẩm
                            </label>
                            <textarea class="form-control"
                                      id="description"
                                      name="description"
                                      rows="4"
                                      placeholder="Mô tả các thông số, ưu điểm nổi bật của sản phẩm...">${not empty param.description ? param.description : ''}</textarea>
                        </div>

                        <!-- Hình ảnh sản phẩm -->
                        <div class="mb-4">
                            <label for="imageInput" class="form-label fw-semibold text-dark">
                                <i class="fas fa-image text-muted me-1"></i> Hình ảnh đại diện sản phẩm <span class="text-danger">*</span>
                            </label>
                            <input type="file"
                                   class="form-control"
                                   id="imageInput"
                                   name="image"
                                   accept="image/png, image/jpeg, image/jpg, image/webp, image/gif"
                                   required>
                            <div class="invalid-feedback" id="imageFeedback">
                                Vui lòng chọn một tệp hình ảnh hợp lệ (.jpg, .jpeg, .png, .webp).
                            </div>

                            <div class="mt-3 text-center p-3 border rounded-3 bg-light d-none" id="previewContainer">
                                <div class="small text-muted mb-2">Ảnh xem trước:</div>
                                <img id="previewImg" src="" alt="Preview" class="rounded-3 shadow-sm border object-fit-cover" style="max-height: 180px; max-width: 100%;">
                            </div>
                        </div>

                        <!-- Nút hành động -->
                        <div class="d-flex gap-2 pt-2 border-top">
                            <button type="submit" class="btn btn-primary rounded-pill px-4 shadow-sm">
                                <i class="fas fa-save me-1"></i> Thêm sản phẩm
                            </button>
                            <a href="<c:url value='/admin/products'/>" class="btn btn-light rounded-pill px-4 border">
                                <i class="fas fa-arrow-left me-1"></i> Quay lại
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Preview ảnh và kiểm tra định dạng
        const imageInput = document.getElementById('imageInput');
        const previewContainer = document.getElementById('previewContainer');
        const previewImg = document.getElementById('previewImg');
        const imageFeedback = document.getElementById('imageFeedback');

        imageInput.addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                // Kiểm tra định dạng
                const validTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp', 'image/gif'];
                if (!validTypes.includes(file.type)) {
                    imageInput.setCustomValidity('Chỉ chấp nhận tệp hình ảnh (jpg, png, webp, gif)!');
                    imageFeedback.textContent = 'Định dạng tệp không hợp lệ! Vui lòng chọn ảnh định dạng JPG, PNG, WEBP.';
                    previewContainer.classList.add('d-none');
                    return;
                }
                // Kiểm tra kích thước (tối đa 10MB)
                if (file.size > 10 * 1024 * 1024) {
                    imageInput.setCustomValidity('Kích thước ảnh tối đa 10MB!');
                    imageFeedback.textContent = 'Kích thước tệp ảnh quá lớn (tối đa 10MB).';
                    previewContainer.classList.add('d-none');
                    return;
                }
                imageInput.setCustomValidity('');
                const reader = new FileReader();
                reader.onload = function(ev) {
                    previewImg.src = ev.target.result;
                    previewContainer.classList.remove('d-none');
                };
                reader.readAsDataURL(file);
            } else {
                previewContainer.classList.add('d-none');
            }
        });

        // Bootstrap Validation
        (() => {
            'use strict';
            const forms = document.querySelectorAll('.needs-validation');
            Array.from(forms).forEach(form => {
                form.addEventListener('submit', event => {
                    const priceInput = document.getElementById('price');
                    if (priceInput && (parseFloat(priceInput.value) <= 0 || isNaN(parseFloat(priceInput.value)))) {
                        priceInput.setCustomValidity('Giá phải lớn hơn 0');
                    } else if (priceInput) {
                        priceInput.setCustomValidity('');
                    }

                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        })();
    </script>
</body>
</html>
