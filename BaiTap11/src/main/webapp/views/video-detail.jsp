<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<html>
<head>
    <title>Chi tiết Video - ${video.title}</title>
    <style>
        .video-detail-card { background: #fff; border-radius: 15px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); overflow: hidden; }
        .video-poster { width: 100%; max-height: 400px; object-fit: cover; background: #e9ecef; }
        .poster-placeholder-lg { width: 100%; height: 350px; background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); display: flex; align-items: center; justify-content: center; color: #666; font-size: 80px; }
        .video-detail-body { padding: 30px; }
        .video-detail-body h2 { font-weight: 700; color: #333; margin-bottom: 20px; }
        .detail-info { font-size: 15px; color: #555; }
        .detail-info p { margin-bottom: 10px; }
        .detail-info .label { font-weight: 600; color: #333; min-width: 130px; display: inline-block; }
        .stats-row { display: flex; gap: 20px; margin: 15px 0; }
        .stat-item { display: flex; align-items: center; gap: 8px; padding: 8px 16px; border-radius: 20px; font-weight: 600; }
        .stat-share { background: #e3f2fd; color: #1976d2; }
        .stat-like { background: #fce4ec; color: #c62828; }
        .stat-view { background: #e8f5e9; color: #2e7d32; }
        .description-box { background: #f8f9fa; padding: 20px; border-radius: 10px; margin-top: 20px; }
        .description-box h5 { font-weight: 700; color: #333; margin-bottom: 10px; }
    </style>
</head>
<body>
    <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
    <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left"></i> Quay lại trang chủ
        </a>
    </div>

    <div class="video-detail-card">
        <div class="row g-0">
            <!-- Poster bên trái -->
            <div class="col-md-5">
                <c:choose>
                    <c:when test="${not empty video.poster && (video.poster.endsWith('.mp4') || video.poster.endsWith('.webm') || video.poster.contains('/video/'))}">
                        <video controls class="video-poster" style="max-height: 400px; width: 100%; background: #000;">
                            <source src="${pageContext.request.contextPath}/upload/${video.poster}" type="video/mp4">
                            Trình duyệt không hỗ trợ thẻ video.
                        </video>
                    </c:when>
                    <c:when test="${not empty video.poster && (video.poster.startsWith('http://') || video.poster.startsWith('https://'))}">
                        <img src="${video.poster}" class="video-poster" alt="${video.title}">
                    </c:when>
                    <c:when test="${not empty video.poster}">
                        <img src="${pageContext.request.contextPath}/upload/${video.poster}" class="video-poster" alt="${video.title}">
                    </c:when>
                    <c:otherwise>
                        <div class="poster-placeholder-lg">
                            <i class="fas fa-video"></i>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Thông tin bên phải -->
            <div class="col-md-7">
                <div class="video-detail-body">
                    <h2>${video.title}</h2>

                    <div class="detail-info">
                        <p><span class="label"><i class="fas fa-hashtag"></i> Mã video:</span> ${video.videoId}</p>
                        <p><span class="label"><i class="fas fa-folder"></i> Category name:</span> ${video.category.categoryname}</p>
                        <p><span class="label"><i class="fas fa-eye"></i> View:</span> ${video.views}</p>
                    </div>

                    <div class="stats-row">
                        <div class="stat-item stat-view">
                            <i class="fas fa-eye"></i> ${video.views} lượt xem
                        </div>
                        <div class="stat-item stat-share">
                            <i class="fas fa-share"></i> Share(${shareCount})
                        </div>
                        <div class="stat-item stat-like">
                            <i class="fas fa-heart"></i> Like(${likeCount})
                        </div>
                    </div>

                    <div class="description-box">
                        <h5><i class="fas fa-info-circle"></i> Mô tả</h5>
                        <p>${video.description}</p>
                    </div>
                    <div class="d-flex align-items-center justify-content-between mt-4 p-3 border rounded">
                        <div>
                            <div class="fs-4 fw-bold text-danger"><fmt:formatNumber value="${video.price}" type="number"/> ₫</div>
                            <small class="text-muted">Tồn kho: ${video.stock}</small>
                        </div>
                        <form action="${pageContext.request.contextPath}/cart/add/${video.videoId}" method="post" class="d-flex gap-2">
                            <input type="hidden" name="_csrf" value="${fn:escapeXml(csrfToken)}">
                            <input type="hidden" name="returnUrl" value="/video/${video.videoId}">
                            <input class="form-control" style="width:85px" type="number" name="quantity" value="1" min="1" max="${video.stock}">
                            <button class="btn btn-primary" ${video.stock == null || video.stock <= 0 ? 'disabled' : ''}>
                                <i class="fas fa-cart-plus"></i> Thêm vào giỏ
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
