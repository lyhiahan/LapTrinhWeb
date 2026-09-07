<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="sitemesh" uri="http://sitemesh.org/tags-sitemesh" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
<style>
    /* === SiteMesh Layout Reset === */
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body { font-family: 'Outfit', sans-serif; background: #f8fafc; color: #0f172a; }

    /* === Navbar === */
    .sm-navbar {
        background: #ffffff;
        border-bottom: 1px solid #e2e8f0;
        padding: 0 40px; height: 64px;
        display: flex; justify-content: space-between; align-items: center;
        position: sticky; top: 0; z-index: 100;
        box-shadow: 0 1px 3px rgba(0,0,0,0.04);
    }
    .sm-navbar .logo { font-size: 22px; font-weight: 700; color: #4f46e5; text-decoration: none; }
    .sm-navbar .logo span { color: #0f172a; }
    .sm-nav-links { display: flex; align-items: center; gap: 28px; }
    .sm-nav-links a {
        text-decoration: none; color: #475569; font-size: 15px; font-weight: 500;
        transition: color 0.2s; padding: 4px 0;
    }
    .sm-nav-links a:hover, .sm-nav-links a.active { color: #4f46e5; }
    .sm-nav-links .btn-login {
        background: linear-gradient(90deg, #4f46e5, #7c3aed); color: #fff;
        padding: 8px 22px; border-radius: 10px; font-weight: 600;
        transition: all 0.3s;
    }
    .sm-nav-links .btn-login:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(79,70,229,0.25); }

    /* === User Dropdown === */
    .user-dropdown {
        position: relative;
        display: inline-block;
    }
    .user-dropdown-btn {
        display: flex; align-items: center; gap: 8px;
        cursor: pointer; padding: 6px 12px;
        border-radius: 10px; transition: background 0.2s;
        text-decoration: none; color: #475569; font-weight: 500; font-size: 15px;
    }
    .user-dropdown-btn:hover { background: #f1f5f9; color: #4f46e5; }
    .user-dropdown-btn .user-avatar-sm {
        width: 32px; height: 32px; border-radius: 50%;
        object-fit: cover; border: 2px solid #e2e8f0;
    }
    .user-dropdown-btn .user-initial {
        width: 32px; height: 32px; border-radius: 50%;
        background: linear-gradient(135deg, #4f46e5, #7c3aed);
        color: #fff; display: flex; align-items: center; justify-content: center;
        font-size: 14px; font-weight: 700;
    }
    .user-dropdown-menu {
        display: none; position: absolute; right: 0; top: 100%;
        margin-top: 8px; background: #fff; border: 1px solid #e2e8f0;
        border-radius: 12px; box-shadow: 0 12px 32px rgba(15,23,42,0.1);
        min-width: 200px; overflow: hidden; z-index: 200;
        animation: dropdownFade 0.2s ease;
    }
    .user-dropdown:hover .user-dropdown-menu { display: block; }
    @keyframes dropdownFade {
        from { opacity: 0; transform: translateY(-8px); }
        to { opacity: 1; transform: translateY(0); }
    }
    .user-dropdown-menu a {
        display: flex; align-items: center; gap: 10px;
        padding: 12px 18px; font-size: 14px; font-weight: 500;
        color: #475569; text-decoration: none; transition: all 0.2s;
    }
    .user-dropdown-menu a:hover { background: #f8fafc; color: #4f46e5; }
    .user-dropdown-menu a i { width: 18px; text-align: center; font-size: 15px; }
    .user-dropdown-menu .divider { height: 1px; background: #e2e8f0; margin: 4px 0; }
    .user-dropdown-menu .logout-link { color: #dc2626; }
    .user-dropdown-menu .logout-link:hover { background: #fef2f2; color: #b91c1c; }

    /* === Footer === */
    .sm-footer {
        background: #0f172a; color: rgba(255,255,255,0.7);
        padding: 40px; text-align: center; font-size: 0.9rem;
        margin-top: 40px;
    }
    .sm-footer a { color: #818cf8; text-decoration: none; }

    @media (max-width: 768px) {
        .sm-navbar { padding: 0 20px; }
        .sm-nav-links { gap: 16px; }
    }
</style>
<sitemesh:write property="head"/>
</head>
<body>
    <!-- Navbar -->
    <nav class="sm-navbar">
        <a href="${pageContext.request.contextPath}/home" class="logo">Shopping<span>Admin</span></a>
        <div class="sm-nav-links">
            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
            <a href="${pageContext.request.contextPath}/product">Sản phẩm</a>
            <c:choose>
                <c:when test="${not empty sessionScope.account}">
                    <div class="user-dropdown">
                        <div class="user-dropdown-btn">
                            <c:choose>
                                <c:when test="${not empty sessionScope.account.avatar}">
                                    <img class="user-avatar-sm" src="${pageContext.request.contextPath}/image?fname=${sessionScope.account.avatar}" alt="Avatar"/>
                                </c:when>
                                <c:otherwise>
                                    <div class="user-initial">
                                        ${fn:toUpperCase(fn:substring(sessionScope.account.fullName, 0, 1))}
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <span>${sessionScope.account.fullName}</span>
                            <i class="fas fa-chevron-down" style="font-size: 11px; color: #94a3b8;"></i>
                        </div>
                        <div class="user-dropdown-menu">
                            <a href="${pageContext.request.contextPath}/waiting">
                                <i class="fas fa-user"></i> Trang cá nhân
                            </a>
                            <a href="${pageContext.request.contextPath}/profile">
                                <i class="fas fa-pen-to-square"></i> Chỉnh sửa hồ sơ
                            </a>
                            <c:if test="${sessionScope.account.roleid == 1}">
                                <div class="divider"></div>
                                <a href="${pageContext.request.contextPath}/admin/categories">
                                    <i class="fas fa-list"></i> Quản lý danh mục
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/products">
                                    <i class="fas fa-box"></i> Quản lý sản phẩm
                                </a>
                            </c:if>
                            <div class="divider"></div>
                            <a href="${pageContext.request.contextPath}/logout" class="logout-link">
                                <i class="fas fa-right-from-bracket"></i> Đăng xuất
                            </a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="btn-login">Đăng nhập</a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>

    <!-- Page Content -->
    <sitemesh:write property="body"/>

    <!-- Footer -->
    <footer class="sm-footer">
        <p>&copy; 2026 ShoppingAdmin. Developed by <a href="#">Ly Gia Han</a></p>
    </footer>
</body>
</html>
