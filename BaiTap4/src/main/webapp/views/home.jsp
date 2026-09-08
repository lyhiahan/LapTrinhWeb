<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang Chủ - ShoppingAdmin</title>
    <style>
        .hero-banner {
            background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 50%, #9333ea 100%);
            border-radius: 24px;
            padding: 60px 40px;
            color: #ffffff;
            margin-bottom: 40px;
            box-shadow: 0 10px 30px rgba(79, 70, 229, 0.25);
        }
        .product-card-modern {
            transition: all 0.3s ease;
            border-radius: 16px;
            overflow: hidden;
            background: #ffffff;
        }
        .product-card-modern:hover {
            transform: translateY(-6px);
            box-shadow: 0 12px 28px rgba(15, 23, 42, 0.1) !important;
            border-color: #c7d2fe !important;
        }
        .product-img-wrapper {
            height: 220px;
            overflow: hidden;
            background-color: #f1f5f9;
        }
        .product-img-wrapper img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.4s ease;
        }
        .product-card-modern:hover .product-img-wrapper img {
            transform: scale(1.08);
        }
    </style>
</head>
<body>
    <div class="container py-4">
        <!-- Hero Banner -->
        <div class="hero-banner text-center position-relative overflow-hidden">
            <div class="position-relative z-1 max-w-700 mx-auto">
                <span class="badge bg-white bg-opacity-20 text-white rounded-pill px-3 py-2 mb-3 fw-medium">
                    <i class="fas fa-sparkles me-1"></i> Khám phá bộ sưu tập công nghệ mới nhất 2026
                </span>
                <h1 class="display-5 fw-bold mb-3">Mua Sắm Đẳng Cấp & Tiện Lợi</h1>
                <p class="lead opacity-90 mb-4 fs-6">
                    Hàng ngàn sản phẩm công nghệ, điện máy chính hãng với ưu đãi đặc biệt và hỗ trợ giao hàng nhanh chóng.
                </p>
                <a href="${pageContext.request.contextPath}/product" class="btn btn-light btn-lg rounded-pill px-4 fw-semibold text-primary shadow">
                    <i class="fas fa-store me-2"></i>Xem Tất Cả Sản Phẩm
                </a>
            </div>
        </div>

        <!-- Danh sách Danh mục Nổi Bật -->
        <c:if test="${not empty categories}">
            <div class="mb-5">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <div>
                        <h4 class="fw-bold text-dark mb-0"><i class="fas fa-grid-2 text-primary me-2"></i>Danh Mục Ngành Hàng</h4>
                        <p class="text-muted small mb-0">Lựa chọn theo nhu cầu mua sắm của bạn</p>
                    </div>
                </div>

                <div class="row g-3">
                    <c:forEach items="${categories}" var="cate">
                        <div class="col-6 col-md-3 col-lg-2">
                            <a href="${pageContext.request.contextPath}/product?cateId=${cate.id}"
                               class="card h-100 border-0 shadow-sm rounded-4 text-center p-3 text-decoration-none text-dark bg-white hover-shadow transition">
                                <div class="bg-primary-subtle text-primary rounded-circle mx-auto d-flex align-items-center justify-content-center mb-2"
                                     style="width: 50px; height: 50px;">
                                    <c:choose>
                                        <c:when test="${fn:containsIgnoreCase(cate.name, 'Áo')}">
                                            <i class="fas fa-shirt fs-5"></i>
                                        </c:when>
                                        <c:when test="${fn:containsIgnoreCase(cate.name, 'Quần')}">
                                            <i class="fas fa-user-tie fs-5"></i>
                                        </c:when>
                                        <c:when test="${fn:containsIgnoreCase(cate.name, 'Laptop')}">
                                            <i class="fas fa-laptop fs-5"></i>
                                        </c:when>
                                        <c:when test="${fn:containsIgnoreCase(cate.name, 'Điện thoại')}">
                                            <i class="fas fa-mobile-screen-button fs-5"></i>
                                        </c:when>
                                        <c:when test="${fn:containsIgnoreCase(cate.name, 'Giày')}">
                                            <i class="fas fa-shoe-prints fs-5"></i>
                                        </c:when>
                                        <c:when test="${fn:containsIgnoreCase(cate.name, 'Trang Sức')}">
                                            <i class="fas fa-gem fs-5"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fas fa-tag fs-5"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <span class="fw-semibold small">${cate.name}</span>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>

        <!-- Sản phẩm mới nhất -->
        <div class="mb-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold text-dark mb-1">
                        <i class="fas fa-fire text-danger me-2"></i>Sản Phẩm Mới Nhất
                    </h3>
                    <p class="text-muted small mb-0">Các sản phẩm vừa cập bến kho hàng của chúng tôi</p>
                </div>
                <a href="${pageContext.request.contextPath}/product" class="btn btn-outline-primary btn-sm rounded-pill px-3 fw-medium">
                    Xem tất cả <i class="fas fa-arrow-right ms-1"></i>
                </a>
            </div>

            <c:if test="${empty newestProducts}">
                <div class="text-center py-5 bg-white rounded-4 border">
                    <i class="fas fa-box-open fa-3x text-muted mb-3 d-block"></i>
                    <p class="text-muted">Chưa có sản phẩm nào. Hãy đăng nhập tài khoản Quản trị để thêm sản phẩm mới!</p>
                </div>
            </c:if>

            <div class="row g-4">
                <c:forEach items="${newestProducts}" var="p">
                    <div class="col-sm-6 col-md-4 col-lg-3">
                        <div class="card h-100 border-0 shadow-sm product-card-modern">
                            <a href="${pageContext.request.contextPath}/product/detail?id=${p.productId}" class="product-img-wrapper d-block text-decoration-none">
                                <c:choose>
                                    <c:when test="${not empty p.image && fn:startsWith(p.image, 'http')}">
                                        <img src="${p.image}" alt="${p.productName}"/>
                                    </c:when>
                                    <c:when test="${not empty p.image}">
                                        <img src="${pageContext.request.contextPath}/image?fname=${p.image}" alt="${p.productName}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="w-100 h-100 d-flex align-items-center justify-content-center text-muted">
                                            <i class="fas fa-image fa-2x"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </a>
                            <div class="card-body d-flex flex-column p-3">
                                <c:if test="${p.category != null}">
                                    <div class="mb-2">
                                        <span class="badge bg-primary-subtle text-primary border border-primary-subtle rounded-pill px-2 py-1 small">
                                            ${p.category.name}
                                        </span>
                                    </div>
                                </c:if>
                                <h6 class="card-title text-dark fw-bold mb-2 text-truncate" title="${p.productName}">
                                    <a href="${pageContext.request.contextPath}/product/detail?id=${p.productId}" class="text-dark text-decoration-none">
                                        ${p.productName}
                                    </a>
                                </h6>
                                <div class="mt-auto d-flex justify-content-between align-items-center pt-2">
                                    <span class="fs-6 fw-bold text-danger">
                                        <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/> đ
                                    </span>
                                    <a href="${pageContext.request.contextPath}/product/detail?id=${p.productId}" class="btn btn-sm btn-light rounded-circle border p-2 text-primary" title="Xem chi tiết">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</body>
</html>
