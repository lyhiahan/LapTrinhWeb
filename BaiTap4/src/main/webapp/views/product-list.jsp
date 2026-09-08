<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Danh Sách Sản Phẩm - ShoppingAdmin</title>
    <style>
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
    <!-- Page Header Banner -->
    <div class="bg-dark text-white py-4 mb-4">
        <div class="container">
            <h2 class="fw-bold mb-1"><i class="fas fa-store text-primary me-2"></i>Tất Cả Sản Phẩm</h2>
            <p class="text-white-50 mb-0 small">Khám phá các sản phẩm chất lượng cao và cập nhật mới nhất</p>
        </div>
    </div>

    <div class="container pb-5">
        <!-- Bộ lọc danh mục (Pills) -->
        <div class="d-flex flex-wrap align-items-center gap-2 mb-4 pb-2 border-bottom">
            <span class="text-muted small fw-semibold me-2"><i class="fas fa-filter text-primary me-1"></i>Danh mục:</span>
            <a href="${pageContext.request.contextPath}/product"
               class="btn btn-sm ${empty selectedCateId ? 'btn-primary' : 'btn-outline-secondary'} rounded-pill px-3">
                Tất cả (${totalProducts})
            </a>
            <c:forEach items="${categories}" var="cat">
                <a href="${pageContext.request.contextPath}/product?cateId=${cat.id}"
                   class="btn btn-sm ${selectedCateId == cat.id ? 'btn-primary' : 'btn-outline-secondary'} rounded-pill px-3">
                    ${cat.name}
                </a>
            </c:forEach>
        </div>

        <!-- Trống danh sách -->
        <c:if test="${empty products}">
            <div class="text-center py-5 bg-white rounded-4 border">
                <i class="fas fa-box-open fa-3x text-muted mb-3 d-block"></i>
                <h5 class="fw-bold text-dark">Không tìm thấy sản phẩm nào</h5>
                <p class="text-muted small">Hãy thử chọn danh mục khác hoặc quay lại sau!</p>
                <a href="${pageContext.request.contextPath}/product" class="btn btn-primary rounded-pill px-4 btn-sm">
                    Xem tất cả sản phẩm
                </a>
            </div>
        </c:if>

        <!-- Product Grid -->
        <div class="row g-4 mb-5">
            <c:forEach items="${products}" var="p">
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

        <!-- Pagination Bootstrap -->
        <c:if test="${totalPages > 1}">
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                        <a class="page-link rounded-start-pill" href="${pageContext.request.contextPath}/product?page=${currentPage - 1}${not empty selectedCateId ? '&cateId=' : ''}${selectedCateId}">
                            <i class="fas fa-chevron-left me-1"></i> Trước
                        </a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="pageNum">
                        <li class="page-item ${currentPage == pageNum ? 'active' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/product?page=${pageNum}${not empty selectedCateId ? '&cateId=' : ''}${selectedCateId}">
                                ${pageNum}
                            </a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                        <a class="page-link rounded-end-pill" href="${pageContext.request.contextPath}/product?page=${currentPage + 1}${not empty selectedCateId ? '&cateId=' : ''}${selectedCateId}">
                            Sau <i class="fas fa-chevron-right ms-1"></i>
                        </a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </div>
</body>
</html>
