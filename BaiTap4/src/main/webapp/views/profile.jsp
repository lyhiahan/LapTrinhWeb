<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hồ sơ cá nhân - ShoppingAdmin</title>
</head>
<body>
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-lg-8 col-xl-7">
                <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                    <!-- Header Card -->
                    <div class="p-4 text-white text-center" style="background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%);">
                        <h4 class="fw-bold mb-1"><i class="fas fa-user-pen me-2"></i>Hồ Sơ Cá Nhân</h4>
                        <p class="small text-white-50 mb-0">Quản lý và cập nhật thông tin tài khoản của bạn</p>
                    </div>

                    <div class="card-body p-4 p-sm-5">
                        <!-- Thông báo thành công / lỗi -->
                        <c:if test="${param.message == 'success'}">
                            <div class="alert alert-success alert-dismissible fade show rounded-3 small py-2 mb-4" role="alert">
                                <i class="fas fa-circle-check me-2"></i> Cập nhật thông tin hồ sơ thành công!
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show rounded-3 small py-2 mb-4" role="alert">
                                <i class="fas fa-circle-exclamation me-2"></i> ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/profile/update" method="post" enctype="multipart/form-data" class="needs-validation" novalidate id="profileForm">
                            <!-- Avatar Upload Section -->
                            <div class="d-flex flex-column align-items-center mb-4 text-center">
                                <div class="position-relative d-inline-block cursor-pointer" onclick="document.getElementById('avatarInput').click()" style="cursor: pointer;">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.account.avatar}">
                                            <img id="avatarPreviewImg"
                                                 src="${pageContext.request.contextPath}/image?fname=${sessionScope.account.avatar}"
                                                 alt="Avatar"
                                                 class="rounded-circle border border-3 border-light shadow-sm object-fit-cover"
                                                 style="width: 105px; height: 105px;"/>
                                        </c:when>
                                        <c:otherwise>
                                            <div id="avatarInitialDiv"
                                                 class="rounded-circle text-white shadow-sm d-flex align-items-center justify-content-center fw-bold fs-1"
                                                 style="width: 105px; height: 105px; background: linear-gradient(135deg, #4f46e5, #7c3aed);">
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

                                    <div class="position-absolute bottom-0 end-0 bg-primary text-white rounded-circle p-2 shadow-sm d-flex align-items-center justify-content-center border border-2 border-white"
                                         style="width: 32px; height: 32px; font-size: 12px;">
                                        <i class="fas fa-camera"></i>
                                    </div>
                                </div>
                                <div class="small text-muted mt-2">Bấm vào ảnh đại diện để thay đổi (tối đa 5MB)</div>
                                <input type="file" id="avatarInput" name="avatar" class="d-none" accept="image/png, image/jpeg, image/jpg, image/webp, image/gif" onchange="handleAvatarChange(this)"/>
                                <div class="small text-danger mt-1 d-none" id="avatarError"></div>
                            </div>

                            <div class="text-uppercase small fw-bold text-muted border-bottom pb-2 mb-3">
                                <i class="fas fa-user me-1"></i> Thông tin chỉnh sửa
                            </div>

                            <div class="row g-3 mb-4">
                                <!-- Họ tên -->
                                <div class="col-sm-6">
                                    <label for="fullname" class="form-label fw-semibold text-dark small">Họ và tên <span class="text-danger">*</span></label>
                                    <div class="input-group has-validation">
                                        <span class="input-group-text bg-light text-muted border-end-0"><i class="fas fa-id-badge"></i></span>
                                        <input type="text"
                                               class="form-control border-start-0 ps-1"
                                               id="fullname"
                                               name="fullname"
                                               value="${sessionScope.account.fullName}"
                                               placeholder="Nhập họ và tên"
                                               minlength="2"
                                               maxlength="50"
                                               required>
                                        <div class="invalid-feedback">Họ và tên không được để trống (từ 2 đến 50 ký tự).</div>
                                    </div>
                                </div>

                                <!-- Số điện thoại -->
                                <div class="col-sm-6">
                                    <label for="phone" class="form-label fw-semibold text-dark small">Số điện thoại <span class="text-danger">*</span></label>
                                    <div class="input-group has-validation">
                                        <span class="input-group-text bg-light text-muted border-end-0"><i class="fas fa-phone"></i></span>
                                        <input type="tel"
                                               class="form-control border-start-0 ps-1"
                                               id="phone"
                                               name="phone"
                                               value="${sessionScope.account.phone}"
                                               placeholder="Ví dụ: 0912345678"
                                               pattern="^(0[3|5|7|8|9])[0-9]{8}$"
                                               required>
                                        <div class="invalid-feedback">Số điện thoại phải gồm 10 chữ số hợp lệ của Việt Nam.</div>
                                    </div>
                                </div>
                            </div>

                            <div class="text-uppercase small fw-bold text-muted border-bottom pb-2 mb-3">
                                <i class="fas fa-lock me-1"></i> Thông tin bảo mật & hệ thống
                            </div>

                            <div class="row g-3 mb-4">
                                <div class="col-sm-6">
                                    <label class="form-label fw-semibold text-muted small">Tên tài khoản</label>
                                    <input type="text" class="form-control bg-light text-muted" value="${sessionScope.account.userName}" readonly disabled>
                                </div>

                                <div class="col-sm-6">
                                    <label class="form-label fw-semibold text-muted small">Địa chỉ Email</label>
                                    <input type="text" class="form-control bg-light text-muted" value="${sessionScope.account.email}" readonly disabled>
                                </div>

                                <div class="col-sm-6">
                                    <label class="form-label fw-semibold text-muted small">Vai trò</label>
                                    <div>
                                        <c:choose>
                                            <c:when test="${sessionScope.account.roleid == 1}">
                                                <span class="badge bg-danger-subtle text-danger border border-danger-subtle rounded-pill px-3 py-2 fw-medium">
                                                    <i class="fas fa-shield-halved me-1"></i> Quản trị viên
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-success-subtle text-success border border-success-subtle rounded-pill px-3 py-2 fw-medium">
                                                    <i class="fas fa-user-check me-1"></i> Thành viên
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <div class="col-sm-6">
                                    <label class="form-label fw-semibold text-muted small">Ngày tham gia</label>
                                    <input type="text" class="form-control bg-light text-muted" value="${sessionScope.account.createdDate}" readonly disabled>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-gradient-primary w-100 py-2 rounded-pill shadow-sm">
                                <i class="fas fa-save me-2"></i>Lưu Thay Đổi Hồ Sơ
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        function handleAvatarChange(input) {
            const errorDiv = document.getElementById('avatarError');
            errorDiv.classList.add('d-none');

            if (input.files && input.files[0]) {
                const file = input.files[0];
                const validTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp', 'image/gif'];

                if (!validTypes.includes(file.type)) {
                    errorDiv.textContent = 'Định dạng ảnh không hợp lệ! Vui lòng chọn file JPG, PNG, WEBP.';
                    errorDiv.classList.remove('d-none');
                    input.value = '';
                    return;
                }

                if (file.size > 5 * 1024 * 1024) {
                    errorDiv.textContent = 'Dung lượng ảnh không được vượt quá 5MB!';
                    errorDiv.classList.remove('d-none');
                    input.value = '';
                    return;
                }

                const reader = new FileReader();
                reader.onload = function(e) {
                    const initialDiv = document.getElementById('avatarInitialDiv');
                    const previewImg = document.getElementById('avatarPreviewImg');

                    if (initialDiv) {
                        const newImg = document.createElement('img');
                        newImg.id = 'avatarPreviewImg';
                        newImg.src = e.target.result;
                        newImg.alt = 'Avatar';
                        newImg.className = 'rounded-circle border border-3 border-light shadow-sm object-fit-cover';
                        newImg.style.width = '105px';
                        newImg.style.height = '105px';
                        initialDiv.replaceWith(newImg);
                    } else if (previewImg) {
                        previewImg.src = e.target.result;
                    }
                };
                reader.readAsDataURL(file);
            }
        }

        (() => {
            'use strict';
            const forms = document.querySelectorAll('.needs-validation');
            Array.from(forms).forEach(form => {
                form.addEventListener('submit', event => {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        })();
    </script>
</body>
</html>
