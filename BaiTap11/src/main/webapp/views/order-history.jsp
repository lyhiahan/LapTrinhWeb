<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<html>
<head><title>Lịch sử đặt hàng</title></head>
<body>
<h2 class="mb-4"><i class="fas fa-clock-rotate-left text-primary"></i> Lịch sử đặt hàng</h2>
<c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
<c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>

<div class="d-flex flex-wrap gap-2 mb-4">
    <a href="${pageContext.request.contextPath}/orders" class="btn ${selectedStatus == null ? 'btn-primary' : 'btn-outline-primary'}">Tất cả</a>
    <c:forEach var="st" items="${statuses}">
        <a href="${pageContext.request.contextPath}/orders?status=${st.name()}" class="btn ${selectedStatus == st ? 'btn-primary' : 'btn-outline-primary'}">${st.label}</a>
    </c:forEach>
</div>

<c:choose>
    <c:when test="${empty orders}"><div class="alert alert-light border text-center p-5">Không có đơn hàng ở trạng thái này.</div></c:when>
    <c:otherwise>
        <c:forEach var="order" items="${orders}">
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-header bg-white d-flex flex-wrap justify-content-between align-items-center py-3">
                    <div><strong>Đơn #${order.orderId}</strong> <span class="text-muted ms-2">${order.formattedCreatedAt}</span></div>
                    <span class="badge bg-${order.status.badgeClass} fs-6">${order.status.label}</span>
                </div>
                <div class="card-body">
                    <c:forEach var="item" items="${order.items}">
                        <div class="d-flex justify-content-between border-bottom py-2"><span>${item.productName} <span class="text-muted">× ${item.quantity}</span></span><strong><fmt:formatNumber value="${item.subtotal}" type="number"/> ₫</strong></div>
                    </c:forEach>
                    <div class="row mt-3">
                        <div class="col-md-8 text-muted"><i class="fas fa-location-dot"></i> ${order.receiverName} · ${order.phone}<br>${order.address}</div>
                        <div class="col-md-4 text-md-end"><small>Thanh toán: COD</small><div class="fs-5 text-danger fw-bold"><fmt:formatNumber value="${order.totalAmount}" type="number"/> ₫</div></div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>
</body>
</html>
