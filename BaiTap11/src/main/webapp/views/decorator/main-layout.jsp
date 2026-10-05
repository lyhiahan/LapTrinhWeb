<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property='title'/> - WebProject_24133016</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <style>
        body { font-family: 'Segoe UI', Tahoma, sans-serif; background-color: #f8f9fa; }
        .navbar { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        .navbar-brand { font-weight: 700; font-size: 1.3rem; }
        .navbar .nav-link { color: rgba(255,255,255,0.9) !important; font-weight: 500; }
        .navbar .nav-link:hover { color: #fff !important; }
        .footer { background: #2c3e50; color: #ecf0f1; padding: 20px 0; margin-top: 40px; }
        .footer .info { font-size: 14px; }
    </style>
    <sitemesh:write property='head'/>
</head>
<body>
    <!-- Header & Menu -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
                <i class="fas fa-play-circle"></i> VideoApp
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/home">
                            <i class="fas fa-home"></i> Trang Chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/home">
                            <i class="fas fa-video"></i> Sản phẩm
                        </a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${sessionScope.account != null}">
                            <li class="nav-item">
                                <span class="nav-link">
                                    <i class="fas fa-user"></i> Xin chào, ${sessionScope.account.fullname}
                                </span>
                            </li>
                            <c:if test="${sessionScope.account.admin == true}">
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/home">
                                        <i class="fas fa-cog"></i> Trang quản trị
                                    </a>
                                </li>
                            </c:if>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                                    <i class="fas fa-sign-out-alt"></i> Đăng xuất
                                </a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                    <i class="fas fa-sign-in-alt"></i> Đăng nhập
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Content -->
    <div class="container mt-4">
        <sitemesh:write property='body'/>
    </div>

    <!-- Footer -->
    <footer class="footer text-center">
        <div class="container">
            <div class="info">
                <p class="mb-1"><strong>Họ tên:</strong> Lý Gia Hân | <strong>MSSV:</strong> 24133016 | <strong>Mã đề:</strong> Đề số 03</p>
                <p class="mb-0">&copy; 2026 WebProject_24133016 - Lập Trình Web</p>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
