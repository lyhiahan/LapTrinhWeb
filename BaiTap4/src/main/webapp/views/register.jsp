<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký thành viên - ShoppingAdmin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/style.css">
</head>
<body>
<div class="container">
    <h2>Tạo Tài Khoản</h2>
    <p class="subtitle">Đăng ký để tham gia hệ thống của chúng tôi</p>

    <c:if test="${alert != null}">
        <div class="alert-card">
            ${alert}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <div class="form-group">
            <label for="username">Tài khoản</label>
            <input type="text" id="username" name="username" class="input-control" placeholder="Nhập tên tài khoản" required autocomplete="username">
        </div>

        <div class="form-group">
            <label for="fullname">Họ tên</label>
            <input type="text" id="fullname" name="fullname" class="input-control" placeholder="Nhập họ và tên đầy đủ" required autocomplete="name">
        </div>

        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" class="input-control" placeholder="Nhập địa chỉ email" required autocomplete="email">
        </div>

        <div class="form-group">
            <label for="phone">Số điện thoại</label>
            <input type="tel" id="phone" name="phone" class="input-control" placeholder="Nhập số điện thoại" required autocomplete="tel">
        </div>
        
        <div class="form-group">
            <label for="password">Mật khẩu</label>
            <input type="password" id="password" name="password" class="input-control" placeholder="Nhập mật khẩu bảo mật" required autocomplete="new-password">
        </div>

        <button type="submit" class="btn">Tạo tài khoản</button>

        <p class="footer-text">
            Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
        </p>
    </form>
</div>
</body>
</html>
