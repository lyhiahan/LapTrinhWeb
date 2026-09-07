<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - ShoppingAdmin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/style.css">
</head>
<body>
<div class="container">
    <h2>Đăng Nhập</h2>
    <p class="subtitle">Truy cập vào tài khoản hệ thống của bạn</p>

    <c:if test="${param.activated == 'true'}">
        <div class="alert-success-card">
            Tài khoản đã được kích hoạt thành công! Hãy đăng nhập.
        </div>
    </c:if>

    <c:if test="${param.resetSuccess == 'true'}">
        <div class="alert-success-card">
            Mật khẩu đã được đặt lại thành công! Hãy đăng nhập bằng mật khẩu mới.
        </div>
    </c:if>

    <c:if test="${alert != null}">
        <div class="alert-card">
            ${alert}
        </div>
    </c:if>

    <c:if test="${inactiveEmail != null}">
        <div style="text-align: center; margin-bottom: 15px;">
            <a href="${pageContext.request.contextPath}/verify-otp?email=${inactiveEmail}" style="font-size: 0.9rem;">
                Bấm vào đây để xác thực OTP
            </a>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
            <label for="username">Tài khoản</label>
            <input type="text" id="username" name="username" class="input-control" placeholder="Nhập tên tài khoản" required autocomplete="username">
        </div>
        
        <div class="form-group">
            <label for="password">Mật khẩu</label>
            <input type="password" id="password" name="password" class="input-control" placeholder="Nhập mật khẩu" required autocomplete="current-password">
        </div>

        <div class="form-options">
            <label class="checkbox-label">
                <input type="checkbox" name="remember">
                <span>Nhớ tài khoản</span>
            </label>
            <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
        </div>

        <button type="submit" class="btn">Đăng nhập</button>

        <p class="footer-text">
            Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
        </p>
    </form>
</div>
</body>
</html>
