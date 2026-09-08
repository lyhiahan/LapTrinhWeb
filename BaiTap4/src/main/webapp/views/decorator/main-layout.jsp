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

    <!-- Custom Bootstrap enhancements -->
    <style>
        :root {
            --bs-primary: #4f46e5;
            --bs-primary-rgb: 79, 70, 229;
            --bs-primary-hover: #4338ca;
            --bs-secondary: #7c3aed;
        }

        * {
            box-sizing: border-box;
        }

        body {
            font-family: 'Outfit', -apple-system, BlinkMacSystemFont, sans-serif;
            background-color: #f8fafc;
            color: #0f172a;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        main {
            flex: 1;
        }

        /* Navbar Styling */
        .navbar-custom {
            background: #ffffff;
            border-bottom: 1px solid #e2e8f0;
            box-shadow: 0 1px 3px rgba(0,0,0,0.04);
            padding-top: 0.75rem;
            padding-bottom: 0.75rem;
        }

        .navbar-brand {
            font-weight: 700;
            font-size: 1.45rem;
            color: #4f46e5 !important;
            letter-spacing: -0.5px;
        }

        .navbar-brand span {
            color: #0f172a;
        }

        .navbar-nav .nav-link {
            font-weight: 500;
            color: #475569;
            padding: 0.5rem 1rem;
            border-radius: 8px;
            transition: all 0.2s ease;
        }

        .navbar-nav .nav-link:hover,
        .navbar-nav .nav-link.active {
            color: #4f46e5;
            background-color: #f1f5f9;
        }

        /* User Avatar */
        .user-avatar-sm {
            width: 34px;
            height: 34px;
            border-radius: 50%;
            object-fit: cover;
            border: 2px solid #e2e8f0;
        }

        .user-initial-sm {
            width: 34px;
            height: 34px;
            border-radius: 50%;
            background: linear-gradient(135deg, #4f46e5, #7c3aed);
            color: #ffffff;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-weight: 700;
            font-size: 0.875rem;
        }

        /* Buttons */
        .btn-gradient-primary {
            background: linear-gradient(90deg, #4f46e5, #7c3aed);
            color: #ffffff;
            border: none;
            font-weight: 600;
            transition: all 0.25s ease;
        }

        .btn-gradient-primary:hover {
            color: #ffffff;
            transform: translateY(-1px);
            box-shadow: 0 4px 14px rgba(79, 70, 229, 0.35);
        }

        /* Footer */
        .footer-custom {
            background: #0f172a;
            color: #94a3b8;
            padding-top: 3.5rem;
            padding-bottom: 2rem;
            margin-top: 3rem;
            border-top: 1px solid #1e293b;
        }

        .footer-custom h6 {
            color: #f8fafc;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.05em;
            margin-bottom: 1.25rem;
        }

        .footer-custom a {
            color: #94a3b8;
            text-decoration: none;
            transition: color 0.2s ease;
        }

        .footer-custom a:hover {
            color: #818cf8;
        }

        .footer-bottom {
            border-top: 1px solid #1e293b;
            padding-top: 1.5rem;
            margin-top: 2.5rem;
            font-size: 0.875rem;
        }
    </style>

    <sitemesh:write property="head"/>
</head>
<body>
    <!-- Navbar Bootstrap -->
    <nav class="navbar navbar-expand-lg navbar-custom sticky-top">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/home">
                <i class="fas fa-bag-shopping text-primary"></i>
                <span>Shopping<b>Admin</b></span>
            </a>

            <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent"
                    aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-3">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/home">
                            <i class="fas fa-house me-1 text-muted"></i> Trang chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/product">
                            <i class="fas fa-store me-1 text-muted"></i> Sản phẩm
                        </a>
                    </li>
                </ul>

                <!-- User Profile / Auth Area -->
                <div class="d-flex align-items-center gap-2">
                    <c:choose>
                        <c:when test="${not empty sessionScope.account}">
                            <div class="dropdown">
                                <button class="btn btn-light d-flex align-items-center gap-2 rounded-pill px-3 py-1 border"
                                        type="button" id="userMenuBtn" data-bs-toggle="dropdown" aria-expanded="false">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.account.avatar}">
                                            <img class="user-avatar-sm" src="${pageContext.request.contextPath}/image?fname=${sessionScope.account.avatar}" alt="Avatar"/>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="user-initial-sm">
                                                ${fn:toUpperCase(fn:substring(sessionScope.account.fullName, 0, 1))}
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                    <span class="fw-semibold text-dark small">${sessionScope.account.fullName}</span>
                                    <i class="fas fa-chevron-down text-muted" style="font-size: 0.7rem;"></i>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 mt-2 rounded-3" aria-labelledby="userMenuBtn">
                                    <li class="px-3 py-2 border-bottom">
                                        <div class="small fw-semibold text-dark">${sessionScope.account.fullName}</div>
                                        <div class="small text-muted">${sessionScope.account.email}</div>
                                    </li>
                                    <li>
                                        <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/profile">
                                            <i class="fas fa-user-pen me-2 text-primary"></i> Chỉnh sửa hồ sơ
                                        </a>
                                    </li>
                                    <c:if test="${sessionScope.account.roleid == 1}">
                                        <li><hr class="dropdown-divider"></li>
                                        <li class="dropdown-header small text-uppercase fw-bold text-muted">Quản trị hệ thống</li>
                                        <li>
                                            <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/admin/categories">
                                                <i class="fas fa-list-check me-2 text-info"></i> Quản lý Danh mục
                                            </a>
                                        </li>
                                        <li>
                                            <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/admin/products">
                                                <i class="fas fa-boxes-stacked me-2 text-success"></i> Quản lý Sản phẩm
                                            </a>
                                        </li>
                                    </c:if>
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item py-2 text-danger" href="${pageContext.request.contextPath}/logout">
                                            <i class="fas fa-right-from-bracket me-2"></i> Đăng xuất
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary rounded-pill px-3 py-1 fw-semibold small">
                                <i class="fas fa-right-to-bracket me-1"></i> Đăng nhập
                            </a>
                            <a href="${pageContext.request.contextPath}/register" class="btn btn-gradient-primary rounded-pill px-3 py-1 small">
                                <i class="fas fa-user-plus me-1"></i> Đăng ký
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </nav>

    <!-- Body Content injected by SiteMesh -->
    <main>
        <sitemesh:write property="body"/>
    </main>

    <!-- Footer Bootstrap Template -->
    <footer class="footer-custom">
        <div class="container">
            <div class="row g-4">
                <div class="col-lg-4 col-md-6">
                    <h5 class="text-white fw-bold mb-3 d-flex align-items-center gap-2">
                        <i class="fas fa-bag-shopping text-primary"></i> ShoppingAdmin
                    </h5>
                    <p class="small text-muted pe-lg-4">
                        Nền tảng thương mại điện tử hiện đại xây dựng trên kiến trúc Jakarta Servlet, Hibernate JPA 3.0 và Sitemesh Decorator 3 cùng giao diện Bootstrap 5 đáp ứng hoàn hảo.
                    </p>
                </div>
                <div class="col-lg-2 col-md-6">
                    <h6>Khám phá</h6>
                    <ul class="list-unstyled small mb-0 d-flex flex-column gap-2">
                        <li><a href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                        <li><a href="${pageContext.request.contextPath}/product">Danh mục sản phẩm</a></li>
                        <li><a href="${pageContext.request.contextPath}/login">Đăng nhập tài khoản</a></li>
                        <li><a href="${pageContext.request.contextPath}/register">Tạo tài khoản mới</a></li>
                    </ul>
                </div>
                <div class="col-lg-3 col-md-6">
                    <h6>Tính năng nổi bật</h6>
                    <ul class="list-unstyled small mb-0 d-flex flex-column gap-2">
                        <li><a href="#!"><i class="fas fa-check text-primary me-2"></i>Sitemesh Decorator 3</a></li>
                        <li><a href="#!"><i class="fas fa-check text-primary me-2"></i>Validation Form 2 Lớp</a></li>
                        <li><a href="#!"><i class="fas fa-check text-primary me-2"></i>Xác thực OTP qua Email</a></li>
                        <li><a href="#!"><i class="fas fa-check text-primary me-2"></i>Quản trị CRUD & Phân trang</a></li>
                    </ul>
                </div>
                <div class="col-lg-3 col-md-6">
                    <h6>Liên hệ & Hỗ trợ</h6>
                    <p class="small mb-2"><i class="fas fa-location-dot me-2 text-primary"></i> TP. Hồ Chí Minh, Việt Nam</p>
                    <p class="small mb-2"><i class="fas fa-envelope me-2 text-primary"></i> support@shoppingadmin.vn</p>
                    <p class="small mb-0"><i class="fas fa-phone me-2 text-primary"></i> 0900 123 456</p>
                </div>
            </div>

            <div class="footer-bottom d-flex flex-column flex-sm-row justify-content-between align-items-center gap-2">
                <div>&copy; 2026 <strong>ShoppingAdmin</strong>. All rights reserved.</div>
                <div class="text-muted small">
                    Phát triển bởi <a href="#!" class="text-white text-decoration-none fw-semibold">Ly Gia Han</a>
                </div>
            </div>
        </div>
    </footer>

    <!-- Bootstrap 5.3.3 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
