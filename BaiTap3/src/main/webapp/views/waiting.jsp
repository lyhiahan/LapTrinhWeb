<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang cá nhân - ShoppingAdmin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/style.css">
</head>
<body>
<div class="container container-wide">
    <div class="avatar-section">
        <div class="avatar-circle">
            <c:choose>
                <c:when test="${not empty account.fullName}">
                    ${fn:substring(account.fullName, 0, 1).toUpperCase()}
                </c:when>
                <c:otherwise>
                    ${fn:substring(account.userName, 0, 1).toUpperCase()}
                </c:otherwise>
            </c:choose>
        </div>
        <h2>${account.fullName}</h2>
        
        <c:choose>
            <c:when test="${account.roleid == 1}">
                <span class="badge badge-admin">Quản trị viên</span>
            </c:when>
            <c:otherwise>
                <span class="badge badge-member">Thành viên</span>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="profile-grid">
        <div class="info-box">
            <div class="label">Tài khoản</div>
            <div class="value">${account.userName}</div>
        </div>
        
        <div class="info-box">
            <div class="label">Email</div>
            <div class="value">${account.email}</div>
        </div>
        
        <div class="info-box">
            <div class="label">Số điện thoại</div>
            <div class="value">${account.phone}</div>
        </div>
        
        <div class="info-box">
            <div class="label">Ngày tham gia</div>
            <div class="value">${account.createdDate}</div>
        </div>
    </div>

    <div style="display: flex; gap: 15px; flex-wrap: wrap;">
        <a href="${pageContext.request.contextPath}/home" class="btn" style="text-align: center; display: block; flex: 1;">
            Trang chủ
        </a>
        <a href="${pageContext.request.contextPath}/admin/categories" class="btn" style="text-align: center; display: block; flex: 1;">
            Quản lý danh mục
        </a>
        <a href="${pageContext.request.contextPath}/admin/products" class="btn" style="text-align: center; display: block; flex: 1;">
            Quản lý sản phẩm
        </a>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary" style="text-align: center; display: block; flex: 1;">
            Đăng xuất
        </a>
    </div>
</div>
</body>
</html>
