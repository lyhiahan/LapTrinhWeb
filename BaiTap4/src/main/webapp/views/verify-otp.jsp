<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực OTP - ShoppingAdmin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/style.css">
</head>
<body>
<div class="container">
    <h2>Xác Thực OTP</h2>
    <p class="subtitle">Nhập mã OTP đã được gửi tới email <strong>${email}</strong></p>

    <c:if test="${alert != null}">
        <div class="alert-card">
            ${alert}
        </div>
    </c:if>

    <c:if test="${success != null}">
        <div class="alert-success-card">
            ${success}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/verify-otp" method="post">
        <input type="hidden" name="email" value="${email}"/>
        <div class="form-group">
            <label for="otp">Mã OTP</label>
            <input type="text" id="otp" name="otp" class="input-control" placeholder="Nhập mã OTP 6 chữ số" required maxlength="6"
                   style="text-align: center; font-size: 1.5rem; letter-spacing: 8px; font-weight: 700;">
        </div>

        <button type="submit" class="btn">Xác nhận</button>

        <p class="footer-text" style="margin-top: 20px;">
            Không nhận được mã?
        </p>
    </form>

    <form action="${pageContext.request.contextPath}/verify-otp" method="post" style="margin-top: 5px;">
        <input type="hidden" name="email" value="${email}"/>
        <input type="hidden" name="action" value="resend"/>
        <button type="submit" class="btn btn-secondary">Gửi lại mã OTP</button>
    </form>

    <p class="footer-text" style="margin-top: 15px;">
        <a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
    </p>
</div>
</body>
</html>
