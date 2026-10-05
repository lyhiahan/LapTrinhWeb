<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
    <title>Quản lý Video</title>
    <style>
        .video-table { border-radius: 10px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .video-table th { background: linear-gradient(135deg, #2c3e50, #3498db); color: #fff; font-weight: 600; }
        .video-table td { vertical-align: middle; }
        .poster-thumb { width: 80px; height: 50px; object-fit: cover; border-radius: 5px; }
        .poster-thumb-placeholder { width: 80px; height: 50px; background: #e9ecef; border-radius: 5px; display: flex; align-items: center; justify-content: center; color: #aaa; }
        .search-box { max-width: 400px; }
    </style>
</head>
<body>
    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle me-2"></i> ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle me-2"></i> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="fas fa-video"></i> Quản lý Video</h2>
        <a href="${pageContext.request.contextPath}/admin/videos/add" class="btn btn-primary">
            <i class="fas fa-plus"></i> Thêm Video mới
        </a>
    </div>

    <!-- Thanh tìm kiếm -->
    <form action="${pageContext.request.contextPath}/admin/videos" method="get" class="mb-4">
        <div class="input-group search-box">
            <input type="text" class="form-control" name="keyword" value="${keyword}" placeholder="Tìm kiếm video...">
            <button type="submit" class="btn btn-outline-primary">
                <i class="fas fa-search"></i> Tìm
            </button>
        </div>
    </form>

    <p class="text-muted">Tổng cộng: <strong>${totalItems}</strong> video | Trang ${currentPage} / ${totalPages}</p>

    <!-- Bảng danh sách video -->
    <table class="table table-hover video-table">
        <thead>
            <tr>
                <th>STT</th>
                <th>Poster</th>
                <th>Mã Video</th>
                <th>Tiêu đề</th>
                <th>Category</th>
                <th>Views</th>
                <th>Active</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="video" items="${videos}" varStatus="loop">
                <tr>
                    <td>${(currentPage - 1) * 6 + loop.index + 1}</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty video.poster && (video.poster.endsWith('.mp4') || video.poster.endsWith('.webm') || video.poster.contains('/video/'))}">
                                <video class="poster-thumb" muted>
                                    <source src="${pageContext.request.contextPath}/upload/${video.poster}" type="video/mp4">
                                </video>
                            </c:when>
                            <c:when test="${not empty video.poster && (video.poster.startsWith('http://') || video.poster.startsWith('https://'))}">
                                <img src="${video.poster}" class="poster-thumb" alt="poster">
                            </c:when>
                            <c:when test="${not empty video.poster}">
                                <img src="${pageContext.request.contextPath}/upload/${video.poster}" class="poster-thumb" alt="poster">
                            </c:when>
                            <c:otherwise>
                                <div class="poster-thumb-placeholder"><i class="fas fa-image"></i></div>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><strong>${video.videoId}</strong></td>
                    <td>${video.title}</td>
                    <td>
                        <c:if test="${video.category != null}">
                            <span class="badge bg-info">${video.category.categoryname}</span>
                        </c:if>
                    </td>
                    <td>${video.views}</td>
                    <td>
                        <c:choose>
                            <c:when test="${video.active}">
                                <span class="badge bg-success">Active</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">Inactive</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/videos/edit/${video.videoId}" class="btn btn-sm btn-warning" title="Sửa">
                            <i class="fas fa-edit"></i>
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/videos/delete/${video.videoId}"
                           class="btn btn-sm btn-danger" title="Xóa"
                           onclick="return confirm('Bạn có chắc chắn muốn xóa video này?');">
                            <i class="fas fa-trash"></i>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty videos}">
                <tr>
                    <td colspan="8" class="text-center text-muted py-4">
                        <i class="fas fa-inbox fa-2x mb-2 d-block"></i>
                        Không tìm thấy video nào.
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>

    <!-- Phân trang 6 video/trang -->
    <c:if test="${totalPages > 1}">
        <nav class="d-flex justify-content-center">
            <ul class="pagination">
                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/admin/videos?page=${currentPage - 1}&keyword=${keyword}">&laquo;</a>
                </li>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/admin/videos?page=${i}&keyword=${keyword}">${i}</a>
                    </li>
                </c:forEach>
                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/admin/videos?page=${currentPage + 1}&keyword=${keyword}">&raquo;</a>
                </li>
            </ul>
        </nav>
    </c:if>
</body>
</html>
