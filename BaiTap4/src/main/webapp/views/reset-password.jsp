<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu - ShoppingAdmin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/style.css">
</head>
<body>
<div class="container">
    <h2>Đặt Lại Mật Khẩu</h2>
    <p class="subtitle">Nhập mã OTP và mật khẩu mới cho tài khoản <strong>${email}</strong></p>

    <c:if test="${alert != null}">
        <div class="alert-card">
            ${alert}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/reset-password" method="post">
        <input type="hidden" name="email" value="${email}"/>

        <div class="form-group">
            <label for="otp">Mã OTP</label>
            <input type="text" id="otp" name="otp" class="input-control" placeholder="Nhập mã OTP 6 chữ số" required maxlength="6"
                   style="text-align: center; font-size: 1.3rem; letter-spacing: 6px; font-weight: 700;">
        </div>

        <div class="form-group">
            <label for="newPassword">Mật khẩu mới</label>
            <input type="password" id="newPassword" name="newPassword" class="input-control" placeholder="Nhập mật khẩu mới" required autocomplete="new-password">
        </div>

        <div class="form-group">
            <label for="confirmPassword">Xác nhận mật khẩu</label>
            <input type="password" id="confirmPassword" name="confirmPassword" class="input-control" placeholder="Nhập lại mật khẩu mới" required autocomplete="new-password">
        </div>

        <button type="submit" class="btn">Đặt lại mật khẩu</button>

        <p class="footer-text">
            <a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
        </p>
    </form>
</div>
</body>
</html>
