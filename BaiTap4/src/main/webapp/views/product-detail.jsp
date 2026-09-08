<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>${product.productName} - ShoppingAdmin</title>
</head>
<body>
    <div class="container py-4">
        <!-- Breadcrumbs -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-decoration-none">Trang chủ</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/product" class="text-decoration-none">Sản phẩm</a></li>
                <c:if test="${product.category != null}">
                    <li class="breadcrumb-item">
                        <a href="${pageContext.request.contextPath}/product?cateId=${product.category.id}" class="text-decoration-none">
                            ${product.category.name}
                        </a>
                    </li>
                </c:if>
                <li class="breadcrumb-item active text-truncate" aria-current="page" style="max-width: 250px;">
                    ${product.productName}
                </li>
            </ol>
        </nav>

        <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-5">
            <div class="row g-0">
                <!-- Cột hình ảnh -->
                <div class="col-md-5 bg-light p-4 d-flex align-items-center justify-content-center border-end">
                    <c:choose>
                        <c:when test="${not empty product.image && fn:startsWith(product.image, 'http')}">
                            <img src="${product.image}" alt="${product.productName}" class="img-fluid rounded-3 shadow-sm object-fit-cover" style="max-height: 420px;"/>
                        </c:when>
                        <c:when test="${not empty product.image}">
                            <img src="${pageContext.request.contextPath}/image?fname=${product.image}" alt="${product.productName}" class="img-fluid rounded-3 shadow-sm object-fit-cover" style="max-height: 420px;"/>
                        </c:when>
                        <c:otherwise>
                            <div class="py-5 text-muted text-center">
                                <i class="fas fa-image fa-4x mb-3 d-block"></i>
                                Chưa có hình ảnh
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Cột thông tin chi tiết -->
                <div class="col-md-7 p-4 p-lg-5 d-flex flex-column justify-content-between">
                    <div>
                        <c:if test="${product.category != null}">
                            <div class="mb-2">
                                <span class="badge bg-primary-subtle text-primary border border-primary-subtle rounded-pill px-3 py-1">
                                    <i class="fas fa-tag me-1"></i>${product.category.name}
                                </span>
                            </div>
                        </c:if>

                        <h2 class="fw-bold text-dark mb-3">${product.productName}</h2>

                        <div class="mb-4">
                            <span class="fs-2 fw-bold text-danger">
                                <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true"/> VNĐ
                            </span>
                        </div>

                        <div class="mb-4">
                            <h6 class="fw-bold text-dark text-uppercase small text-muted">Mô tả sản phẩm:</h6>
                            <p class="text-secondary lh-lg mb-0" style="white-space: pre-line;">
                                <c:choose>
                                    <c:when test="${not empty product.description}">
                                        ${product.description}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="fst-italic text-muted">Chưa có thông tin mô tả chi tiết cho sản phẩm này.</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>

                        <div class="small text-muted mb-4">
                            <i class="fas fa-calendar-days me-1"></i> Ngày đăng: <b>${product.createdDate}</b>
                        </div>
                    </div>

                    <div class="d-flex gap-3 pt-3 border-top">
                        <a href="${pageContext.request.contextPath}/product" class="btn btn-outline-secondary rounded-pill px-4">
                            <i class="fas fa-arrow-left me-1"></i> Quay lại danh sách
                        </a>
                        <c:if test="${sessionScope.account.roleid == 1}">
                            <a href="${pageContext.request.contextPath}/admin/product/edit?id=${product.productId}" class="btn btn-primary rounded-pill px-4">
                                <i class="fas fa-pen-to-square me-1"></i> Chỉnh sửa sản phẩm
                            </a>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
