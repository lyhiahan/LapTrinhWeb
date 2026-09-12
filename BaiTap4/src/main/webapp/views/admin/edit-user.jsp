<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chỉnh Sửa Người Dùng - Admin</title>
</head>
<body>
    <div class="mb-4">
        <a href="<c:url value='/admin/users'/>" class="btn btn-outline-secondary btn-sm rounded-pill mb-2">
            <i class="fas fa-arrow-left me-1"></i> Quay lại danh sách
        </a>
        <h3 class="fw-bold mb-1"><i class="fas fa-user-pen text-primary me-2"></i>Chỉnh Sửa Người Dùng</h3>
        <p class="text-muted mb-0 small">Cập nhật thông tin tài khoản, phân quyền hoặc đổi mật khẩu cho người dùng</p>
    </div>

    <!-- Thông báo lỗi nếu có -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show rounded-3 shadow-sm" role="alert">
            <i class="fas fa-triangle-exclamation me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row">
        <div class="col-12 col-lg-8">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-header bg-white py-3 border-0 rounded-top-4">
                    <span class="fw-bold text-dark"><i class="fas fa-id-badge me-2 text-primary"></i>Thông tin tài khoản: <span class="text-primary">${user.userName}</span></span>
                </div>
                <div class="card-body p-4">
                    <form action="<c:url value='/admin/user/update'/>" method="POST" enctype="multipart/form-data">
                        <input type="hidden" name="id" value="${user.id}">

                        <div class="row g-3">
                            <!-- Username (Read-only) -->
                            <div class="col-md-6">
                                <label class="form-label fw-semibold small text-muted">
                                    Tên đăng nhập (Username)
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fas fa-at text-muted"></i></span>
                                    <input type="text" class="form-control bg-light" value="${user.userName}" readonly disabled>
                                </div>
                                <div class="form-text">Tên đăng nhập cố định không thể thay đổi.</div>
                            </div>

                            <!-- Fullname -->
                            <div class="col-md-6">
                                <label for="fullname" class="form-label fw-semibold small text-muted">
                                    Họ và tên
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fas fa-user text-muted"></i></span>
                                    <input type="text" class="form-control" id="fullname" name="fullname"
                                           placeholder="vd: Nguyễn Văn A" value="${user.fullName}">
                                </div>
                            </div>

                            <!-- Email -->
                            <div class="col-md-6">
                                <label for="email" class="form-label fw-semibold small text-muted">
                                    Địa chỉ Email <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fas fa-envelope text-muted"></i></span>
                                    <input type="email" class="form-control" id="email" name="email"
                                           placeholder="vd: nguyenvana@gmail.com" value="${user.email}" required>
                                </div>
                            </div>

                            <!-- Phone -->
                            <div class="col-md-6">
                                <label for="phone" class="form-label fw-semibold small text-muted">
                                    Số điện thoại
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fas fa-phone text-muted"></i></span>
                                    <input type="tel" class="form-control" id="phone" name="phone"
                                           placeholder="vd: 0912345678" value="${user.phone}">
                                </div>
                            </div>

                            <!-- Role -->
                            <div class="col-md-6">
                                <label for="roleid" class="form-label fw-semibold small text-muted">
                                    Vai trò (Phân quyền)
                                </label>
                                <select class="form-select" id="roleid" name="roleid">
                                    <option value="5" ${user.roleid == 5 ? 'selected' : ''}>Người dùng / Khách hàng (User)</option>
                                    <option value="2" ${user.roleid == 2 ? 'selected' : ''}>Quản lý (Manager)</option>
                                    <option value="1" ${user.roleid == 1 ? 'selected' : ''}>Quản trị viên (Admin)</option>
                                </select>
                            </div>

                            <!-- New Password (Optional) -->
                            <div class="col-md-6">
                                <label for="password" class="form-label fw-semibold small text-muted">
                                    Mật khẩu mới (Tùy chọn)
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fas fa-key text-muted"></i></span>
                                    <input type="password" class="form-control" id="password" name="password"
                                           placeholder="Để trống nếu không muốn đổi mật khẩu" minlength="6">
                                </div>
                                <div class="form-text">Chỉ nhập nếu bạn muốn thay đổi mật khẩu người dùng.</div>
                            </div>

                            <!-- Avatar Upload -->
                            <div class="col-12">
                                <label for="avatar" class="form-label fw-semibold small text-muted">
                                    Ảnh đại diện
                                </label>
                                <div class="d-flex align-items-center gap-4 mb-3">
                                    <!-- Current Avatar -->
                                    <div>
                                        <span class="d-block small text-muted mb-1">Ảnh hiện tại:</span>
                                        <c:choose>
                                            <c:when test="${not empty user.avatar}">
                                                <img src="<c:url value='/image?fname=${user.avatar}'/>" alt="${user.userName}"
                                                     class="rounded-circle border shadow-sm object-fit-cover"
                                                     style="width: 72px; height: 72px;"
                                                     onerror="this.src='https://ui-avatars.com/api/?name=${user.userName}&background=random'"/>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="rounded-circle d-flex align-items-center justify-content-center text-white fw-bold shadow-sm"
                                                     style="width: 72px; height: 72px; background: linear-gradient(135deg, #6366f1, #8b5cf6); font-size: 1.5rem;">
                                                    ${fn:toUpperCase(fn:substring(user.userName, 0, 1))}
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="flex-grow-1">
                                        <input type="file" class="form-control" id="avatar" name="avatar" accept="image/*" onchange="previewImage(this)">
                                        <div class="form-text">Chọn ảnh mới để thay thế ảnh hiện tại (JPG, PNG, WEBP, GIF).</div>
                                    </div>
                                </div>

                                <div class="text-center d-none" id="previewContainer">
                                    <img id="avatarPreview" src="#" alt="Xem trước ảnh mới"
                                         class="rounded-circle border shadow-sm object-fit-cover"
                                         style="width: 80px; height: 80px;">
                                    <p class="text-muted small mt-1">Ảnh mới đã chọn</p>
                                </div>
                            </div>

                            <!-- Active Status -->
                            <div class="col-12">
                                <div class="form-check form-switch mt-2">
                                    <input class="form-check-input" type="checkbox" role="switch" id="isActive" name="isActive" ${user.isActive ? 'checked' : ''}>
                                    <label class="form-check-label fw-semibold small" for="isActive">
                                        Trạng thái hoạt động (Kích hoạt cho phép đăng nhập)
                                    </label>
                                </div>
                            </div>
                        </div>

                        <hr class="my-4 text-secondary opacity-25">

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary rounded-pill px-4 shadow-sm">
                                <i class="fas fa-save me-1"></i> Lưu thay đổi
                            </button>
                            <a href="<c:url value='/admin/users'/>" class="btn btn-light border rounded-pill px-4">
                                Hủy bỏ
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-12 col-lg-4 mt-4 mt-lg-0">
            <div class="card border-0 shadow-sm rounded-4 mb-4">
                <div class="card-header bg-white py-3 border-0 rounded-top-4">
                    <span class="fw-bold text-dark"><i class="fas fa-info-circle me-2 text-primary"></i>Thông tin bổ sung</span>
                </div>
                <div class="card-body p-3">
                    <ul class="list-group list-group-flush small">
                        <li class="list-group-item d-flex justify-content-between px-0">
                            <span class="text-muted">Mã ID:</span>
                            <span class="fw-bold">#USR-${user.id}</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between px-0">
                            <span class="text-muted">Ngày tạo tài khoản:</span>
                            <span>${not empty user.createdDate ? user.createdDate : 'N/A'}</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between px-0">
                            <span class="text-muted">Trạng thái:</span>
                            <c:choose>
                                <c:when test="${user.isActive}">
                                    <span class="badge bg-success-subtle text-success">Hoạt động</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger-subtle text-danger">Đang khóa</span>
                                </c:otherwise>
                            </c:choose>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <script>
        function previewImage(input) {
            const previewContainer = document.getElementById('previewContainer');
            const preview = document.getElementById('avatarPreview');
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    preview.src = e.target.result;
                    previewContainer.classList.remove('d-none');
                }
                reader.readAsDataURL(input.files[0]);
            } else {
                previewContainer.classList.add('d-none');
            }
        }
    </script>
</body>
</html>
