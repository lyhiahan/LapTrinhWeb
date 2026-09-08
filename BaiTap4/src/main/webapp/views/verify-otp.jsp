<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực OTP - ShoppingAdmin</title>
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
            <i class="fas fa-shield-halved fa-2x"></i>
        </div>
        <h4 class="fw-bold text-dark mb-1">Xác Thực Mã OTP</h4>
        <p class="text-muted small">Mã 6 chữ số đã được gửi tới email:<br><strong class="text-primary">${email}</strong></p>
    </div>

    <!-- Alert thông báo -->
    <c:if test="${not empty alert}">
        <div class="alert alert-danger alert-dismissible fade show rounded-3 small py-2" role="alert">
            <i class="fas fa-circle-exclamation me-1"></i> ${alert}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty success}">
        <div class="alert alert-success alert-dismissible fade show rounded-3 small py-2" role="alert">
            <i class="fas fa-circle-check me-1"></i> ${success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/verify-otp" method="post" class="needs-validation" novalidate>
        <input type="hidden" name="email" value="${email}"/>

        <div class="mb-4">
            <label for="otp" class="form-label fw-semibold text-dark small text-center d-block">Nhập mã xác nhận (6 chữ số) <span class="text-danger">*</span></label>
            <input type="text"
                   id="otp"
                   name="otp"
                   class="form-control text-center fw-bold fs-3 py-2"
                   placeholder="------"
                   pattern="^[0-9]{6}$"
                   maxlength="6"
                   style="letter-spacing: 8px;"
                   required
                   autocomplete="one-time-code">
            <div class="invalid-feedback text-center">Vui lòng nhập đúng 6 chữ số mã OTP.</div>
            <div class="form-text small text-muted text-center mt-2">
                Mã xác thực có hiệu lực trong vòng <b>5 phút</b>.
            </div>
        </div>

        <button type="submit" class="btn btn-gradient-primary w-100 py-2 rounded-pill shadow-sm mb-3">
            <i class="fas fa-check-circle me-2"></i>Kích Hoạt Tài Khoản
        </button>
    </form>

    <div class="text-center pt-2 border-top">
        <span class="small text-muted">Chưa nhận được mã? </span>
        <form action="${pageContext.request.contextPath}/verify-otp" method="post" class="d-inline">
            <input type="hidden" name="email" value="${email}"/>
            <input type="hidden" name="action" value="resend"/>
            <button type="submit" class="btn btn-link p-0 text-primary text-decoration-none fw-semibold small">
                Gửi lại mã OTP
            </button>
        </form>
    </div>

    <p class="text-center text-muted small mt-3 mb-0">
        <a href="${pageContext.request.contextPath}/login" class="text-secondary text-decoration-none small">
            <i class="fas fa-arrow-left me-1"></i> Quay lại Đăng nhập
        </a>
    </p>
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
