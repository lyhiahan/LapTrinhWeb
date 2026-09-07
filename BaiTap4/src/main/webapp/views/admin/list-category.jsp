<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard - Quản Lý Danh Mục</title>
<link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
<style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body {
        font-family: 'Roboto', 'Segoe UI', Arial, sans-serif;
        background-color: #f8f9fa;
        display: flex; flex-direction: column;
        height: 100vh; color: #212529;
    }

    .top-header {
        background: #212529;
        height: 56px;
        display: flex; justify-content: space-between; align-items: center;
        padding: 0 24px;
        z-index: 100;
        box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    }
    .top-header .logo-area {
        font-size: 20px; font-weight: 700; color: #ffffff;
        letter-spacing: 0.5px;
    }
    .top-header .header-right {
        display: flex; align-items: center; gap: 16px;
    }
    .top-header .greeting {
        font-size: 14px; color: #f8f9fa;
    }
    .top-header .greeting b { font-weight: 500; }
    .btn-logout {
        background: #fa5252; color: white;
        padding: 6px 18px; border-radius: 8px;
        text-decoration: none; font-size: 13px; font-weight: 500;
        transition: background 0.2s;
        border: 1px solid #e03131;
    }
    .btn-logout:hover { background: #e03131; }

    .workspace-wrapper { display: flex; flex: 1; overflow: hidden; }

    .app-sidebar {
        width: 240px; background: #343a40; color: #ffffff;
        display: flex; flex-direction: column;
        overflow-y: auto;
    }
    .user-card {
        display: flex; flex-direction: column; align-items: center;
        padding: 28px 20px 20px;
        border-bottom: 1px solid rgba(255,255,255,0.08);
    }
    .user-card img {
        width: 80px; height: 80px; border-radius: 50%;
        border: 3px solid rgba(255,255,255,0.15);
        object-fit: cover; background: #ffffff;
    }
    .user-card .admin-label {
        font-size: 13px; color: rgba(255,255,255,0.7);
        margin-top: 12px; font-weight: 400;
    }

    .navigation-menu { width: 100%; display: flex; flex-direction: column; padding-top: 8px; }
    .nav-link {
        padding: 13px 20px; color: rgba(255,255,255,0.85);
        text-decoration: none; font-size: 14px;
        display: flex; align-items: center; gap: 12px;
        transition: all 0.2s; font-weight: 500;
        border-left: 4px solid transparent;
    }
    .nav-link:hover {
        background: rgba(255,255,255,0.05);
        color: #ffffff;
    }
    .nav-link.active-tab {
        background: #495057;
        color: #74c0fc; font-weight: 700;
        border-left: 4px solid #339af0;
    }
    .nav-link i { width: 20px; text-align: center; font-size: 16px; }

    .sub-menu {
        display: flex; flex-direction: column;
        background: rgba(0,0,0,0.15);
    }
    .sub-link {
        padding: 10px 20px 10px 56px;
        color: rgba(255,255,255,0.65);
        text-decoration: none; font-size: 13px;
        transition: all 0.2s;
        display: flex; align-items: center; gap: 8px;
    }
    .sub-link:hover { color: #ffffff; background: rgba(255,255,255,0.05); }
    .sub-link.active { color: #74c0fc; font-weight: 700; }
    .sub-link i { font-size: 8px; width: 14px; text-align: center; }

    .content-canvas { flex: 1; padding: 30px; overflow-y: auto; }

    .page-title {
        color: #212529; font-size: 24px; font-weight: 700;
        margin-bottom: 6px;
        border-left: 4px solid #339af0;
        padding-left: 12px;
    }
    .page-subtitle {
        color: #6c757d; font-size: 14px; margin-bottom: 24px;
        padding-left: 16px;
    }

    .data-card {
        background: #ffffff; border-radius: 12px;
        box-shadow: 0 4px 16px rgba(0,0,0,0.04);
        overflow: hidden;
        border: 1px solid #dee2e6;
    }
    .card-header {
        padding: 16px 20px;
        border-bottom: 1px solid #dee2e6;
        font-size: 15px; font-weight: 500; color: #212529;
    }

    .table-controls {
        display: flex; justify-content: space-between; align-items: center;
        padding: 14px 20px;
        border-bottom: 1px solid #e9ecef;
    }
    .records-per-page {
        display: flex; align-items: center; gap: 6px;
        font-size: 13px; color: #6c757d;
    }
    .records-per-page select {
        padding: 6px 10px; border: 1px solid #ced4da; border-radius: 8px;
        font-size: 13px; outline: none;
    }
    .search-box {
        display: flex; align-items: center; gap: 6px;
        font-size: 13px; color: #6c757d;
    }
    .search-box input {
        padding: 6px 12px; border: 1px solid #ced4da; border-radius: 8px;
        font-size: 13px; outline: none; width: 200px;
    }
    .search-box input:focus { border-color: #495057; }

    .modern-table { width: 100%; border-collapse: collapse; }
    .modern-table th {
        background: #f8f9fa; color: #495057;
        font-weight: 600; font-size: 13px;
        padding: 14px 20px;
        border-bottom: 2px solid #dee2e6;
        text-align: left;
    }
    .modern-table td {
        padding: 14px 20px;
        border-bottom: 1px solid #e9ecef;
        font-size: 14px; vertical-align: middle;
    }
    .modern-table tr:hover { background: #f8f9fa; }

    .avatar-box {
        width: 130px; height: 95px;
        object-fit: cover; border-radius: 10px;
        border: 1px solid #dee2e6;
        box-shadow: 0 2px 8px rgba(0,0,0,0.06);
    }

    .action-link {
        text-decoration: none; font-weight: 500;
        font-size: 13px; color: #1c7ed6;
        padding: 6px 14px;
        border-radius: 20px;
        background: #e7f5ff;
        border: 1px solid #a5d8ff;
        transition: all 0.2s;
        display: inline-block;
        margin-right: 6px;
    }
    .action-link:hover { background: #d0ebff; color: #1864ab; }
    .link-delete { background: #fff5f5; color: #c92a2a; border-color: #ffc9c9; }
    .link-delete:hover { background: #ffe3e3; color: #b02a37; }

    .empty-state {
        text-align: center; color: #868e96;
        padding: 50px 20px; font-size: 14px;
        font-style: italic;
    }

    .table-footer {
        padding: 12px 20px;
        font-size: 13px; color: #868e96;
        border-top: 1px solid #dee2e6;
        background: #f8f9fa;
    }

    .alert-success {
        background: #ebfbee; color: #2b8a3e;
        padding: 14px 20px; border-radius: 10px;
        margin-bottom: 20px; font-size: 14px; font-weight: 500;
        border-left: 5px solid #40c057;
        display: flex; align-items: center; justify-content: space-between;
        animation: slideDown 0.4s ease;
    }
    .alert-success .close-btn {
        background: none; border: none; color: #2b8a3e;
        font-size: 18px; cursor: pointer; padding: 0 4px;
    }
    @keyframes slideDown {
        from { opacity: 0; transform: translateY(-10px); }
        to { opacity: 1; transform: translateY(0); }
    }
    .price-col { color: #c92a2a; font-weight: 600; }
</style>
</head>
<body>
    <div class="top-header">
        <div class="logo-area">Dashboard</div>
        <div class="header-right">
            <c:if test="${not empty sessionScope.account}">
                <span class="greeting">Xin chào <b>${sessionScope.account.fullName}</b></span>
            </c:if>
            <a href="<c:url value='/logout'/>" class="btn-logout">Đăng xuất</a>
        </div>
    </div>

    <div class="workspace-wrapper">
        <div class="app-sidebar">
            <div class="user-card">
                <img src="https://ui-avatars.com/api/?name=Admin&background=ffffff&color=343a40&size=128&bold=true&font-size=0.4" alt="Admin"/>
                <div class="admin-label">Bạn là Admin</div>
            </div>
            <div class="navigation-menu">
                <a href="${pageContext.request.contextPath}/home" class="nav-link">
                    <i class="fas fa-home" style="color: #fcc419;"></i> Trang chủ
                </a>
                <a href="<c:url value='/admin/categories'/>" class="nav-link" style="background: rgba(255,255,255,0.05); color: #74c0fc;">
                    <i class="fas fa-list" style="color: #339af0;"></i> Quản lý Danh mục
                </a>
                <div class="sub-menu">
                    <a href="<c:url value='/admin/category/add'/>" class="sub-link">
                        <i class="fa-regular fa-circle" style="color: rgba(255,255,255,0.5);"></i> Thêm danh mục mới
                    </a>
                    <a href="<c:url value='/admin/categories'/>" class="sub-link active">
                        <i class="fa-solid fa-circle" style="color: #74c0fc;"></i> Danh sách danh mục
                    </a>
                </div>
                <a href="<c:url value='/admin/products'/>" class="nav-link">
                    <i class="fas fa-box" style="color: #51cf66;"></i> Quản lý sản phẩm
                </a>
                <div class="sub-menu">
                    <a href="<c:url value='/admin/product/add'/>" class="sub-link">
                        <i class="fa-regular fa-circle" style="color: rgba(255,255,255,0.5);"></i> Thêm sản phẩm mới
                    </a>
                    <a href="<c:url value='/admin/products'/>" class="sub-link">
                        <i class="fa-regular fa-circle" style="color: rgba(255,255,255,0.5);"></i> Danh sách sản phẩm
                    </a>
                </div>
            </div>
        </div>

        <div class="content-canvas">
            <h1 class="page-title">Quản lý danh mục</h1>
            <p class="page-subtitle">Nơi bạn có thể quản lý danh mục của mình</p>

            <c:if test="${not empty error}">
                <div style="background: #fff5f5; color: #c92a2a; padding: 14px 20px; border-radius: 10px; margin-bottom: 20px; font-size: 14px; font-weight: 500; border-left: 5px solid #fa5252;">
                    <i class="fas fa-exclamation-triangle" style="margin-right: 8px;"></i>${error}
                </div>
            </c:if>

            <c:if test="${param.message == 'add_success'}">
                <div class="alert-success" id="alertMsg">
                    <span><i class="fas fa-check-circle" style="margin-right: 8px;"></i>Thêm danh mục mới thành công!</span>
                    <button class="close-btn" onclick="this.parentElement.style.display='none'">&times;</button>
                </div>
            </c:if>
            <c:if test="${param.message == 'edit_success'}">
                <div class="alert-success" id="alertMsg">
                    <span><i class="fas fa-check-circle" style="margin-right: 8px;"></i>Cập nhật danh mục thành công!</span>
                    <button class="close-btn" onclick="this.parentElement.style.display='none'">&times;</button>
                </div>
            </c:if>

            <div class="data-card">
                <div class="card-header">Danh sách danh mục</div>

                <div class="table-controls">
                    <div class="records-per-page">
                        <select id="recordsSelect">
                            <option>10</option>
                            <option>25</option>
                            <option>50</option>
                        </select>
                        records per page
                    </div>
                    <div class="search-box">
                        Search: <input type="text" id="searchInput" placeholder="" onkeyup="filterTable()"/>
                    </div>
                </div>

                <table class="modern-table" id="categoryTable">
                    <thead>
                            <th style="width: 10%;">STT</th>
                            <th style="width: 60%;">Tên danh mục</th>
                            <th style="width: 30%;">Hành động</th>
                        </tr>
                    </thead>
                    <tbody id="tableBody">
                        <c:if test="${empty cateList}">
                            <tr><td colspan="3" class="empty-state">Chưa có danh mục nào. Hãy thêm danh mục mới!</td></tr>
                        </c:if>
                        <c:forEach items="${cateList}" var="cate" varStatus="STT">
                            <tr>
                                <td style="font-weight: 500; color: #495057;">${STT.index + 1}</td>
                                <td style="font-weight: 500; color: #212529;">${cate.name}</td>
                                <td>
                                    <a class="action-link" href="<c:url value='/admin/category/edit?id=${cate.id}'/>">Sửa</a>
                                    <a class="action-link link-delete" href="<c:url value='/admin/category/delete?id=${cate.id}'/>" onclick="return confirm('Bạn có chắc chắn muốn xóa danh mục này?')">Xóa</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <div class="table-footer">
                    <c:choose>
                        <c:when test="${empty cateList}">Hiển thị 0 danh mục</c:when>
                        <c:otherwise>Hiển thị ${cateList.size()} danh mục</c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <script>
        function filterTable() {
            var input = document.getElementById('searchInput');
            var filter = input.value.toUpperCase();
            var table = document.getElementById('categoryTable');
            var tr = table.getElementsByTagName('tr');
            for (var i = 1; i < tr.length; i++) {
                var tdName = tr[i].getElementsByTagName('td')[1]; // Tên danh mục giờ là cột thứ 2 (index 1)
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
        var alertMsg = document.getElementById('alertMsg');
        if (alertMsg) {
            setTimeout(function() {
                alertMsg.style.transition = 'opacity 0.5s';
                alertMsg.style.opacity = '0';
                setTimeout(function() { alertMsg.style.display = 'none'; }, 500);
            }, 4000);
        }
    </script>
</body>
</html>
