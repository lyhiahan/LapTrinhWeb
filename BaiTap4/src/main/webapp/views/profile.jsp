<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Chỉnh sửa hồ sơ - ShoppingAdmin</title>
<style>
    /* === Profile Page Styles === */
    .profile-wrapper {
        max-width: 720px;
        margin: 40px auto;
        padding: 0 20px;
        animation: fadeUp 0.5s ease;
    }
    @keyframes fadeUp {
        from { opacity: 0; transform: translateY(16px); }
        to { opacity: 1; transform: translateY(0); }
    }

    .profile-card {
        background: #ffffff;
        border: 1px solid #e2e8f0;
        border-radius: 20px;
        box-shadow: 0 8px 24px rgba(15, 23, 42, 0.06);
        overflow: hidden;
    }

    .profile-header {
        background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 50%, #a855f7 100%);
        padding: 36px 40px;
        text-align: center;
        position: relative;
    }
    .profile-header h2 {
        color: #ffffff;
        font-size: 1.6rem;
        font-weight: 700;
        margin-bottom: 4px;
        background: none;
        -webkit-text-fill-color: #ffffff;
    }
    .profile-header p {
        color: rgba(255,255,255,0.8);
        font-size: 0.9rem;
    }

    .profile-body {
        padding: 36px 40px;
    }

    /* Avatar Upload Area */
    .avatar-upload-section {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 32px;
        position: relative;
    }
    .avatar-preview-wrapper {
        position: relative;
        cursor: pointer;
    }
    .avatar-preview {
        width: 110px;
        height: 110px;
        border-radius: 50%;
        object-fit: cover;
        border: 4px solid #e2e8f0;
        box-shadow: 0 4px 16px rgba(0,0,0,0.08);
        transition: all 0.3s;
        background: linear-gradient(135deg, #4f46e5, #7c3aed);
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .avatar-preview img {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        object-fit: cover;
    }
    .avatar-initial {
        width: 110px;
        height: 110px;
        border-radius: 50%;
        background: linear-gradient(135deg, #4f46e5, #7c3aed);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 2.8rem;
        font-weight: 700;
        color: #ffffff;
        border: 4px solid #e2e8f0;
        box-shadow: 0 4px 16px rgba(0,0,0,0.08);
    }
    .avatar-overlay {
        position: absolute;
        bottom: 4px; right: 4px;
        width: 34px; height: 34px;
        background: linear-gradient(135deg, #4f46e5, #7c3aed);
        border-radius: 50%;
        display: flex; align-items: center; justify-content: center;
        color: #fff; font-size: 14px;
        border: 3px solid #fff;
        box-shadow: 0 2px 8px rgba(79,70,229,0.3);
        transition: transform 0.2s;
    }
    .avatar-preview-wrapper:hover .avatar-overlay {
        transform: scale(1.1);
    }
    .avatar-upload-hint {
        margin-top: 10px;
        font-size: 0.82rem;
        color: #94a3b8;
    }
    #avatarInput {
        display: none;
    }

    /* Form */
    .form-section-title {
        font-size: 0.8rem;
        font-weight: 700;
        color: #94a3b8;
        text-transform: uppercase;
        letter-spacing: 0.08em;
        margin-bottom: 16px;
        padding-bottom: 8px;
        border-bottom: 1px solid #f1f5f9;
    }

    .form-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 20px;
        margin-bottom: 20px;
    }
    .form-row.single {
        grid-template-columns: 1fr;
    }

    .form-group {
        display: flex;
        flex-direction: column;
    }
    .form-group label {
        font-size: 0.83rem;
        font-weight: 600;
        color: #475569;
        margin-bottom: 6px;
        text-transform: uppercase;
        letter-spacing: 0.04em;
    }
    .form-group input[type="text"],
    .form-group input[type="tel"] {
        width: 100%;
        padding: 12px 16px;
        border: 1px solid #e2e8f0;
        border-radius: 12px;
        font-size: 0.95rem;
        font-family: 'Outfit', sans-serif;
        color: #0f172a;
        background: #f8fafc;
        outline: none;
        transition: all 0.3s;
    }
    .form-group input:focus {
        border-color: #4f46e5;
        box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.12);
        background: #ffffff;
    }
    .form-group input[readonly] {
        background: #f1f5f9;
        color: #94a3b8;
        cursor: not-allowed;
        border-color: #e2e8f0;
    }
    .form-group input[readonly]:focus {
        border-color: #e2e8f0;
        box-shadow: none;
    }

    /* Info badges */
    .info-badge {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        padding: 6px 14px;
        border-radius: 20px;
        font-size: 0.78rem;
        font-weight: 600;
    }
    .info-badge.role-admin {
        background: rgba(217, 119, 6, 0.1);
        color: #b45309;
        border: 1px solid rgba(217, 119, 6, 0.2);
    }
    .info-badge.role-member {
        background: rgba(5, 150, 105, 0.1);
        color: #047857;
        border: 1px solid rgba(5, 150, 105, 0.2);
    }

    /* Buttons */
    .btn-submit {
        width: 100%;
        padding: 14px;
        border: none;
        border-radius: 12px;
        background: linear-gradient(90deg, #4f46e5 0%, #7c3aed 100%);
        color: #ffffff;
        font-size: 1rem;
        font-weight: 600;
        font-family: 'Outfit', sans-serif;
        cursor: pointer;
        transition: all 0.3s;
        box-shadow: 0 4px 12px rgba(79, 70, 229, 0.2);
        margin-top: 10px;
    }
    .btn-submit:hover {
        background: linear-gradient(90deg, #4338ca 0%, #6d28d9 100%);
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(124, 58, 237, 0.3);
    }
    .btn-submit:active {
        transform: translateY(0);
    }

    /* Alerts */
    .alert-success {
        background: rgba(5, 150, 105, 0.08);
        border: 1px solid rgba(5, 150, 105, 0.2);
        color: #047857;
        border-radius: 12px;
        padding: 14px 18px;
        font-size: 0.9rem;
        font-weight: 500;
        margin-bottom: 24px;
        display: flex;
        align-items: center;
        gap: 10px;
        animation: fadeUp 0.4s ease;
    }
    .alert-success i {
        font-size: 18px;
    }
    .alert-error {
        background: rgba(220, 38, 38, 0.08);
        border: 1px solid rgba(220, 38, 38, 0.2);
        color: #b91c1c;
        border-radius: 12px;
        padding: 14px 18px;
        font-size: 0.9rem;
        font-weight: 500;
        margin-bottom: 24px;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    @media (max-width: 600px) {
        .profile-wrapper { margin: 20px auto; }
        .profile-body { padding: 24px 20px; }
        .profile-header { padding: 28px 20px; }
        .form-row { grid-template-columns: 1fr; }
    }
</style>
</head>
<body>
    <div class="profile-wrapper">
        <div class="profile-card">
            <div class="profile-header">
                <h2><i class="fas fa-user-pen" style="margin-right: 10px;"></i>Chỉnh sửa hồ sơ</h2>
                <p>Cập nhật thông tin cá nhân của bạn</p>
            </div>

            <div class="profile-body">
                <!-- Success Message -->
                <c:if test="${param.message == 'success'}">
                    <div class="alert-success">
                        <i class="fas fa-check-circle"></i>
                        Cập nhật hồ sơ thành công!
                    </div>
                </c:if>

                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert-error">
                        <i class="fas fa-exclamation-circle"></i>
                        ${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/profile/update" method="post" enctype="multipart/form-data">
                    <!-- Avatar Upload -->
                    <div class="avatar-upload-section">
                        <div class="avatar-preview-wrapper" onclick="document.getElementById('avatarInput').click()">
                            <c:choose>
                                <c:when test="${not empty sessionScope.account.avatar}">
                                    <div class="avatar-preview">
                                        <img id="avatarPreviewImg" src="${pageContext.request.contextPath}/image?fname=${sessionScope.account.avatar}" alt="Avatar"/>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div id="avatarInitialDiv" class="avatar-initial">
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.account.fullName}">
                                                ${fn:substring(sessionScope.account.fullName, 0, 1).toUpperCase()}
                                            </c:when>
                                            <c:otherwise>
                                                ${fn:substring(sessionScope.account.userName, 0, 1).toUpperCase()}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <div class="avatar-overlay">
                                <i class="fas fa-camera"></i>
                            </div>
                        </div>
                        <div class="avatar-upload-hint">Nhấn vào ảnh để thay đổi avatar (tối đa 5MB)</div>
                        <input type="file" id="avatarInput" name="avatar" accept="image/*" onchange="previewAvatar(this)"/>
                    </div>

                    <!-- Editable Info -->
                    <div class="form-section-title">
                        <i class="fas fa-pen" style="margin-right: 6px;"></i>Thông tin có thể chỉnh sửa
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="fullname"><i class="fas fa-user" style="margin-right: 4px;"></i>Họ và tên</label>
                            <input type="text" id="fullname" name="fullname" value="${sessionScope.account.fullName}" placeholder="Nhập họ và tên" required/>
                        </div>
                        <div class="form-group">
                            <label for="phone"><i class="fas fa-phone" style="margin-right: 4px;"></i>Số điện thoại</label>
                            <input type="tel" id="phone" name="phone" value="${sessionScope.account.phone}" placeholder="Nhập số điện thoại"/>
                        </div>
                    </div>

                    <!-- Read-only Info -->
                    <div class="form-section-title" style="margin-top: 28px;">
                        <i class="fas fa-lock" style="margin-right: 6px;"></i>Thông tin không thể chỉnh sửa
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>Tài khoản</label>
                            <input type="text" value="${sessionScope.account.userName}" readonly/>
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <input type="text" value="${sessionScope.account.email}" readonly/>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>Vai trò</label>
                            <div style="padding-top: 6px;">
                                <c:choose>
                                    <c:when test="${sessionScope.account.roleid == 1}">
                                        <span class="info-badge role-admin">
                                            <i class="fas fa-shield-halved"></i> Quản trị viên
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="info-badge role-member">
                                            <i class="fas fa-user-check"></i> Thành viên
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Ngày tham gia</label>
                            <input type="text" value="${sessionScope.account.createdDate}" readonly/>
                        </div>
                    </div>

                    <button type="submit" class="btn-submit">
                        <i class="fas fa-save" style="margin-right: 8px;"></i>Lưu thay đổi
                    </button>
                </form>
            </div>
        </div>
    </div>

    <script>
        function previewAvatar(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    // Nếu đang hiện initial, thay bằng ảnh
                    var initialDiv = document.getElementById('avatarInitialDiv');
                    var previewImg = document.getElementById('avatarPreviewImg');

                    if (initialDiv) {
                        // Thay thế initial div bằng preview img
                        var wrapper = initialDiv.parentElement;
                        initialDiv.remove();

                        var previewDiv = document.createElement('div');
                        previewDiv.className = 'avatar-preview';
                        var img = document.createElement('img');
                        img.id = 'avatarPreviewImg';
                        img.src = e.target.result;
                        img.alt = 'Avatar';
                        previewDiv.appendChild(img);
                        wrapper.insertBefore(previewDiv, wrapper.firstChild);
                    } else if (previewImg) {
                        previewImg.src = e.target.result;
                    }
                }
                reader.readAsDataURL(input.files[0]);
            }
        }
    </script>
</body>
</html>
