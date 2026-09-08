<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu - ShoppingAdmin</title>
    <!-- Google Fonts & Font Awesome -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <!-- Bootstrap 5.3.3 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Outfit', sans-serif;
            background: linear-gradient(135deg, #f0f4ff 0%, #e2e8f0 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        .auth-card {
            background: #ffffff;
            border-radius: 24px;
            box-shadow: 0 20px 40px rgba(15, 23, 42, 0.08);
            border: 1px solid #e2e8f0;
            width: 100%;
            max-width: 440px;
            overflow: hidden;
        }
        .btn-gradient-primary {
            background: linear-gradient(90deg, #4f46e5 0%, #7c3aed 100%);
            color: #ffffff;
            border: none;
            font-weight: 600;
            transition: all 0.3s;
        }
        .btn-gradient-primary:hover {
            color: #ffffff;
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(79, 70, 229, 0.35);
        }
    </style>
</head>
<body>

<div class="auth-card p-4 p-sm-5">
    <div class="text-center mb-4">
        <div class="d-inline-flex align-items-center justify-content-center bg-primary-subtle text-primary rounded-circle mb-3" style="width: 60px; height: 60px;">
            <i class="fas fa-key fa-2x"></i>
        </div>
        <h4 class="fw-bold text-dark mb-1">Quên Mật Khẩu</h4>
        <p class="text-muted small">Nhập email đã đăng ký để nhận mã OTP lấy lại quyền truy cập</p>
    </div>

    <!-- Alert error từ server -->
    <c:if test="${not empty alert}">
        <div class="alert alert-danger alert-dismissible fade show rounded-3 small py-2" role="alert">
            <i class="fas fa-circle-exclamation me-1"></i> ${alert}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/forgot-password" method="post" class="needs-validation" novalidate>
        <div class="mb-4">
            <label for="email" class="form-label fw-semibold text-dark small">Địa chỉ Email của bạn <span class="text-danger">*</span></label>
            <div class="input-group has-validation">
                <span class="input-group-text bg-light text-muted border-end-0"><i class="fas fa-envelope"></i></span>
                <input type="email"
                       id="email"
                       name="email"
                       class="form-control border-start-0 ps-1"
                       placeholder="Nhập email của tài khoản..."
                       value="${not empty param.email ? param.email : ''}"
                       required
                       autocomplete="email">
                <div class="invalid-feedback">Vui lòng nhập địa chỉ email hợp lệ.</div>
            </div>
            <div class="form-text small text-muted mt-2">
                Hệ thống sẽ gửi mã xác nhận OTP gồm 6 chữ số đến hộp thư của bạn.
            </div>
        </div>

        <button type="submit" class="btn btn-gradient-primary w-100 py-2 rounded-pill shadow-sm mb-3">
            <i class="fas fa-paper-plane me-2"></i>Gửi Mã OTP
        </button>

        <p class="text-center text-muted small mb-0">
            <a href="${pageContext.request.contextPath}/login" class="text-primary text-decoration-none fw-semibold">
                <i class="fas fa-arrow-left me-1"></i> Quay lại Đăng nhập
            </a>
        </p>
    </form>
</div>

<script>
    (() => {
        'use strict';
        const forms = document.querySelectorAll('.needs-validation');
        Array.from(forms).forEach(form => {
            form.addEventListener('submit', event => {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    })();
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
