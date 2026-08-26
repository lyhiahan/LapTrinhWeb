<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard - Thêm Danh Mục</title>
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
    .top-header .logo-area { font-size: 20px; font-weight: 700; color: #ffffff; letter-spacing: 0.5px; }
    .top-header .header-right { display: flex; align-items: center; gap: 16px; }
    .top-header .greeting { font-size: 14px; color: #f8f9fa; }
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
    .user-card .admin-label { font-size: 13px; color: rgba(255,255,255,0.7); margin-top: 12px; font-weight: 400; }

    .navigation-menu { width: 100%; display: flex; flex-direction: column; padding-top: 8px; }
    .nav-link {
        padding: 13px 20px; color: rgba(255,255,255,0.75);
        text-decoration: none; font-size: 14px;
        display: flex; align-items: center; gap: 12px;
        transition: all 0.2s; font-weight: 500;
        border-left: 4px solid transparent;
    }
    .nav-link:hover { background: rgba(255,255,255,0.05); color: #ffffff; }
    .nav-link i { width: 20px; text-align: center; font-size: 16px; }

    .sub-menu { display: flex; flex-direction: column; background: rgba(0,0,0,0.15); }
    .sub-link {
        padding: 10px 20px 10px 56px; color: rgba(255,255,255,0.65);
        text-decoration: none; font-size: 13px; transition: all 0.2s;
        display: flex; align-items: center; gap: 8px;
    }
    .sub-link:hover { color: #ffffff; background: rgba(255,255,255,0.05); }
    .sub-link.active { color: #74c0fc; font-weight: 700; }
    .sub-link i { font-size: 8px; width: 14px; text-align: center; }

    .content-canvas { flex: 1; padding: 30px; overflow-y: auto; }

    .page-title {
        color: #212529; font-size: 24px; font-weight: 700; margin-bottom: 6px;
        border-left: 4px solid #339af0; padding-left: 12px;
    }
    .page-subtitle { color: #6c757d; font-size: 14px; margin-bottom: 24px; padding-left: 16px; }

    .form-card {
        background: #ffffff; border-radius: 12px;
        box-shadow: 0 4px 16px rgba(0,0,0,0.04);
        padding: 30px; max-width: 550px;
        border: 1px solid #dee2e6;
    }
    .form-card h3 {
        color: #212529; font-size: 18px; font-weight: 600;
        margin-bottom: 24px; padding-bottom: 12px;
        border-bottom: 2px solid #e9ecef;
    }
    .form-group { margin-bottom: 20px; }
    .form-group label {
        display: block; font-size: 14px; font-weight: 500;
        color: #495057; margin-bottom: 8px;
    }
    .form-group input[type="text"],
    .form-group input[type="file"] {
        width: 100%; padding: 10px 12px;
        border: 1px solid #ced4da; border-radius: 8px;
        font-size: 14px; outline: none;
        font-family: 'Roboto', sans-serif;
        transition: border-color 0.2s;
        color: #212529;
    }
    .form-group input[type="text"]:focus { border-color: #495057; }
    .form-group input[type="file"] { padding: 8px 12px; background: #f8f9fa; }

    .error-msg {
        background: #fff5f5; color: #c92a2a;
        padding: 10px 14px; border-radius: 8px;
        margin-bottom: 16px; font-size: 13px;
        border-left: 4px solid #fa5252;
    }

    .btn-group { display: flex; gap: 12px; margin-top: 28px; }
    .btn-group button, .btn-group a {
        padding: 10px 24px; border: none; border-radius: 8px;
        font-weight: 500; font-size: 14px; cursor: pointer;
        text-decoration: none; text-align: center;
        font-family: 'Roboto', sans-serif;
        transition: all 0.2s;
    }
    .btn-submit { background: #e7f5ff; color: #1c7ed6; border: 1px solid #a5d8ff; }
    .btn-submit:hover { background: #d0ebff; color: #1864ab; }
    .btn-reset { background: #f1f3f5; color: #495057; border: 1px solid #dee2e6; }
    .btn-reset:hover { background: #e9ecef; }

    .img-preview-area {
        margin-top: 12px;
        display: none;
    }
    .img-preview-area img {
        width: 130px; height: 95px;
        border-radius: 10px; border: 1px solid #dee2e6;
        object-fit: cover;
        box-shadow: 0 2px 8px rgba(0,0,0,0.06);
    }
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
                <a href="#" class="nav-link">
                    <i class="fas fa-tachometer-alt" style="color: #fcc419;"></i> Dashboard
                </a>
                <a href="<c:url value='/admin/categories'/>" class="nav-link" style="background: rgba(255,255,255,0.05); color: #74c0fc;">
                    <i class="fas fa-list" style="color: #339af0;"></i> Quản lý Danh mục
                </a>
                <div class="sub-menu">
                    <a href="<c:url value='/admin/category/add'/>" class="sub-link active">
                        <i class="fa-solid fa-circle" style="color: #74c0fc;"></i> Thêm danh mục mới
                    </a>
                    <a href="<c:url value='/admin/categories'/>" class="sub-link">
                        <i class="fa-regular fa-circle" style="color: rgba(255,255,255,0.5);"></i> Danh sách danh mục
                    </a>
                </div>
                <a href="#" class="nav-link">
                    <i class="fas fa-box" style="color: #51cf66;"></i> Quản lý sản phẩm
                </a>
                <a href="#" class="nav-link">
                    <i class="fas fa-users" style="color: #cc5de8;"></i> Quản lý tài khoản
                </a>
            </div>
        </div>

        <div class="content-canvas">
            <h1 class="page-title">Thêm danh mục mới</h1>
            <p class="page-subtitle">Tạo danh mục hàng hóa mới cho cửa hàng</p>

            <div class="form-card">
                <h3><i class="fas fa-plus-circle" style="margin-right: 8px;"></i>Tạo danh mục</h3>

                <c:if test="${not empty error}">
                    <div class="error-msg"><i class="fas fa-exclamation-triangle" style="margin-right: 6px;"></i>${error}</div>
                </c:if>

                <form action="<c:url value='/admin/category/insert'/>" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <label><i class="fas fa-tag" style="margin-right: 6px; color: #495057;"></i>Tên danh mục hàng hóa:</label>
                        <input type="text" name="name" placeholder="Nhập tên phân loại..." required/>
                    </div>
                    <div class="form-group">
                        <label><i class="fas fa-money-bill-wave" style="margin-right: 6px; color: #495057;"></i>Giá (VNĐ):</label>
                        <input type="text" name="price" placeholder="Nhập giá sản phẩm... (VD: 150000)" required/>
                    </div>
                    <div class="form-group">
                        <label><i class="fas fa-image" style="margin-right: 6px; color: #495057;"></i>Ảnh biểu tượng đại diện:</label>
                        <input type="file" name="icon" accept="image/*" required id="iconInput"/>
                        <div class="img-preview-area" id="previewArea">
                            <img id="previewImg" src="" alt="Preview"/>
                        </div>
                    </div>
                    <div class="btn-group">
                        <button type="submit" class="btn-submit"><i class="fas fa-save" style="margin-right: 6px;"></i>Thêm danh mục</button>
                        <button type="reset" class="btn-reset" onclick="hidePreview()"><i class="fas fa-undo" style="margin-right: 6px;"></i>Hủy nhập</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        document.getElementById('iconInput').addEventListener('change', function(e) {
            var file = e.target.files[0];
            if (file) {
                var reader = new FileReader();
                reader.onload = function(ev) {
                    document.getElementById('previewImg').src = ev.target.result;
                    document.getElementById('previewArea').style.display = 'block';
                };
                reader.readAsDataURL(file);
            }
        });
        function hidePreview() {
            document.getElementById('previewArea').style.display = 'none';
        }
    </script>
</body>
</html>
