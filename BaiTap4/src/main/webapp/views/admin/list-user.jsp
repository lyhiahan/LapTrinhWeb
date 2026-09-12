<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Người dùng - Admin</title>
</head>
<body>
    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
        <div>
            <h3 class="fw-bold mb-1"><i class="fas fa-users-gear text-primary me-2"></i>Quản lý Người dùng</h3>
            <p class="text-muted mb-0 small">Quản lý danh sách tài khoản, phân quyền quản trị và trạng thái người dùng</p>
        </div>
        <a href="<c:url value='/admin/user/add'/>" class="btn btn-primary rounded-pill px-3 shadow-sm align-self-start align-self-md-auto">
            <i class="fas fa-user-plus me-1"></i> Thêm người dùng mới
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
            <i class="fas fa-circle-check me-2"></i>Thêm người dùng mới thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.message == 'edit_success'}">
        <div class="alert alert-success alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-circle-check me-2"></i>Cập nhật thông tin người dùng thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.message == 'delete_success'}">
        <div class="alert alert-success alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-circle-check me-2"></i>Xóa tài khoản người dùng thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.message == 'delete_error'}">
        <div class="alert alert-danger alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-triangle-exclamation me-2"></i>Không thể xóa người dùng này! Vui lòng thử lại.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.message == 'self_delete_forbidden'}">
        <div class="alert alert-warning alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-shield-halved me-2"></i>Bạn không thể tự xóa tài khoản đang đăng nhập hiện tại!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Thanh tìm kiếm & lọc -->
    <div class="card border-0 shadow-sm rounded-4 mb-4">
        <div class="card-body p-3">
            <form action="<c:url value='/admin/users'/>" method="GET" class="row g-2 align-items-center">
                <div class="col-12 col-md-6 col-lg-5">
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0"><i class="fas fa-search text-muted"></i></span>
                        <input type="text" name="keyword" class="form-control bg-light border-start-0"
                               placeholder="Tìm theo username, họ tên, email, SĐT..." value="${keyword}">
                    </div>
                </div>
                <div class="col-12 col-md-auto d-flex gap-2">
                    <button type="submit" class="btn btn-primary px-3 rounded-pill">
                        <i class="fas fa-magnifying-glass me-1"></i> Tìm kiếm
                    </button>
                    <c:if test="${not empty keyword}">
                        <a href="<c:url value='/admin/users'/>" class="btn btn-outline-secondary px-3 rounded-pill">
                            <i class="fas fa-arrow-rotate-left me-1"></i> Làm mới
                        </a>
                    </c:if>
                </div>
                <div class="col-12 col-md text-md-end text-muted small mt-2 mt-md-0">
                    Tìm thấy: <strong class="text-dark">${totalUsers}</strong> người dùng
                </div>
            </form>
        </div>
    </div>

    <!-- Card Bảng Người dùng -->
    <div class="card border-0 shadow-sm rounded-4">
        <div class="card-header bg-white py-3 border-0 rounded-top-4 d-flex justify-content-between align-items-center">
            <span class="fw-bold text-dark"><i class="fas fa-id-card me-2 text-primary"></i>Danh sách người dùng</span>
            <span class="badge bg-primary-subtle text-primary border border-primary-subtle rounded-pill px-3 py-2">
                Trang ${currentPage} / ${totalPages}
            </span>
        </div>

        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light text-uppercase small text-muted">
                    <tr>
                        <th style="width: 5%;" class="ps-4">STT</th>
                        <th style="width: 25%;">Tài khoản & Họ tên</th>
                        <th style="width: 20%;">Email</th>
                        <th style="width: 12%;">Số điện thoại</th>
                        <th style="width: 13%;">Vai trò</th>
                        <th style="width: 10%;">Trạng thái</th>
                        <th style="width: 15%;" class="text-end pe-4">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty userList}">
                        <tr>
                            <td colspan="7" class="text-center py-5 text-muted">
                                <i class="fas fa-user-slash fa-3x mb-3 text-secondary d-block"></i>
                                Không tìm thấy người dùng nào phù hợp!
                            </td>
                        </tr>
                    </c:if>
                    <c:forEach items="${userList}" var="u" varStatus="STT">
                        <tr>
                            <td class="ps-4 fw-medium text-muted">${(currentPage - 1) * pageSize + STT.index + 1}</td>
                            <td>
                                <div class="d-flex align-items-center gap-3">
                                    <c:choose>
                                        <c:when test="${not empty u.avatar}">
                                            <img src="<c:url value='/image?fname=${u.avatar}'/>" alt="${u.userName}"
                                                 class="rounded-circle border object-fit-cover shadow-sm"
                                                 style="width: 42px; height: 42px; min-width: 42px;"
                                                 onerror="this.src='https://ui-avatars.com/api/?name=${u.userName}&background=random'"/>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="rounded-circle d-flex align-items-center justify-content-center text-white fw-bold shadow-sm"
                                                 style="width: 42px; height: 42px; min-width: 42px; background: linear-gradient(135deg, #6366f1, #8b5cf6); font-size: 1.1rem;">
                                                ${fn:toUpperCase(fn:substring(u.userName, 0, 1))}
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                    <div>
                                        <div class="fw-bold text-dark">${u.userName}</div>
                                        <div class="text-muted small">${not empty u.fullName ? u.fullName : '<span class="fst-italic text-secondary">Chưa cập nhật</span>'}</div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <span class="text-dark small">${u.email}</span>
                            </td>
                            <td>
                                <span class="text-secondary small">${not empty u.phone ? u.phone : '-'}</span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.roleid == 1}">
                                        <span class="badge bg-danger-subtle text-danger border border-danger-subtle rounded-pill px-2 py-1 small">
                                            <i class="fas fa-shield-alt me-1"></i>Admin
                                        </span>
                                    </c:when>
                                    <c:when test="${u.roleid == 2}">
                                        <span class="badge bg-warning-subtle text-warning-emphasis border border-warning-subtle rounded-pill px-2 py-1 small">
                                            <i class="fas fa-user-tie me-1"></i>Manager
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-info-subtle text-info-emphasis border border-info-subtle rounded-pill px-2 py-1 small">
                                            <i class="fas fa-user me-1"></i>User
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.isActive}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle rounded-pill px-2 py-1 small">
                                            <i class="fas fa-check-circle me-1"></i>Hoạt động
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary-subtle text-secondary border border-secondary-subtle rounded-pill px-2 py-1 small">
                                            <i class="fas fa-lock me-1"></i>Đang khóa
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end pe-4">
                                <a href="<c:url value='/admin/user/edit?id=${u.id}'/>"
                                   class="btn btn-sm btn-outline-primary rounded-pill px-3 me-1"
                                   title="Chỉnh sửa thông tin">
                                    <i class="fas fa-pen-to-square me-1"></i>Sửa
                                </a>
                                <a href="<c:url value='/admin/user/delete?id=${u.id}'/>"
                                   class="btn btn-sm btn-outline-danger rounded-pill px-3"
                                   onclick="return confirm('Bạn có chắc chắn muốn xóa tài khoản [${u.userName}]?');"
                                   title="Xóa tài khoản">
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
                Hiển thị từ <b>${empty userList ? 0 : (currentPage - 1) * pageSize + 1}</b> đến
                <b>${(currentPage - 1) * pageSize + userList.size()}</b> trên tổng số <b>${totalUsers}</b> người dùng
            </div>

            <c:if test="${totalPages > 1}">
                <nav aria-label="User Page navigation">
                    <ul class="pagination pagination-sm mb-0">
                        <!-- Đầu trang -->
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link rounded-start-pill" href="<c:url value='/admin/users?page=1&keyword=${keyword}'/>" aria-label="First">
                                <i class="fas fa-angles-left"></i>
                            </a>
                        </li>
                        <!-- Trước -->
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="<c:url value='/admin/users?page=${currentPage - 1}&keyword=${keyword}'/>" aria-label="Previous">
                                <i class="fas fa-angle-left"></i>
                            </a>
                        </li>

                        <!-- Các trang -->
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:if test="${i >= currentPage - 2 && i <= currentPage + 2}">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="<c:url value='/admin/users?page=${i}&keyword=${keyword}'/>">${i}</a>
                                </li>
                            </c:if>
                        </c:forEach>

                        <!-- Tiếp -->
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="<c:url value='/admin/users?page=${currentPage + 1}&keyword=${keyword}'/>" aria-label="Next">
                                <i class="fas fa-angle-right"></i>
                            </a>
                        </li>
                        <!-- Cuối trang -->
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link rounded-end-pill" href="<c:url value='/admin/users?page=${totalPages}&keyword=${keyword}'/>" aria-label="Last">
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
