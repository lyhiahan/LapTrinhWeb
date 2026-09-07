<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Sản Phẩm - ShoppingAdmin</title>
<link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
<style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body { font-family: 'Outfit', sans-serif; background: #f8fafc; color: #0f172a; }

    .navbar {
        background: #ffffff; border-bottom: 1px solid #e2e8f0;
        padding: 0 40px; height: 64px;
        display: flex; justify-content: space-between; align-items: center;
        position: sticky; top: 0; z-index: 100;
        box-shadow: 0 1px 3px rgba(0,0,0,0.04);
    }
    .navbar .logo { font-size: 22px; font-weight: 700; color: #4f46e5; text-decoration: none; }
    .navbar .logo span { color: #0f172a; }
    .nav-links { display: flex; align-items: center; gap: 28px; }
    .nav-links a {
        text-decoration: none; color: #475569; font-size: 15px; font-weight: 500;
        transition: color 0.2s;
    }
    .nav-links a:hover, .nav-links a.active { color: #4f46e5; }
    .nav-links .btn-login {
        background: linear-gradient(90deg, #4f46e5, #7c3aed); color: #fff;
        padding: 8px 22px; border-radius: 10px; font-weight: 600;
        transition: all 0.3s;
    }
    .nav-links .btn-login:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(79,70,229,0.25); }

    .page-header {
        background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
        padding: 40px; text-align: center; color: #fff;
    }
    .page-header h1 { font-size: 2rem; font-weight: 700; margin-bottom: 8px; }
    .page-header p { color: rgba(255,255,255,0.7); font-size: 1rem; }

    .section { padding: 40px; max-width: 1280px; margin: 0 auto; }

    .product-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 24px;
    }
    .product-card {
        background: #fff; border-radius: 16px;
        border: 1px solid #e2e8f0;
        overflow: hidden; transition: all 0.3s;
        text-decoration: none; color: inherit;
        display: flex; flex-direction: column;
    }
    .product-card:hover {
        transform: translateY(-6px);
        box-shadow: 0 12px 32px rgba(15,23,42,0.08);
        border-color: rgba(79,70,229,0.2);
    }
    .product-card .img-wrap {
        width: 100%; height: 220px; overflow: hidden; background: #f1f5f9;
    }
    .product-card .img-wrap img {
        width: 100%; height: 100%; object-fit: cover;
        transition: transform 0.4s;
    }
    .product-card:hover .img-wrap img { transform: scale(1.08); }
    .product-card .info { padding: 18px; flex: 1; display: flex; flex-direction: column; }
    .product-card .cate-tag {
        font-size: 0.75rem; font-weight: 600; color: #4f46e5;
        background: rgba(79,70,229,0.08); border-radius: 6px;
        padding: 3px 8px; display: inline-block; margin-bottom: 8px; width: fit-content;
    }
    .product-card .name {
        font-size: 1.05rem; font-weight: 600; color: #0f172a;
        margin-bottom: 10px;
        display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;
    }
    .product-card .price {
        font-size: 1.15rem; font-weight: 700; color: #dc2626;
        margin-top: auto;
    }

    /* Pagination */
    .pagination {
        display: flex; justify-content: center; align-items: center;
        gap: 8px; margin-top: 40px;
    }
    .pagination a, .pagination span {
        display: inline-flex; justify-content: center; align-items: center;
        min-width: 40px; height: 40px; padding: 0 12px;
        border-radius: 10px; font-size: 14px; font-weight: 600;
        text-decoration: none; transition: all 0.2s;
    }
    .pagination a {
        background: #fff; color: #475569;
        border: 1px solid #e2e8f0;
    }
    .pagination a:hover {
        background: #f1f5f9; color: #4f46e5;
        border-color: rgba(79,70,229,0.3);
    }
    .pagination .active-page {
        background: linear-gradient(90deg, #4f46e5, #7c3aed);
        color: #fff; border: none;
    }
    .pagination .disabled {
        color: #cbd5e1; pointer-events: none;
        border-color: #f1f5f9;
    }

    .empty-state {
        text-align: center; color: #94a3b8; padding: 80px 20px; font-size: 1.1rem;
    }
    .empty-state i { font-size: 3.5rem; margin-bottom: 16px; display: block; color: #cbd5e1; }

    .site-footer {
        background: #0f172a; color: rgba(255,255,255,0.7);
        padding: 40px; text-align: center; font-size: 0.9rem; margin-top: 40px;
    }
    .site-footer a { color: #818cf8; text-decoration: none; }

    @media (max-width: 900px) {
        .product-grid { grid-template-columns: repeat(2, 1fr); }
        .navbar { padding: 0 20px; }
        .section { padding: 30px 20px; }
    }
    @media (max-width: 600px) {
        .product-grid { grid-template-columns: 1fr; }
    }
</style>
</head>
<body>


    <div class="page-header">
        <h1><i class="fas fa-shopping-bag" style="margin-right: 10px;"></i>Tất Cả Sản Phẩm</h1>
        <p>Tổng cộng ${totalProducts} sản phẩm — Trang ${currentPage} / ${totalPages > 0 ? totalPages : 1}</p>
    </div>

    <section class="section">
        <c:if test="${empty productList}">
            <div class="empty-state">
                <i class="fas fa-box-open"></i>
                Chưa có sản phẩm nào.
            </div>
        </c:if>

        <div class="product-grid">
            <c:forEach items="${productList}" var="p">
                <a href="${pageContext.request.contextPath}/product/detail?id=${p.productId}" class="product-card">
                    <div class="img-wrap">
                        <c:choose>
                            <c:when test="${p.image != null && p.image.startsWith('http')}">
                                <img src="${p.image}" alt="${p.productName}"/>
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/image?fname=${p.image}" alt="${p.productName}"/>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="info">
                        <c:if test="${p.category != null}">
                            <span class="cate-tag">${p.category.name}</span>
                        </c:if>
                        <div class="name">${p.productName}</div>
                        <div class="price"><fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/> đ</div>
                    </div>
                </a>
            </c:forEach>
        </div>

        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/product?page=${currentPage - 1}"><i class="fas fa-chevron-left"></i></a>
                    </c:when>
                    <c:otherwise>
                        <span class="disabled"><i class="fas fa-chevron-left"></i></span>
                    </c:otherwise>
                </c:choose>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="active-page">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/product?page=${i}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/product?page=${currentPage + 1}"><i class="fas fa-chevron-right"></i></a>
                    </c:when>
                    <c:otherwise>
                        <span class="disabled"><i class="fas fa-chevron-right"></i></span>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </section>


</body>
</html>
