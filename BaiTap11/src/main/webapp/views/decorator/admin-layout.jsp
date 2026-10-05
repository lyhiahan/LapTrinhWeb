<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property='title'/> - Admin Panel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <style>
        body { font-family: 'Segoe UI', Tahoma, sans-serif; background-color: #f0f2f5; }
        .sidebar { min-height: calc(100vh - 56px - 60px); background: #2c3e50; padding-top: 20px; }
        .sidebar .nav-link { color: rgba(255,255,255,0.8); padding: 12px 20px; border-radius: 5px; margin: 2px 10px; }
        .sidebar .nav-link:hover { background: rgba(255,255,255,0.1); color: #fff; }
        .sidebar .nav-link.active { background: #3498db; color: #fff; }
        .sidebar .nav-link i { width: 25px; }
        .admin-navbar { background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%); }
        .admin-navbar .navbar-brand { font-weight: 700; font-size: 1.3rem; }
        .admin-content { padding: 20px; }
        .footer-admin { background: #2c3e50; color: #ecf0f1; padding: 15px 0; }
    </style>
    <sitemesh:write property='head'/>
</head>
<body>
    <!-- Admin Header -->
    <nav class="navbar navbar-expand-lg navbar-dark admin-navbar">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/admin/home">
                <i class="fas fa-shield-halved"></i> Admin Panel
            </a>
            <div class="d-flex align-items-center">
                <span class="text-white me-3">
                    <i class="fas fa-user-shield"></i>
                    <c:if test="${sessionScope.account != null}">
                        ${sessionScope.account.fullname}
                    </c:if>
                </span>
                <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-light btn-sm me-2">
                    <i class="fas fa-globe"></i> Về trang Client
                </a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">
                    <i class="fas fa-sign-out-alt"></i> Đăng xuất
                </a>
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-2 sidebar p-0">
                <nav class="nav flex-column">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/home">
                        <i class="fas fa-tachometer-alt"></i> Dashboard
                    </a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/videos">
                        <i class="fas fa-video"></i> Quản lý Video
                    </a>
                    <hr class="text-white mx-3">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">
                        <i class="fas fa-arrow-left"></i> Về trang chủ
                    </a>
                </nav>
            </div>

            <!-- Main Content -->
            <div class="col-md-10 admin-content">
                <sitemesh:write property='body'/>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="footer-admin text-center">
        <div class="container">
            <p class="mb-0" style="font-size:14px;">
                <strong>Họ tên:</strong> Lý Gia Hân | <strong>MSSV:</strong> 24133016 | <strong>Mã đề:</strong> Đề số 03
            </p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
