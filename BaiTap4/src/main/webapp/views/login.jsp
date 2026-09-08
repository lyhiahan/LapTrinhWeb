<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - ShoppingAdmin</title>
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
        <a href="${pageContext.request.contextPath}/home" class="d-inline-flex align-items-center gap-2 text-decoration-none text-primary mb-2">
            <i class="fas fa-bag-shopping fa-2x"></i>
            <span class="fs-4 fw-bold text-dark">Shopping<span class="text-primary">Admin</span></span>
        </a>
        <h4 class="fw-bold text-dark mb-1">Đăng Nhập</h4>
        <p class="text-muted small">Truy cập vào tài khoản hệ thống của bạn</p>
    </div>

    <!-- Thông báo kết quả -->
    <c:if test="${param.activated == 'true'}">
        <div class="alert alert-success alert-dismissible fade show rounded-3 small py-2" role="alert">
            <i class="fas fa-check-circle me-1"></i> Tài khoản đã được kích hoạt thành công! Vui lòng đăng nhập.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${param.resetSuccess == 'true'}">
        <div class="alert alert-success alert-dismissible fade show rounded-3 small py-2" role="alert">
            <i class="fas fa-check-circle me-1"></i> Mật khẩu đã được đặt lại thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty alert}">
        <div class="alert alert-danger alert-dismissible fade show rounded-3 small py-2" role="alert">
            <i class="fas fa-circle-exclamation me-1"></i> ${alert}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${inactiveEmail != null}">
        <div class="text-center mb-3">
            <a href="${pageContext.request.contextPath}/verify-otp?email=${inactiveEmail}" class="badge bg-warning-subtle text-warning-emphasis text-decoration-none p-2 rounded-pill">
                <i class="fas fa-envelope-circle-check me-1"></i> Bấm vào đây để xác thực OTP ngay
            </a>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post" class="needs-validation" novalidate>
        <!-- Username -->
        <div class="mb-3">
            <label for="username" class="form-label fw-semibold text-dark small">Tên tài khoản</label>
            <div class="input-group has-validation">
                <span class="input-group-text bg-light text-muted border-end-0"><i class="fas fa-user"></i></span>
                <input type="text"
                       id="username"
                       name="username"
                       class="form-control border-start-0 ps-1"
                       placeholder="Nhập tên tài khoản"
                       value="${not empty username ? username : (not empty param.username ? param.username : '')}"
                       required
                       autocomplete="username">
                <div class="invalid-feedback">Vui lòng nhập tên tài khoản.</div>
            </div>
        </div>

        <!-- Password -->
        <div class="mb-3">
            <label for="password" class="form-label fw-semibold text-dark small">Mật khẩu</label>
            <div class="input-group has-validation">
                <span class="input-group-text bg-light text-muted border-end-0"><i class="fas fa-lock"></i></span>
                <input type="password"
                       id="password"
                       name="password"
                       class="form-control border-start-0 ps-1"
                       placeholder="Nhập mật khẩu"
                       required
                       autocomplete="current-password">
                <div class="invalid-feedback">Vui lòng nhập mật khẩu.</div>
            </div>
        </div>

        <!-- Remember me & Forgot Password -->
        <div class="d-flex justify-content-between align-items-center mb-4 small">
            <div class="form-check">
                <input class="form-check-input" type="checkbox" name="remember" id="rememberMe">
                <label class="form-check-label text-muted" for="rememberMe">
                    Nhớ tài khoản
                </label>
            </div>
            <a href="${pageContext.request.contextPath}/forgot-password" class="text-decoration-none text-primary fw-medium">
                Quên mật khẩu?
            </a>
        </div>

        <!-- Submit Button -->
        <button type="submit" class="btn btn-gradient-primary w-100 py-2 rounded-pill shadow-sm mb-3">
            <i class="fas fa-arrow-right-to-bracket me-2"></i>Đăng nhập
        </button>

        <p class="text-center text-muted small mb-0">
            Chưa có tài khoản?
            <a href="${pageContext.request.contextPath}/register" class="text-primary text-decoration-none fw-semibold">
                Đăng ký ngay
            </a>
        </p>
    </form>
</div>

<!-- Bootstrap 5 Validation Script -->
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
<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
