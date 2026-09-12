<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/></title>

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <!-- Font Awesome 6 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

    <!-- Bootstrap 5.3.3 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <style>
        :root {
            --admin-sidebar-width: 260px;
            --admin-primary: #4f46e5;
            --admin-dark: #0f172a;
        }

        body {
            font-family: 'Outfit', sans-serif;
            background-color: #f1f5f9;
            color: #1e293b;
            min-height: 100vh;
            display: flex;
            overflow-x: hidden;
        }

        /* Sidebar Styling */
        .admin-sidebar {
            width: var(--admin-sidebar-width);
            background: #1e293b;
            color: #e2e8f0;
            display: flex;
            flex-direction: column;
            flex-shrink: 0;
            position: fixed;
            top: 0;
            bottom: 0;
            left: 0;
            z-index: 1040;
            transition: all 0.3s ease;
            box-shadow: 2px 0 10px rgba(0,0,0,0.1);
        }

        .admin-brand {
            padding: 1.25rem 1.5rem;
            background: #0f172a;
            border-bottom: 1px solid #334155;
            display: flex;
            align-items: center;
            gap: 10px;
            text-decoration: none;
            color: #ffffff;
            font-size: 1.2rem;
            font-weight: 700;
        }

        .admin-brand i {
            color: #818cf8;
            font-size: 1.4rem;
        }

        .admin-user-card {
            padding: 1.25rem 1.5rem;
            border-bottom: 1px solid #334155;
            display: flex;
            align-items: center;
            gap: 12px;
            background: rgba(15, 23, 42, 0.4);
        }

        .admin-user-avatar {
            width: 44px;
            height: 44px;
            border-radius: 50%;
            object-fit: cover;
            border: 2px solid #818cf8;
        }

        .admin-user-initial {
            width: 44px;
            height: 44px;
            border-radius: 50%;
            background: linear-gradient(135deg, #4f46e5, #7c3aed);
            color: #ffffff;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 700;
            font-size: 1.1rem;
        }

        .sidebar-menu {
            padding: 1rem 0.75rem;
            overflow-y: auto;
            flex-grow: 1;
        }

        .sidebar-heading {
            font-size: 0.72rem;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            color: #64748b;
            font-weight: 700;
            padding: 0.75rem 1rem 0.25rem;
        }

        .sidebar-link {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 0.75rem 1rem;
            color: #cbd5e1;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 500;
            font-size: 0.92rem;
            transition: all 0.2s ease;
            margin-bottom: 2px;
        }

        .sidebar-link i {
            width: 20px;
            text-align: center;
            font-size: 1.05rem;
        }

        .sidebar-link:hover {
            color: #ffffff;
            background: #334155;
        }

        .sidebar-link.active {
            color: #ffffff;
            background: var(--admin-primary);
            box-shadow: 0 4px 12px rgba(79, 70, 229, 0.4);
        }

        .sidebar-sublink {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 0.5rem 1rem 0.5rem 2.75rem;
            color: #94a3b8;
            text-decoration: none;
            font-size: 0.85rem;
            border-radius: 6px;
            transition: all 0.2s;
        }

        .sidebar-sublink:hover {
            color: #ffffff;
            background: rgba(255,255,255,0.05);
        }

        /* Main Admin Content Wrapper */
        .admin-main {
            margin-left: var(--admin-sidebar-width);
            flex-grow: 1;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            width: calc(100% - var(--admin-sidebar-width));
        }

        .admin-topbar {
            background: #ffffff;
            border-bottom: 1px solid #e2e8f0;
            height: 64px;
            padding: 0 1.5rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
            position: sticky;
            top: 0;
            z-index: 100;
            box-shadow: 0 1px 3px rgba(0,0,0,0.03);
        }

        .admin-content {
            padding: 1.75rem;
            flex-grow: 1;
        }

        /* Admin Footer */
        .admin-footer {
            background: #ffffff;
            border-top: 1px solid #e2e8f0;
            padding: 1rem 1.75rem;
            font-size: 0.85rem;
            color: #64748b;
        }

        @media (max-width: 991.98px) {
            .admin-sidebar {
                transform: translateX(-100%);
            }
            .admin-sidebar.show {
                transform: translateX(0);
            }
            .admin-main {
                margin-left: 0;
                width: 100%;
            }
        }
    </style>

    <sitemesh:write property="head"/>
</head>
<body>
    <!-- Sidebar Admin -->
    <aside class="admin-sidebar" id="adminSidebar">
        <a href="${pageContext.request.contextPath}/admin/categories" class="admin-brand">
            <i class="fas fa-cubes"></i>
            <span>Shop<b>Admin</b></span>
        </a>

        <!-- User Info -->
        <div class="admin-user-card">
            <c:choose>
                <c:when test="${not empty sessionScope.account.avatar}">
                    <img class="admin-user-avatar" src="${pageContext.request.contextPath}/image?fname=${sessionScope.account.avatar}" alt="Admin"/>
                </c:when>
                <c:otherwise>
                    <div class="admin-user-initial">
                        ${not empty sessionScope.account.fullName ? fn:toUpperCase(fn:substring(sessionScope.account.fullName, 0, 1)) : 'A'}
                    </div>
                </c:otherwise>
            </c:choose>
            <div class="overflow-hidden">
                <div class="fw-bold text-white text-truncate" style="font-size: 0.95rem;">
                    ${not empty sessionScope.account.fullName ? sessionScope.account.fullName : 'Admin System'}
                </div>
                <span class="badge bg-danger-subtle text-danger border border-danger-subtle small py-0 px-2" style="font-size: 0.72rem;">
                    Quản trị viên
                </span>
            </div>
        </div>

        <!-- Menu items -->
        <div class="sidebar-menu">
            <div class="sidebar-heading">Menu Điều Khiển</div>

            <a href="${pageContext.request.contextPath}/admin/categories" class="sidebar-link ${pageContext.request.requestURI.contains('/admin/categor') ? 'active' : ''}">
                <i class="fas fa-layer-group text-info"></i>
                <span>Quản lý Danh mục</span>
            </a>
            <div class="ps-2">
                <a href="${pageContext.request.contextPath}/admin/category/add" class="sidebar-sublink">
                    <i class="fas fa-plus fa-xs"></i> Thêm danh mục
                </a>
                <a href="${pageContext.request.contextPath}/admin/categories" class="sidebar-sublink">
                    <i class="fas fa-list fa-xs"></i> Danh sách danh mục
                </a>
            </div>

            <a href="${pageContext.request.contextPath}/admin/products" class="sidebar-link mt-2 ${pageContext.request.requestURI.contains('/admin/product') ? 'active' : ''}">
                <i class="fas fa-box-open text-warning"></i>
                <span>Quản lý Sản phẩm</span>
            </a>
            <div class="ps-2">
                <a href="${pageContext.request.contextPath}/admin/product/add" class="sidebar-sublink">
                    <i class="fas fa-plus fa-xs"></i> Thêm sản phẩm
                </a>
                <a href="${pageContext.request.contextPath}/admin/products" class="sidebar-sublink">
                    <i class="fas fa-list fa-xs"></i> Danh sách sản phẩm
                </a>
            </div>

            <a href="${pageContext.request.contextPath}/admin/users" class="sidebar-link mt-2 ${pageContext.request.requestURI.contains('/admin/user') ? 'active' : ''}">
                <i class="fas fa-users-gear text-success"></i>
                <span>Quản lý Người dùng</span>
            </a>
            <div class="ps-2">
                <a href="${pageContext.request.contextPath}/admin/user/add" class="sidebar-sublink">
                    <i class="fas fa-user-plus fa-xs"></i> Thêm người dùng
                </a>
                <a href="${pageContext.request.contextPath}/admin/users" class="sidebar-sublink">
                    <i class="fas fa-users fa-xs"></i> Danh sách người dùng
                </a>
            </div>

            <div class="sidebar-heading mt-3">Hệ thống</div>

            <a href="${pageContext.request.contextPath}/home" class="sidebar-link" target="_blank">
                <i class="fas fa-arrow-up-right-from-square text-success"></i>
                <span>Xem Website</span>
            </a>
            <a href="${pageContext.request.contextPath}/profile" class="sidebar-link">
                <i class="fas fa-user-gear text-primary"></i>
                <span>Hồ sơ cá nhân</span>
            </a>
            <a href="${pageContext.request.contextPath}/logout" class="sidebar-link text-danger mt-2">
                <i class="fas fa-power-off"></i>
                <span>Đăng xuất</span>
            </a>
        </div>
    </aside>

    <!-- Main Container -->
    <div class="admin-main">
        <!-- Topbar -->
        <header class="admin-topbar">
            <div class="d-flex align-items-center gap-3">
                <button class="btn btn-light d-lg-none border" type="button" onclick="document.getElementById('adminSidebar').classList.toggle('show')">
                    <i class="fas fa-bars"></i>
                </button>
                <div class="d-none d-sm-block text-muted small">
                    <i class="fas fa-gauge-high text-primary me-1"></i> Bảng điều khiển quản trị hệ thống
                </div>
            </div>

            <div class="d-flex align-items-center gap-3">
                <a href="${pageContext.request.contextPath}/home" class="btn btn-sm btn-outline-secondary rounded-pill px-3">
                    <i class="fas fa-shop me-1"></i> Cửa hàng
                </a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-danger rounded-pill px-3">
                    <i class="fas fa-right-from-bracket me-1"></i> Đăng xuất
                </a>
            </div>
        </header>

        <!-- Body Injected by SiteMesh -->
        <div class="admin-content">
            <sitemesh:write property="body"/>
        </div>

        <!-- Footer -->
        <footer class="admin-footer d-flex justify-content-between align-items-center">
            <span>&copy; 2026 <strong>ShoppingAdmin System</strong> - SiteMesh Decorator 3</span>
            <span class="text-muted">Bootstrap 5.3 Theme</span>
        </footer>
    </div>

    <!-- Bootstrap 5.3.3 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
