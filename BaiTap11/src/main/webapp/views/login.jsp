<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <style>
        body { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; display: flex; align-items: center; justify-content: center; font-family: 'Segoe UI', Tahoma, sans-serif; }
        .login-card { background: #fff; border-radius: 15px; box-shadow: 0 15px 35px rgba(0,0,0,0.2); padding: 40px; width: 420px; }
        .login-card h2 { color: #333; font-weight: 700; text-align: center; margin-bottom: 30px; }
        .login-card .form-control { border-radius: 10px; padding: 12px 15px; }
        .login-card .btn-primary { border-radius: 10px; padding: 12px; font-weight: 600; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border: none; }
        .login-card .btn-primary:hover { opacity: 0.9; }
    </style>
</head>
<body>
    <div class="login-card">
        <h2><i class="fas fa-play-circle text-primary"></i> Đăng Nhập</h2>

        <c:if test="${not empty alert}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle"></i> <c:out value="${alert}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle"></i> <c:out value="${success}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <c:if test="${not empty inactiveEmail}">
            <c:url var="verifyOtpUrl" value="/verify-otp">
                <c:param name="email" value="${inactiveEmail}"/>
            </c:url>
            <div class="alert alert-warning">
                <a href="${fn:escapeXml(verifyOtpUrl)}" class="alert-link">
                    <i class="fas fa-key"></i> Nhấn vào đây để xác thực OTP
                </a>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="hidden" name="_csrf" value="${fn:escapeXml(csrfToken)}">
            <div class="mb-3">
                <label class="form-label"><i class="fas fa-user"></i> Tên đăng nhập</label>
                <input type="text" class="form-control" name="username" value="${fn:escapeXml(username)}" placeholder="Nhập tên đăng nhập" required>
            </div>
            <div class="mb-3">
                <label class="form-label"><i class="fas fa-lock"></i> Mật khẩu</label>
                <input type="password" class="form-control" name="password" placeholder="Nhập mật khẩu" required>
            </div>
            <button type="submit" class="btn btn-primary w-100 mb-3">
                <i class="fas fa-sign-in-alt"></i> Đăng Nhập
            </button>
        </form>

        <div class="text-center">
            <a href="${pageContext.request.contextPath}/register" class="text-decoration-none">
                <i class="fas fa-user-plus"></i> Chưa có tài khoản? Đăng ký ngay
            </a>
        </div>

        <hr>
        <p class="text-center text-muted" style="font-size:12px;">
            Họ tên: Lý Gia Hân | MSSV: 24133016 | Mã đề: Đề số 03
        </p>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
