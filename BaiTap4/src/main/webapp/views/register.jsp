<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký tài khoản - ShoppingAdmin</title>
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
            padding: 24px 16px;
        }
        .auth-card {
            background: #ffffff;
            border-radius: 24px;
            box-shadow: 0 20px 40px rgba(15, 23, 42, 0.08);
            border: 1px solid #e2e8f0;
            width: 100%;
            max-width: 480px;
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
        <h4 class="fw-bold text-dark mb-1">Tạo Tài Khoản</h4>
        <p class="text-muted small">Đăng ký để trải nghiệm mua sắm và các tính năng tuyệt vời</p>
    </div>

    <!-- Alert error từ server -->
    <c:if test="${not empty alert}">
        <div class="alert alert-danger alert-dismissible fade show rounded-3 small py-2" role="alert">
            <i class="fas fa-circle-exclamation me-1"></i> ${alert}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post" class="needs-validation" novalidate id="registerForm">
        <!-- Username -->
        <div class="mb-3">
            <label for="username" class="form-label fw-semibold text-dark small">Tên tài khoản <span class="text-danger">*</span></label>
            <div class="input-group has-validation">
                <span class="input-group-text bg-light text-muted border-end-0"><i class="fas fa-user"></i></span>
                <input type="text"
                       id="username"
                       name="username"
                       class="form-control border-start-0 ps-1"
                       placeholder="Từ 3-30 ký tự, không dấu"
                       value="${not empty username ? username : (not empty param.username ? param.username : '')}"
                       pattern="^[a-zA-Z0-9_]{3,30}$"
                       required
                       autocomplete="username">
                <div class="invalid-feedback">
                    Tên tài khoản từ 3-30 ký tự (chỉ bao gồm chữ cái, chữ số và gạch dưới).
                </div>
            </div>
        </div>

        <!-- Họ tên -->
        <div class="mb-3">
            <label for="fullname" class="form-label fw-semibold text-dark small">Họ và tên đầy đủ <span class="text-danger">*</span></label>
            <div class="input-group has-validation">
                <span class="input-group-text bg-light text-muted border-end-0"><i class="fas fa-id-card"></i></span>
                <input type="text"
                       id="fullname"
                       name="fullname"
                       class="form-control border-start-0 ps-1"
                       placeholder="Ví dụ: Nguyễn Văn A"
                       value="${not empty fullname ? fullname : (not empty param.fullname ? param.fullname : '')}"
                       minlength="2"
                       maxlength="50"
                       required
                       autocomplete="name">
                <div class="invalid-feedback">Vui lòng nhập họ và tên hợp lệ (từ 2 đến 50 ký tự).</div>
            </div>
        </div>

        <!-- Email -->
        <div class="mb-3">
            <label for="email" class="form-label fw-semibold text-dark small">Địa chỉ Email <span class="text-danger">*</span></label>
            <div class="input-group has-validation">
                <span class="input-group-text bg-light text-muted border-end-0"><i class="fas fa-envelope"></i></span>
                <input type="email"
                       id="email"
                       name="email"
                       class="form-control border-start-0 ps-1"
                       placeholder="name@example.com"
                       value="${not empty email ? email : (not empty param.email ? param.email : '')}"
                       required
                       autocomplete="email">
                <div class="invalid-feedback">Vui lòng nhập địa chỉ email hợp lệ để nhận mã OTP.</div>
            </div>
        </div>

        <!-- Phone -->
        <div class="mb-3">
            <label for="phone" class="form-label fw-semibold text-dark small">Số điện thoại <span class="text-danger">*</span></label>
            <div class="input-group has-validation">
                <span class="input-group-text bg-light text-muted border-end-0"><i class="fas fa-phone"></i></span>
                <input type="tel"
                       id="phone"
                       name="phone"
                       class="form-control border-start-0 ps-1"
                       placeholder="Ví dụ: 0912345678"
                       value="${not empty phone ? phone : (not empty param.phone ? param.phone : '')}"
                       pattern="^(0[3|5|7|8|9])[0-9]{8}$"
                       required
                       autocomplete="tel">
                <div class="invalid-feedback">Số điện thoại phải gồm 10 chữ số hợp lệ của Việt Nam (đầu 03, 05, 07, 08, 09).</div>
            </div>
        </div>

        <!-- Mật khẩu -->
        <div class="mb-3">
            <label for="password" class="form-label fw-semibold text-dark small">Mật khẩu <span class="text-danger">*</span></label>
            <div class="input-group has-validation">
                <span class="input-group-text bg-light text-muted border-end-0"><i class="fas fa-lock"></i></span>
                <input type="password"
                       id="password"
                       name="password"
                       class="form-control border-start-0 ps-1"
                       placeholder="Tối thiểu 6 ký tự"
                       minlength="6"
                       required
                       autocomplete="new-password">
                <div class="invalid-feedback">Mật khẩu phải có ít nhất 6 ký tự.</div>
            </div>
        </div>

        <!-- Nhập lại Mật khẩu -->
        <div class="mb-4">
            <label for="confirmPassword" class="form-label fw-semibold text-dark small">Nhập lại mật khẩu <span class="text-danger">*</span></label>
            <div class="input-group has-validation">
                <span class="input-group-text bg-light text-muted border-end-0"><i class="fas fa-shield-halved"></i></span>
                <input type="password"
                       id="confirmPassword"
                       name="confirmPassword"
                       class="form-control border-start-0 ps-1"
                       placeholder="Nhập lại mật khẩu đã chọn"
                       required
                       autocomplete="new-password">
                <div class="invalid-feedback" id="confirmFeedback">Mật khẩu xác nhận không khớp!</div>
            </div>
        </div>

        <!-- Submit Button -->
        <button type="submit" class="btn btn-gradient-primary w-100 py-2 rounded-pill shadow-sm mb-3">
            <i class="fas fa-user-plus me-2"></i>Đăng Ký Tài Khoản
        </button>

        <p class="text-center text-muted small mb-0">
            Đã có tài khoản?
            <a href="${pageContext.request.contextPath}/login" class="text-primary text-decoration-none fw-semibold">
                Đăng nhập
            </a>
        </p>
    </form>
</div>

<!-- Validation Script -->
<script>
    const form = document.getElementById('registerForm');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const confirmFeedback = document.getElementById('confirmFeedback');

    function checkPasswordMatch() {
        if (confirmPassword.value && password.value !== confirmPassword.value) {
            confirmPassword.setCustomValidity('Mật khẩu không khớp');
            confirmFeedback.textContent = 'Mật khẩu xác nhận không khớp với mật khẩu đã nhập.';
        } else {
            confirmPassword.setCustomValidity('');
        }
    }

    password.addEventListener('input', checkPasswordMatch);
    confirmPassword.addEventListener('input', checkPasswordMatch);

    form.addEventListener('submit', function(event) {
        checkPasswordMatch();
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    }, false);
</script>
<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
