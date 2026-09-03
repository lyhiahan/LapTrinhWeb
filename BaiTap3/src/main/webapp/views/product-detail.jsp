<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${product.productName} - ShoppingAdmin</title>
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
    .nav-links a:hover { color: #4f46e5; }
    .nav-links .btn-login {
        background: linear-gradient(90deg, #4f46e5, #7c3aed); color: #fff;
        padding: 8px 22px; border-radius: 10px; font-weight: 600;
        transition: all 0.3s;
    }
    .nav-links .btn-login:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(79,70,229,0.25); }

    .detail-container {
        max-width: 1000px; margin: 40px auto; padding: 0 40px;
    }

    .breadcrumb {
        display: flex; align-items: center; gap: 8px;
        font-size: 0.9rem; color: #64748b; margin-bottom: 30px;
    }
    .breadcrumb a { color: #4f46e5; text-decoration: none; font-weight: 500; }
    .breadcrumb a:hover { text-decoration: underline; }

    .product-detail {
        background: #fff; border-radius: 20px;
        border: 1px solid #e2e8f0;
        box-shadow: 0 8px 24px rgba(15,23,42,0.05);
        display: flex; overflow: hidden;
    }
    .product-image {
        width: 45%; min-height: 400px; background: #f1f5f9;
        display: flex; align-items: center; justify-content: center;
    }
    .product-image img {
        width: 100%; height: 100%; object-fit: cover;
    }
    .product-info {
        flex: 1; padding: 40px;
        display: flex; flex-direction: column;
    }
    .product-info .cate-tag {
        font-size: 0.8rem; font-weight: 600; color: #4f46e5;
        background: rgba(79,70,229,0.08); border-radius: 8px;
        padding: 5px 12px; display: inline-block; margin-bottom: 16px; width: fit-content;
    }
    .product-info h1 {
        font-size: 1.8rem; font-weight: 700; color: #0f172a;
        margin-bottom: 16px; line-height: 1.3;
    }
    .product-info .price-tag {
        font-size: 2rem; font-weight: 700; color: #dc2626;
        margin-bottom: 24px;
    }
    .product-info .desc-label {
        font-size: 0.85rem; font-weight: 600; color: #64748b;
        text-transform: uppercase; letter-spacing: 0.05em;
        margin-bottom: 10px;
    }
    .product-info .description {
        font-size: 0.95rem; color: #475569; line-height: 1.7;
        margin-bottom: 30px; flex: 1;
    }
    .product-info .meta-info {
        display: flex; gap: 24px; font-size: 0.85rem; color: #94a3b8;
        border-top: 1px solid #e2e8f0; padding-top: 20px;
    }
    .product-info .meta-info i { margin-right: 6px; }

    .btn-back {
        display: inline-flex; align-items: center; gap: 8px;
        padding: 12px 28px; border-radius: 12px;
        background: linear-gradient(90deg, #4f46e5, #7c3aed);
        color: #fff; text-decoration: none; font-weight: 600;
        font-size: 0.95rem; transition: all 0.3s;
        margin-top: 30px; width: fit-content;
    }
    .btn-back:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(79,70,229,0.25);
    }

    .site-footer {
        background: #0f172a; color: rgba(255,255,255,0.7);
        padding: 40px; text-align: center; font-size: 0.9rem; margin-top: 60px;
    }
    .site-footer a { color: #818cf8; text-decoration: none; }

    @media (max-width: 768px) {
        .product-detail { flex-direction: column; }
        .product-image { width: 100%; min-height: 280px; }
        .detail-container { padding: 0 20px; }
        .navbar { padding: 0 20px; }
    }
</style>
</head>
<body>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/home" class="logo">Shopping<span>Admin</span></a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
            <a href="${pageContext.request.contextPath}/product">Sản phẩm</a>
            <c:choose>
                <c:when test="${not empty sessionScope.account}">
                    <a href="${pageContext.request.contextPath}/waiting">
                        <i class="fas fa-user-circle"></i> ${sessionScope.account.fullName}
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn-login" style="background: linear-gradient(90deg, #dc2626, #ef4444);">
                        Đăng xuất
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="btn-login">Đăng nhập</a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>

    <div class="detail-container">
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
            <span>/</span>
            <a href="${pageContext.request.contextPath}/product">Sản phẩm</a>
            <span>/</span>
            <span>${product.productName}</span>
        </div>

        <div class="product-detail">
            <div class="product-image">
                <c:choose>
                    <c:when test="${product.image != null && product.image.startsWith('http')}">
                        <img src="${product.image}" alt="${product.productName}"/>
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/image?fname=${product.image}" alt="${product.productName}"/>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="product-info">
                <c:if test="${product.category != null}">
                    <span class="cate-tag"><i class="fas fa-tag" style="margin-right: 4px;"></i>${product.category.name}</span>
                </c:if>
                <h1>${product.productName}</h1>
                <div class="price-tag"><fmt:formatNumber value="${product.price}" type="number" groupingUsed="true"/> đ</div>

                <c:if test="${not empty product.description}">
                    <div class="desc-label">Mô tả sản phẩm</div>
                    <div class="description">${product.description}</div>
                </c:if>

                <div class="meta-info">
                    <span><i class="fas fa-calendar-alt"></i>Ngày đăng: ${product.createdDate}</span>
                    <c:if test="${product.category != null}">
                        <span><i class="fas fa-folder"></i>Danh mục: ${product.category.name}</span>
                    </c:if>
                </div>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/product" class="btn-back">
            <i class="fas fa-arrow-left"></i> Quay lại danh sách
        </a>
    </div>

    <footer class="site-footer">
        <p>&copy; 2026 ShoppingAdmin. Developed by <a href="#">Ly Gia Han</a></p>
    </footer>
</body>
</html>
