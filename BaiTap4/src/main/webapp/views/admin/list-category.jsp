<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Danh mục - Admin</title>
</head>
<body>
    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
        <div>
            <h3 class="fw-bold mb-1"><i class="fas fa-layer-group text-primary me-2"></i>Quản lý Danh mục</h3>
            <p class="text-muted mb-0 small">Quản lý và phân loại các ngành hàng cho sản phẩm trong hệ thống</p>
        </div>
        <a href="<c:url value='/admin/category/add'/>" class="btn btn-primary rounded-pill px-3 shadow-sm align-self-start align-self-md-auto">
            <i class="fas fa-plus me-1"></i> Thêm danh mục mới
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
            <i class="fas fa-circle-check me-2"></i>Thêm danh mục mới thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.message == 'edit_success'}">
        <div class="alert alert-success alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-circle-check me-2"></i>Cập nhật danh mục thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.message == 'delete_success'}">
        <div class="alert alert-success alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-circle-check me-2"></i>Xóa danh mục thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.message == 'delete_error'}">
        <div class="alert alert-danger alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-triangle-exclamation me-2"></i>Không thể xóa danh mục này (có thể danh mục đang chứa sản phẩm liên kết)!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Thanh tìm kiếm & lọc -->
    <div class="card border-0 shadow-sm rounded-4 mb-4">
        <div class="card-body p-3">
            <form action="<c:url value='/admin/categories'/>" method="GET" class="row g-2 align-items-center">
                <div class="col-12 col-md-6 col-lg-5">
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0"><i class="fas fa-search text-muted"></i></span>
                        <input type="text" name="keyword" class="form-control bg-light border-start-0"
                               placeholder="Tìm kiếm danh mục theo tên..." value="${keyword}">
                    </div>
                </div>
                <div class="col-12 col-md-auto d-flex gap-2">
                    <button type="submit" class="btn btn-primary px-3 rounded-pill">
                        <i class="fas fa-magnifying-glass me-1"></i> Tìm kiếm
                    </button>
                    <c:if test="${not empty keyword}">
                        <a href="<c:url value='/admin/categories'/>" class="btn btn-outline-secondary px-3 rounded-pill">
                            <i class="fas fa-arrow-rotate-left me-1"></i> Làm mới
                        </a>
                    </c:if>
                </div>
                <div class="col-12 col-md text-md-end text-muted small mt-2 mt-md-0">
                    Tìm thấy: <strong class="text-dark">${totalItems}</strong> danh mục
                </div>
            </form>
        </div>
    </div>

    <!-- Card Bảng Danh mục -->
    <div class="card border-0 shadow-sm rounded-4">
        <div class="card-header bg-white py-3 border-0 rounded-top-4 d-flex justify-content-between align-items-center">
            <span class="fw-bold text-dark"><i class="fas fa-list-check me-2 text-primary"></i>Danh sách danh mục</span>
            <span class="badge bg-primary-subtle text-primary border border-primary-subtle rounded-pill px-3 py-2">
                Trang ${currentPage} / ${totalPages}
            </span>
        </div>

        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0" id="categoryTable">
                <thead class="table-light text-uppercase small text-muted">
                    <tr>
                        <th style="width: 10%;" class="ps-4">STT</th>
                        <th style="width: 20%;">Mã danh mục</th>
                        <th style="width: 45%;">Tên danh mục</th>
                        <th style="width: 25%;" class="text-end pe-4">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty cateList}">
                        <tr>
                            <td colspan="4" class="text-center py-5 text-muted">
                                <i class="fas fa-folder-open fa-3x mb-3 text-secondary d-block"></i>
                                Không tìm thấy danh mục nào phù hợp!
                            </td>
                        </tr>
                    </c:if>
                    <c:forEach items="${cateList}" var="cate" varStatus="STT">
                        <tr>
                            <td class="ps-4 fw-medium text-muted">${(currentPage - 1) * pageSize + STT.index + 1}</td>
                            <td>
                                <span class="badge bg-light text-secondary border font-monospace">#CAT-${cate.id}</span>
                            </td>
                            <td>
                                <span class="fw-semibold text-dark">${cate.name}</span>
                            </td>
                            <td class="text-end pe-4">
                                <a href="<c:url value='/admin/category/edit?id=${cate.id}'/>"
                                   class="btn btn-sm btn-outline-primary rounded-pill px-3 me-1"
                                   title="Chỉnh sửa danh mục">
                                    <i class="fas fa-pen-to-square me-1"></i>Sửa
                                </a>
                                <a href="<c:url value='/admin/category/delete?id=${cate.id}'/>"
                                   class="btn btn-sm btn-outline-danger rounded-pill px-3"
                                   onclick="return confirm('Bạn có chắc chắn muốn xóa danh mục [${cate.name}]?');"
                                   title="Xóa danh mục">
                                    <i class="fas fa-trash me-1"></i>Xóa
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Card Footer & Phân trang Bootstrap 5 -->
        <div class="card-footer bg-white py-3 border-0 rounded-bottom-4 d-flex flex-column flex-md-row justify-content-between align-items-center gap-3 text-muted small">
            <div>
                Hiển thị từ <b>${empty cateList ? 0 : (currentPage - 1) * pageSize + 1}</b> đến
                <b>${(currentPage - 1) * pageSize + cateList.size()}</b> trên tổng số <b>${totalItems}</b> danh mục
            </div>

            <c:if test="${totalPages > 1}">
                <nav aria-label="Page navigation">
                    <ul class="pagination pagination-sm mb-0">
                        <!-- Đầu trang -->
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link rounded-start-pill" href="<c:url value='/admin/categories?page=1&keyword=${keyword}'/>" aria-label="First">
                                <i class="fas fa-angles-left"></i>
                            </a>
                        </li>
                        <!-- Trước -->
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="<c:url value='/admin/categories?page=${currentPage - 1}&keyword=${keyword}'/>" aria-label="Previous">
                                <i class="fas fa-angle-left"></i>
                            </a>
                        </li>

                        <!-- Các trang -->
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:if test="${i >= currentPage - 2 && i <= currentPage + 2}">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="<c:url value='/admin/categories?page=${i}&keyword=${keyword}'/>">${i}</a>
                                </li>
                            </c:if>
                        </c:forEach>

                        <!-- Tiếp -->
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="<c:url value='/admin/categories?page=${currentPage + 1}&keyword=${keyword}'/>" aria-label="Next">
                                <i class="fas fa-angle-right"></i>
                            </a>
                        </li>
                        <!-- Cuối trang -->
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link rounded-end-pill" href="<c:url value='/admin/categories?page=${totalPages}&keyword=${keyword}'/>" aria-label="Last">
                                <i class="fas fa-angles-right"></i>
                            </a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</body>
</html>
