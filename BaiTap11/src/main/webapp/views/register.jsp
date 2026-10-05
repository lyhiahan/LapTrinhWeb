<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng ký</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <style>
        body { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; display: flex; align-items: center; justify-content: center; font-family: 'Segoe UI', Tahoma, sans-serif; }
        .register-card { background: #fff; border-radius: 15px; box-shadow: 0 15px 35px rgba(0,0,0,0.2); padding: 40px; width: 480px; }
        .register-card h2 { color: #333; font-weight: 700; text-align: center; margin-bottom: 30px; }
        .register-card .form-control { border-radius: 10px; padding: 10px 15px; }
        .register-card .btn-primary { border-radius: 10px; padding: 12px; font-weight: 600; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border: none; }
    </style>
</head>
<body>
    <div class="register-card">
        <h2><i class="fas fa-user-plus text-primary"></i> Đăng Ký</h2>

        <c:if test="${not empty alert}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle"></i> <c:out value="${alert}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <input type="hidden" name="_csrf" value="${fn:escapeXml(csrfToken)}">
            <div class="mb-3">
                <label class="form-label"><i class="fas fa-user"></i> Tên đăng nhập *</label>
                <input type="text" class="form-control" name="username" value="${fn:escapeXml(username)}" maxlength="50" placeholder="Nhập tên đăng nhập" required>
            </div>
            <div class="mb-3">
                <label class="form-label"><i class="fas fa-id-card"></i> Họ tên *</label>
                <input type="text" class="form-control" name="fullname" value="${fn:escapeXml(fullname)}" maxlength="50" placeholder="Nhập họ tên đầy đủ" required>
            </div>
            <div class="mb-3">
                <label class="form-label"><i class="fas fa-envelope"></i> Email *</label>
                <input type="email" class="form-control" name="email" value="${fn:escapeXml(email)}" maxlength="150" placeholder="Nhập email" required>
            </div>
            <div class="mb-3">
                <label class="form-label"><i class="fas fa-phone"></i> Số điện thoại</label>
                <input type="text" class="form-control" name="phone" value="${fn:escapeXml(phone)}" maxlength="15" placeholder="Nhập số điện thoại">
            </div>
            <div class="mb-3">
                <label class="form-label"><i class="fas fa-lock"></i> Mật khẩu *</label>
                <input type="password" class="form-control" name="password" maxlength="128" placeholder="Nhập mật khẩu" required>
            </div>
            <div class="mb-3">
                <label class="form-label"><i class="fas fa-lock"></i> Xác nhận mật khẩu *</label>
                <input type="password" class="form-control" name="confirmPassword" maxlength="128" placeholder="Nhập lại mật khẩu" required>
            </div>
            <button type="submit" class="btn btn-primary w-100 mb-3">
                <i class="fas fa-user-plus"></i> Đăng Ký
            </button>
        </form>

        <div class="text-center">
            <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">
                <i class="fas fa-sign-in-alt"></i> Đã có tài khoản? Đăng nhập
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
