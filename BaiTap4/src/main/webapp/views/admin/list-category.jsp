<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Danh mục - Admin</title>
</head>
<body>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h3 class="fw-bold mb-1"><i class="fas fa-layer-group text-primary me-2"></i>Quản lý Danh mục</h3>
            <p class="text-muted mb-0 small">Quản lý và phân loại các ngành hàng cho sản phẩm của bạn</p>
        </div>
        <a href="<c:url value='/admin/category/add'/>" class="btn btn-primary rounded-pill px-3 shadow-sm">
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

    <!-- Card Bảng Danh mục -->
    <div class="card border-0 shadow-sm rounded-4">
        <div class="card-header bg-white py-3 border-0 rounded-top-4 d-flex flex-column flex-sm-row justify-content-between align-items-sm-center gap-2">
            <span class="fw-bold text-dark">Danh sách danh mục sản phẩm</span>
            <div class="input-group input-group-sm" style="max-width: 280px;">
                <span class="input-group-text bg-light border-end-0"><i class="fas fa-search text-muted"></i></span>
                <input type="text" class="form-control bg-light border-start-0" id="searchInput" placeholder="Tìm kiếm theo tên..." onkeyup="filterCategoryTable()">
            </div>
        </div>

        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0" id="categoryTable">
                <thead class="table-light text-uppercase small text-muted">
                    <tr>
                        <th style="width: 10%;" class="ps-4">STT</th>
                        <th style="width: 60%;">Tên danh mục</th>
                        <th style="width: 30%;" class="text-end pe-4">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty cateList}">
                        <tr>
                            <td colspan="3" class="text-center py-5 text-muted">
                                <i class="fas fa-folder-open fa-2x mb-3 text-secondary d-block"></i>
                                Chưa có danh mục nào. Hãy bấm <b>Thêm danh mục mới</b>!
                            </td>
                        </tr>
                    </c:if>
                    <c:forEach items="${cateList}" var="cate" varStatus="STT">
                        <tr>
                            <td class="ps-4 fw-medium text-muted">${STT.index + 1}</td>
                            <td>
                                <span class="fw-semibold text-dark">${cate.name}</span>
                            </td>
                            <td class="text-end pe-4">
                                <a href="<c:url value='/admin/category/edit?id=${cate.id}'/>" class="btn btn-sm btn-outline-primary rounded-pill px-3 me-1">
                                    <i class="fas fa-pen-to-square me-1"></i>Sửa
                                </a>
                                <a href="<c:url value='/admin/category/delete?id=${cate.id}'/>"
                                   class="btn btn-sm btn-outline-danger rounded-pill px-3"
                                   onclick="return confirm('Bạn có chắc chắn muốn xóa danh mục này? Tất cả sản phẩm thuộc danh mục cũng sẽ bị ảnh hưởng.')">
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
                    <c:when test="${empty cateList}">0 danh mục</c:when>
                    <c:otherwise>Tổng cộng: <b>${cateList.size()}</b> danh mục</c:otherwise>
                </c:choose>
            </span>
        </div>
    </div>

    <script>
        function filterCategoryTable() {
            var input = document.getElementById('searchInput');
            var filter = input.value.toUpperCase();
            var table = document.getElementById('categoryTable');
            var tr = table.getElementsByTagName('tr');
            for (var i = 1; i < tr.length; i++) {
                var tdName = tr[i].getElementsByTagName('td')[1];
                if (tdName) {
                    var txtValue = tdName.textContent || tdName.innerText;
                    if (txtValue.toUpperCase().indexOf(filter) > -1) {
                        tr[i].style.display = '';
                    } else {
                        tr[i].style.display = 'none';
                    }
                }
            }
        }
    </script>
</body>
</html>
