<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu - ShoppingAdmin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/style.css">
</head>
<body>
<div class="container">
    <h2>Quên Mật Khẩu</h2>
    <p class="subtitle">Nhập email để nhận mã xác thực OTP</p>

    <c:if test="${alert != null}">
        <div class="alert-card">
            ${alert}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/forgot-password" method="post">
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" class="input-control" placeholder="Nhập địa chỉ email đã đăng ký" required autocomplete="email">
        </div>

        <button type="submit" class="btn">Gửi mã xác nhận</button>

        <p class="footer-text">
            <a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
        </p>
    </form>
</div>
</body>
</html>
