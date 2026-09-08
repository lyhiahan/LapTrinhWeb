<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chỉnh Sửa Danh Mục - Admin</title>
</head>
<body>
    <div class="mb-4">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-1 small">
                <li class="breadcrumb-item"><a href="<c:url value='/admin/categories'/>" class="text-decoration-none">Danh mục</a></li>
                <li class="breadcrumb-item active" aria-current="page">Chỉnh sửa</li>
            </ol>
        </nav>
        <h3 class="fw-bold mb-1"><i class="fas fa-pen-to-square text-primary me-2"></i>Chỉnh Sửa Danh Mục</h3>
        <p class="text-muted small mb-0">Cập nhật thông tin phân loại sản phẩm #${category.id}</p>
    </div>

    <!-- Alert error từ server nếu có -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-triangle-exclamation me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row">
        <div class="col-lg-6 col-md-8">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-4">
                    <form action="<c:url value='/admin/category/update'/>" method="post" class="needs-validation" novalidate>
                        <!-- ID ẩn -->
                        <input type="hidden" name="id" value="${category.id}"/>

                        <div class="mb-3">
                            <label class="form-label fw-semibold text-muted small">Mã danh mục (ID)</label>
                            <input type="text" class="form-control bg-light" value="${category.id}" readonly disabled>
                        </div>

                        <div class="mb-3">
                            <label for="categoryName" class="form-label fw-semibold text-dark">
                                <i class="fas fa-tag text-muted me-1"></i> Tên danh mục hàng hóa <span class="text-danger">*</span>
                            </label>
                            <input type="text"
                                   class="form-control"
                                   id="categoryName"
                                   name="name"
                                   value="${category.name}"
                                   placeholder="Nhập tên danh mục..."
                                   minlength="2"
                                   maxlength="100"
                                   required>
                            <div class="invalid-feedback">
                                Vui lòng nhập tên danh mục hợp lệ (từ 2 đến 100 ký tự).
                            </div>
                        </div>

                        <div class="d-flex gap-2 pt-2">
                            <button type="submit" class="btn btn-primary rounded-pill px-4 shadow-sm">
                                <i class="fas fa-save me-1"></i> Cập nhật thay đổi
                            </button>
                            <a href="<c:url value='/admin/categories'/>" class="btn btn-light rounded-pill px-4 border">
                                <i class="fas fa-arrow-left me-1"></i> Quay lại
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        (() => {
            'use strict';
            const forms = document.querySelectorAll('.needs-validation');
            Array.from(forms).forEach(form => {
                form.addEventListener('submit', event => {
                    const nameInput = document.getElementById('categoryName');
                    if (nameInput && nameInput.value.trim().length < 2) {
                        nameInput.setCustomValidity('Tên danh mục phải có ít nhất 2 ký tự!');
                    } else if (nameInput) {
                        nameInput.setCustomValidity('');
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
