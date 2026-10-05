<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <title>Xác thực OTP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <style>
        body { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; display: flex; align-items: center; justify-content: center; font-family: 'Segoe UI', Tahoma, sans-serif; }
        .otp-card { background: #fff; border-radius: 15px; box-shadow: 0 15px 35px rgba(0,0,0,0.2); padding: 40px; width: 420px; }
        .otp-card h2 { color: #333; font-weight: 700; text-align: center; margin-bottom: 10px; }
        .otp-card .form-control { border-radius: 10px; padding: 15px; font-size: 24px; text-align: center; letter-spacing: 10px; }
        .otp-card .btn-primary { border-radius: 10px; padding: 12px; font-weight: 600; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border: none; }
    </style>
</head>
<body>
    <div class="otp-card">
        <h2><i class="fas fa-shield-alt text-primary"></i></h2>
        <h2>Xác thực OTP</h2>
        <p class="text-center text-muted mb-4">
            Nhập mã OTP 6 chữ số đã gửi tới email:<br>
            <strong>${email}</strong>
        </p>

        <c:if test="${not empty alert}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle"></i> ${alert}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle"></i> ${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/verify-otp" method="post">
            <input type="hidden" name="email" value="${email}">
            <div class="mb-4">
                <input type="text" class="form-control" name="otp" maxlength="6" placeholder="______" required pattern="[0-9]{6}">
            </div>
            <button type="submit" class="btn btn-primary w-100 mb-3">
                <i class="fas fa-check"></i> Xác Nhận OTP
            </button>
        </form>

        <form action="${pageContext.request.contextPath}/verify-otp" method="post" class="text-center">
            <input type="hidden" name="email" value="${email}">
            <input type="hidden" name="action" value="resend">
            <button type="submit" class="btn btn-link text-decoration-none">
                <i class="fas fa-redo"></i> Gửi lại mã OTP
            </button>
        </form>

        <hr>
        <div class="text-center">
            <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">
                <i class="fas fa-arrow-left"></i> Quay lại đăng nhập
            </a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
