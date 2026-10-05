<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Dashboard Quản trị</title>
</head>
<body>
    <div class="row mb-4">
        <div class="col-12">
            <h2><i class="fas fa-tachometer-alt"></i> Dashboard Quản trị</h2>
            <p class="text-muted">Chào mừng bạn đến với trang quản trị WebProject_24133016</p>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4 mb-3">
            <div class="card border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="fas fa-video fa-3x text-primary mb-3"></i>
                    <h5>Quản lý Video</h5>
                    <p class="text-muted">Thêm, sửa, xóa video</p>
                    <a href="${pageContext.request.contextPath}/admin/videos" class="btn btn-primary">
                        <i class="fas fa-arrow-right"></i> Truy cập
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-3">
            <div class="card border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="fas fa-home fa-3x text-success mb-3"></i>
                    <h5>Trang chủ Client</h5>
                    <p class="text-muted">Xem trang người dùng</p>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-success">
                        <i class="fas fa-arrow-right"></i> Truy cập
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-3">
            <div class="card border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="fas fa-info-circle fa-3x text-warning mb-3"></i>
                    <h5>Thông tin</h5>
                    <p class="text-muted">MSSV: 24133016</p>
                    <p class="text-muted">Họ tên: Lý Gia Hân</p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
