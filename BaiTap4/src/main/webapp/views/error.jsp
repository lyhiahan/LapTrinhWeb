<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Lỗi - ShoppingAdmin</title>
<link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
<style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body {
        font-family: 'Outfit', sans-serif;
        background: #f8fafc;
        color: #0f172a;
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: 100vh;
    }
    .error-container {
        text-align: center;
        padding: 40px;
        max-width: 500px;
    }
    .error-icon {
        font-size: 4rem;
        color: #dc2626;
        margin-bottom: 20px;
    }
    .error-title {
        font-size: 1.8rem;
        font-weight: 700;
        margin-bottom: 12px;
        color: #0f172a;
    }
    .error-message {
        color: #64748b;
        font-size: 1rem;
        margin-bottom: 24px;
        line-height: 1.6;
    }
    .error-detail {
        background: #fef2f2;
        border: 1px solid #fecaca;
        border-radius: 12px;
        padding: 16px;
        font-size: 0.85rem;
        color: #b91c1c;
        text-align: left;
        margin-bottom: 24px;
        word-break: break-word;
    }
    .btn-back {
        display: inline-block;
        padding: 12px 28px;
        background: linear-gradient(90deg, #4f46e5, #7c3aed);
        color: #fff;
        border-radius: 10px;
        text-decoration: none;
        font-weight: 600;
        font-size: 0.95rem;
        transition: all 0.3s;
    }
    .btn-back:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(79,70,229,0.25);
    }
</style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">
            <i class="fas fa-exclamation-triangle"></i>
        </div>
        <h1 class="error-title">Đã xảy ra lỗi!</h1>
        <p class="error-message">
            Xin lỗi, đã có sự cố xảy ra. Vui lòng thử lại sau hoặc quay về trang chủ.
        </p>
        <% if (exception != null) { %>
        <div class="error-detail">
            <strong>Chi tiết:</strong> <%= exception.getMessage() %>
        </div>
        <% } %>
        <% 
            Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
            if (statusCode != null) { 
        %>
        <div class="error-detail">
            <strong>Mã lỗi:</strong> <%= statusCode %>
        </div>
        <% } %>
        <a href="${pageContext.request.contextPath}/home" class="btn-back">
            <i class="fas fa-home" style="margin-right: 8px;"></i>Về trang chủ
        </a>
    </div>
</body>
</html>
