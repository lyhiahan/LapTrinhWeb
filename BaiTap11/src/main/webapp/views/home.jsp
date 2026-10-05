<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<html>
<head>
    <title>Trang Chủ - Video</title>
    <style>
        .category-section { margin-bottom: 40px; }
        .category-header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: #fff; padding: 12px 20px; border-radius: 10px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center; }
        .category-header h3 { margin: 0; font-weight: 700; }
        .category-header .badge { background: rgba(255,255,255,0.3); font-size: 16px; padding: 6px 15px; border-radius: 20px; }
        .video-card { border: none; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.1); transition: transform 0.3s, box-shadow 0.3s; height: 100%; }
        .video-card:hover { transform: translateY(-5px); box-shadow: 0 8px 25px rgba(0,0,0,0.2); }
        .video-card .card-img-top { height: 180px; object-fit: cover; background: #e9ecef; }
        .video-card .card-body { padding: 15px; }
        .video-card .card-title { font-weight: 600; font-size: 15px; margin-bottom: 8px; color: #333; }
        .video-info { font-size: 13px; color: #666; }
        .video-info .badge { font-size: 11px; }
        .video-stats { display: flex; gap: 10px; margin-top: 8px; }
        .video-stats span { font-size: 12px; color: #888; }
        .video-stats span i { margin-right: 3px; }
        .pagination-wrap { display: flex; justify-content: center; margin-top: 15px; }
        .poster-placeholder { width: 100%; height: 180px; background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); display: flex; align-items: center; justify-content: center; color: #666; font-size: 48px; }
        .product-price { color: #dc3545; font-size: 1.1rem; font-weight: 700; }
    </style>
</head>
<body>
    <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
    <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
    <h2 class="mb-4"><i class="fas fa-fire text-danger"></i> Tất cả Video theo danh mục</h2>

    <c:forEach var="catData" items="${categoryDataList}">
        <div class="category-section">
            <!-- Câu 5: Category Name (Số lượng video) -->
            <div class="category-header">
                <h3><i class="fas fa-folder-open"></i> ${catData.category.categoryname}</h3>
                <span class="badge">${catData.videoCount} video</span>
            </div>

            <div class="row">
                <c:forEach var="video" items="${catData.videos}">
                    <div class="col-md-4 mb-3">
                        <div class="card video-card">
                            <a href="${pageContext.request.contextPath}/video/${video.videoId}">
                                <c:choose>
                                    <c:when test="${not empty video.poster && (video.poster.endsWith('.mp4') || video.poster.endsWith('.webm') || video.poster.contains('/video/'))}">
                                        <video class="card-img-top" style="height: 180px; object-fit: cover; background: #000;" muted onmouseover="this.play()" onmouseout="this.pause()">
                                            <source src="${pageContext.request.contextPath}/upload/${video.poster}" type="video/mp4">
                                        </video>
                                    </c:when>
                                    <c:when test="${not empty video.poster && (video.poster.startsWith('http://') || video.poster.startsWith('https://'))}">
                                        <img src="${video.poster}" class="card-img-top" alt="${video.title}">
                                    </c:when>
                                    <c:when test="${not empty video.poster}">
                                        <img src="${pageContext.request.contextPath}/upload/${video.poster}" class="card-img-top" alt="${video.title}">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="poster-placeholder">
                                            <i class="fas fa-video"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </a>
                            <div class="card-body">
                                <h5 class="card-title">
                                    <a href="${pageContext.request.contextPath}/video/${video.videoId}" class="text-decoration-none text-dark">
                                        ${video.title}
                                    </a>
                                </h5>
                                <div class="video-info">
                                    <p class="mb-1"><strong>Mã video:</strong> ${video.videoId}</p>
                                    <p class="mb-1"><strong>Category:</strong> ${video.category.categoryname}</p>
                                    <p class="mb-1"><strong>View:</strong> ${video.views}</p>
                                </div>
                                <div class="video-stats">
                                    <span><i class="fas fa-share text-primary"></i> Share(${catData.shareCountMap[video.videoId]})</span>
                                    <span><i class="fas fa-heart text-danger"></i> Like(${catData.likeCountMap[video.videoId]})</span>
                                </div>
                                <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top">
                                    <div>
                                        <div class="product-price"><fmt:formatNumber value="${video.price}" type="number"/> ₫</div>
                                        <small class="text-muted">Còn ${video.stock} sản phẩm</small>
                                    </div>
                                    <form action="${pageContext.request.contextPath}/cart/add/${video.videoId}" method="post">
                                        <input type="hidden" name="_csrf" value="${fn:escapeXml(csrfToken)}">
                                        <input type="hidden" name="returnUrl" value="/home?catId=${catData.category.categoryId}&amp;catPage=${catData.currentPage}">
                                        <button class="btn btn-primary btn-sm" ${video.stock == null || video.stock <= 0 ? 'disabled' : ''}>
                                            <i class="fas fa-cart-plus"></i> Thêm
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Phân trang 3 video/trang cho mỗi category -->
            <c:if test="${catData.totalPages > 1}">
                <div class="pagination-wrap">
                    <nav>
                        <ul class="pagination pagination-sm">
                            <li class="page-item ${catData.currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/home?catId=${catData.category.categoryId}&catPage=${catData.currentPage - 1}">&laquo;</a>
                            </li>
                            <c:forEach begin="1" end="${catData.totalPages}" var="i">
                                <li class="page-item ${catData.currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/home?catId=${catData.category.categoryId}&catPage=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${catData.currentPage == catData.totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/home?catId=${catData.category.categoryId}&catPage=${catData.currentPage + 1}">&raquo;</a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </c:if>
        </div>
    </c:forEach>
</body>
</html>
