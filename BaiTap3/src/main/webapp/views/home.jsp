<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Trang Chủ - ShoppingAdmin</title>
<link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
<style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body { font-family: 'Outfit', sans-serif; background: #f8fafc; color: #0f172a; }

    /* Navbar */
    .navbar {
        background: #ffffff;
        border-bottom: 1px solid #e2e8f0;
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
        transition: color 0.2s; padding: 4px 0;
    }
    .nav-links a:hover, .nav-links a.active { color: #4f46e5; }
    .nav-links .btn-login {
        background: linear-gradient(90deg, #4f46e5, #7c3aed); color: #fff;
        padding: 8px 22px; border-radius: 10px; font-weight: 600;
        transition: all 0.3s;
    }
    .nav-links .btn-login:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(79,70,229,0.25); }

    /* Hero */
    .hero {
        background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 50%, #a855f7 100%);
        padding: 70px 40px; text-align: center; color: #fff;
    }
    .hero h1 { font-size: 2.8rem; font-weight: 700; margin-bottom: 14px; }
    .hero p { font-size: 1.15rem; opacity: 0.9; max-width: 600px; margin: 0 auto 30px; }
    .hero .btn-hero {
        display: inline-block; padding: 14px 36px;
        background: #fff; color: #4f46e5; border-radius: 12px;
        font-weight: 600; font-size: 1rem; text-decoration: none;
        transition: all 0.3s;
    }
    .hero .btn-hero:hover { transform: translateY(-3px); box-shadow: 0 8px 24px rgba(0,0,0,0.15); }

    /* Section */
    .section { padding: 50px 40px; max-width: 1280px; margin: 0 auto; }
    .section-title {
        font-size: 1.6rem; font-weight: 700; margin-bottom: 8px; color: #0f172a;
    }
    .section-subtitle { color: #64748b; font-size: 0.95rem; margin-bottom: 30px; }

    /* Product Grid */
    .product-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
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
        width: 100%; height: 200px; overflow: hidden;
        background: #f1f5f9;
    }
    .product-card .img-wrap img {
        width: 100%; height: 100%; object-fit: cover;
        transition: transform 0.4s;
    }
    .product-card:hover .img-wrap img { transform: scale(1.08); }
    .product-card .info { padding: 16px; flex: 1; display: flex; flex-direction: column; }
    .product-card .cate-tag {
        font-size: 0.75rem; font-weight: 600; color: #4f46e5;
        background: rgba(79,70,229,0.08); border-radius: 6px;
        padding: 3px 8px; display: inline-block; margin-bottom: 8px; width: fit-content;
    }
    .product-card .name {
        font-size: 1rem; font-weight: 600; color: #0f172a;
        margin-bottom: 8px;
        display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;
    }
    .product-card .price {
        font-size: 1.1rem; font-weight: 700; color: #dc2626;
        margin-top: auto;
    }

    /* Footer */
    .site-footer {
        background: #0f172a; color: rgba(255,255,255,0.7);
        padding: 40px; text-align: center; font-size: 0.9rem;
        margin-top: 40px;
    }
    .site-footer a { color: #818cf8; text-decoration: none; }

    @media (max-width: 768px) {
        .navbar { padding: 0 20px; }
        .hero { padding: 40px 20px; }
        .hero h1 { font-size: 1.8rem; }
        .section { padding: 30px 20px; }
        .nav-links { gap: 16px; }
    }
</style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/home" class="logo">Shopping<span>Admin</span></a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/home" class="active">Trang chủ</a>
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

    <!-- Hero -->
    <section class="hero">
        <h1>Chào mừng đến ShoppingAdmin</h1>
        <p>Khám phá các sản phẩm chất lượng với giá tốt nhất. Mua sắm dễ dàng, nhanh chóng và tiện lợi.</p>
        <a href="${pageContext.request.contextPath}/product" class="btn-hero">
            <i class="fas fa-shopping-bag" style="margin-right: 8px;"></i>Xem tất cả sản phẩm
        </a>
    </section>

    <!-- Newest Products -->
    <section class="section">
        <h2 class="section-title"><i class="fas fa-fire" style="color: #f59e0b; margin-right: 8px;"></i>Sản phẩm mới nhất</h2>
        <p class="section-subtitle">10 sản phẩm mới được thêm gần đây</p>

        <c:if test="${empty newestProducts}">
            <div style="text-align: center; color: #94a3b8; padding: 60px 0; font-size: 1.1rem;">
                <i class="fas fa-box-open" style="font-size: 3rem; margin-bottom: 16px; display: block; color: #cbd5e1;"></i>
                Chưa có sản phẩm nào. Hãy thêm sản phẩm từ trang quản trị!
            </div>
        </c:if>

        <div class="product-grid">
            <c:forEach items="${newestProducts}" var="p">
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
    </section>

    <!-- Footer -->
    <footer class="site-footer">
        <p>&copy; 2026 ShoppingAdmin. Developed by <a href="#">Ly Gia Han</a></p>
    </footer>
</body>
</html>
