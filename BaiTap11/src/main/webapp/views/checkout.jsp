<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<html>
<head><title>Thanh toán COD</title></head>
<body>
<h2 class="mb-4"><i class="fas fa-truck-fast text-success"></i> Thanh toán khi nhận hàng (COD)</h2>
<c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
<div class="row g-4">
    <div class="col-lg-7">
        <div class="card border-0 shadow-sm p-4">
            <h5 class="mb-3">Thông tin nhận hàng</h5>
            <form action="${pageContext.request.contextPath}/checkout/cod" method="post">
                <div class="mb-3"><label class="form-label">Họ tên người nhận *</label><input class="form-control" name="receiverName" maxlength="100" required value="${user.fullname}"></div>
                <div class="mb-3"><label class="form-label">Số điện thoại *</label><input class="form-control" name="phone" maxlength="11" pattern="0[0-9]{9,10}" required value="${user.phone}"></div>
                <div class="mb-3"><label class="form-label">Địa chỉ *</label><textarea class="form-control" name="address" maxlength="500" rows="3" required></textarea></div>
                <div class="mb-3"><label class="form-label">Ghi chú</label><textarea class="form-control" name="note" maxlength="500" rows="2"></textarea></div>
                <div class="alert alert-info"><i class="fas fa-circle-info"></i> Bạn sẽ thanh toán bằng tiền mặt khi nhận hàng.</div>
                <button class="btn btn-success btn-lg w-100"><i class="fas fa-check"></i> Xác nhận đặt hàng COD</button>
            </form>
        </div>
    </div>
    <div class="col-lg-5">
        <div class="card border-0 shadow-sm p-4">
            <h5 class="mb-3">Đơn hàng của bạn</h5>
            <c:forEach var="item" items="${cartItems}">
                <div class="d-flex justify-content-between border-bottom py-2"><span>${item.product.title} × ${item.quantity}</span><strong><fmt:formatNumber value="${item.subtotal}" type="number"/> ₫</strong></div>
            </c:forEach>
            <div class="d-flex justify-content-between pt-3 fs-5"><span>Tổng cộng</span><strong class="text-danger"><fmt:formatNumber value="${cartTotal}" type="number"/> ₫</strong></div>
        </div>
    </div>
</div>
</body>
</html>
