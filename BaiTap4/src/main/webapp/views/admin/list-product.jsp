<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Sản phẩm - Admin</title>
</head>
<body>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h3 class="fw-bold mb-1"><i class="fas fa-boxes-stacked text-primary me-2"></i>Quản lý Sản phẩm</h3>
            <p class="text-muted mb-0 small">Quản lý kho hàng, cập nhật giá, hình ảnh và phân loại danh mục</p>
        </div>
        <a href="<c:url value='/admin/product/add'/>" class="btn btn-primary rounded-pill px-3 shadow-sm">
            <i class="fas fa-plus me-1"></i> Thêm sản phẩm mới
        </a>
    </div>

    <!-- Thông báo Alert -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-triangle-exclamation me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.message == 'add_success'}">
        <div class="alert alert-success alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-circle-check me-2"></i>Thêm sản phẩm mới thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.message == 'edit_success'}">
        <div class="alert alert-success alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-circle-check me-2"></i>Cập nhật sản phẩm thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Card Bảng Sản phẩm -->
    <div class="card border-0 shadow-sm rounded-4">
        <div class="card-header bg-white py-3 border-0 rounded-top-4 d-flex flex-column flex-sm-row justify-content-between align-items-sm-center gap-2">
            <span class="fw-bold text-dark">Danh sách tất cả sản phẩm</span>
            <div class="input-group input-group-sm" style="max-width: 280px;">
                <span class="input-group-text bg-light border-end-0"><i class="fas fa-search text-muted"></i></span>
                <input type="text" class="form-control bg-light border-start-0" id="searchInput" placeholder="Tìm kiếm theo tên sản phẩm..." onkeyup="filterProductTable()">
            </div>
        </div>

        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0" id="productTable">
                <thead class="table-light text-uppercase small text-muted">
                    <tr>
                        <th style="width: 5%;" class="ps-4">STT</th>
                        <th style="width: 12%;">Hình ảnh</th>
                        <th style="width: 28%;">Tên sản phẩm</th>
                        <th style="width: 15%;">Danh mục</th>
                        <th style="width: 15%;">Đơn giá</th>
                        <th style="width: 10%;">Ngày tạo</th>
                        <th style="width: 15%;" class="text-end pe-4">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty productList}">
                        <tr>
                            <td colspan="7" class="text-center py-5 text-muted">
                                <i class="fas fa-box-open fa-2x mb-3 text-secondary d-block"></i>
                                Chưa có sản phẩm nào trong kho. Hãy bấm <b>Thêm sản phẩm mới</b>!
                            </td>
                        </tr>
                    </c:if>
                    <c:forEach items="${productList}" var="p" varStatus="STT">
                        <tr>
                            <td class="ps-4 fw-medium text-muted">${STT.index + 1}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.image != null && p.image.startsWith('http')}">
                                        <img src="${p.image}" alt="${p.productName}" class="rounded-3 border object-fit-cover shadow-sm" style="width: 54px; height: 54px;"/>
                                    </c:when>
                                    <c:when test="${not empty p.image}">
                                        <img src="<c:url value='/image?fname=${p.image}'/>" alt="${p.productName}" class="rounded-3 border object-fit-cover shadow-sm" style="width: 54px; height: 54px;"/>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="rounded-3 bg-light border d-flex align-items-center justify-content-center text-muted" style="width: 54px; height: 54px;">
                                            <i class="fas fa-image"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="fw-semibold text-dark">${p.productName}</div>
                                <c:if test="${not empty p.description}">
                                    <div class="small text-muted text-truncate" style="max-width: 260px;">${p.description}</div>
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.category != null}">
                                        <span class="badge bg-primary-subtle text-primary border border-primary-subtle rounded-pill px-3 py-1 fw-medium">
                                            <i class="fas fa-tag me-1"></i>${p.category.name}
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary-subtle text-secondary rounded-pill px-2">Chưa phân loại</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <span class="fw-bold text-danger">
                                    <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/> đ
                                </span>
                            </td>
                            <td class="small text-muted">${p.createdDate}</td>
                            <td class="text-end pe-4">
                                <a href="<c:url value='/admin/product/edit?id=${p.productId}'/>" class="btn btn-sm btn-outline-primary rounded-pill px-3 me-1">
                                    <i class="fas fa-pen-to-square me-1"></i>Sửa
                                </a>
                                <a href="<c:url value='/admin/product/delete?id=${p.productId}'/>"
                                   class="btn btn-sm btn-outline-danger rounded-pill px-3"
                                   onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm ${p.productName}?')">
                                    <i class="fas fa-trash me-1"></i>Xóa
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="card-footer bg-white py-3 border-0 rounded-bottom-4 d-flex justify-content-between align-items-center text-muted small">
            <span>
                <c:choose>
                    <c:when test="${empty productList}">0 sản phẩm</c:when>
                    <c:otherwise>Tổng cộng: <b>${productList.size()}</b> sản phẩm</c:otherwise>
                </c:choose>
            </span>
        </div>
    </div>

    <script>
        function filterProductTable() {
            var input = document.getElementById('searchInput');
            var filter = input.value.toUpperCase();
            var table = document.getElementById('productTable');
            var tr = table.getElementsByTagName('tr');
            for (var i = 1; i < tr.length; i++) {
                var tdName = tr[i].getElementsByTagName('td')[2];
                if (tdName) {
                    var txtValue = tdName.textContent || tdName.innerText;
                    tr[i].style.display = txtValue.toUpperCase().indexOf(filter) > -1 ? '' : 'none';
                }
            }
        }
    </script>
</body>
</html>
