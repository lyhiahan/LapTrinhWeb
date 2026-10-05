<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<html>
<head>
    <title>Giỏ hàng</title>
    <style>
        .cart-card { border: 0; border-radius: 14px; box-shadow: 0 4px 20px rgba(0,0,0,.08); }
        .cart-thumb { width: 100px; height: 68px; object-fit: cover; border-radius: 8px; background: #eee; }
        .quantity-input { width: 85px; }
    </style>
</head>
<body>
<div class="d-flex justify-content-between align-items-center mb-4">
    <h2><i class="fas fa-shopping-cart text-primary"></i> Giỏ hàng</h2>
    <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary">Tiếp tục mua hàng</a>
</div>
<c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
<c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>

<c:choose>
    <c:when test="${empty cartItems}">
        <div class="card cart-card text-center p-5">
            <i class="fas fa-basket-shopping fa-4x text-muted mb-3"></i>
            <h4>Giỏ hàng đang trống</h4>
            <p class="text-muted">Hãy chọn sản phẩm bạn muốn mua.</p>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary mx-auto">Xem sản phẩm</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="card cart-card p-3">
            <div class="table-responsive">
                <table class="table align-middle mb-0">
                    <thead><tr><th>Sản phẩm</th><th>Đơn giá</th><th>Số lượng</th><th>Thành tiền</th><th></th></tr></thead>
                    <tbody>
                    <c:forEach var="item" items="${cartItems}">
                        <tr>
                            <td>
                                <div class="d-flex gap-3 align-items-center">
                                    <c:choose>
                                        <c:when test="${not empty item.product.poster && (item.product.poster.startsWith('http://') || item.product.poster.startsWith('https://'))}">
                                            <img class="cart-thumb" src="${item.product.poster}" alt="">
                                        </c:when>
                                        <c:otherwise><div class="cart-thumb d-flex align-items-center justify-content-center"><i class="fas fa-video"></i></div></c:otherwise>
                                    </c:choose>
                                    <div><strong>${item.product.title}</strong><br><small class="text-muted">Kho: ${item.product.stock}</small></div>
                                </div>
                            </td>
                            <td><fmt:formatNumber value="${item.product.price}" type="number"/> ₫</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart/update/${item.product.videoId}" method="post" class="d-flex gap-2">
                                    <input type="hidden" name="_csrf" value="${fn:escapeXml(csrfToken)}">
                                    <input class="form-control quantity-input" type="number" name="quantity" value="${item.quantity}" min="0" max="${item.product.stock}">
                                    <button class="btn btn-outline-primary btn-sm" title="Cập nhật"><i class="fas fa-rotate"></i></button>
                                </form>
                            </td>
                            <td class="fw-bold text-danger"><fmt:formatNumber value="${item.subtotal}" type="number"/> ₫</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart/remove/${item.product.videoId}" method="post">
                                    <input type="hidden" name="_csrf" value="${fn:escapeXml(csrfToken)}">
                                    <button class="btn btn-outline-danger btn-sm" title="Xóa"><i class="fas fa-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="d-flex justify-content-end align-items-center gap-4 mt-4 pt-3 border-top">
                <div class="fs-5">Tổng cộng: <strong class="text-danger"><fmt:formatNumber value="${cartTotal}" type="number"/> ₫</strong></div>
                <a href="${pageContext.request.contextPath}/checkout" class="btn btn-success btn-lg"><i class="fas fa-money-bill-wave"></i> Thanh toán COD</a>
            </div>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>
