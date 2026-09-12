<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm Người Dùng Mới - Admin</title>
</head>
<body>
    <div class="mb-4">
        <a href="<c:url value='/admin/users'/>" class="btn btn-outline-secondary btn-sm rounded-pill mb-2">
            <i class="fas fa-arrow-left me-1"></i> Quay lại danh sách
        </a>
        <h3 class="fw-bold mb-1"><i class="fas fa-user-plus text-primary me-2"></i>Thêm Người Dùng Mới</h3>
        <p class="text-muted mb-0 small">Điền các thông tin để cấp tài khoản người dùng mới trong hệ thống</p>
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
                    <span class="fw-bold text-dark"><i class="fas fa-file-pen me-2 text-primary"></i>Thông tin tài khoản</span>
                </div>
                <div class="card-body p-4">
                    <form action="<c:url value='/admin/user/insert'/>" method="POST" enctype="multipart/form-data">
                        
                        <div class="row g-3">
                            <!-- Username -->
                            <div class="col-md-6">
                                <label for="username" class="form-label fw-semibold small text-muted">
                                    Tên đăng nhập (Username) <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fas fa-at text-muted"></i></span>
                                    <input type="text" class="form-control" id="username" name="username"
                                           placeholder="vd: nguyenvana" value="${username}" required minlength="3" maxlength="50">
                                </div>
                                <div class="form-text">Tối thiểu 3 ký tự, không chứa khoảng trắng.</div>
                            </div>

                            <!-- Password -->
                            <div class="col-md-6">
                                <label for="password" class="form-label fw-semibold small text-muted">
                                    Mật khẩu <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fas fa-lock text-muted"></i></span>
                                    <input type="password" class="form-control" id="password" name="password"
                                           placeholder="Nhập mật khẩu..." required minlength="6">
                                </div>
                                <div class="form-text">Tối thiểu 6 ký tự.</div>
                            </div>

                            <!-- Fullname -->
                            <div class="col-md-6">
                                <label for="fullname" class="form-label fw-semibold small text-muted">
                                    Họ và tên
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fas fa-user text-muted"></i></span>
                                    <input type="text" class="form-control" id="fullname" name="fullname"
                                           placeholder="vd: Nguyễn Văn A" value="${fullname}">
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
                                           placeholder="vd: nguyenvana@gmail.com" value="${email}" required>
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
                                           placeholder="vd: 0912345678" value="${phone}">
                                </div>
                            </div>

                            <!-- Role -->
                            <div class="col-md-6">
                                <label for="roleid" class="form-label fw-semibold small text-muted">
                                    Vai trò (Phân quyền)
                                </label>
                                <select class="form-select" id="roleid" name="roleid">
                                    <option value="5" ${roleid == '5' ? 'selected' : ''}>Người dùng / Khách hàng (User)</option>
                                    <option value="2" ${roleid == '2' ? 'selected' : ''}>Quản lý (Manager)</option>
                                    <option value="1" ${roleid == '1' ? 'selected' : ''}>Quản trị viên (Admin)</option>
                                </select>
                            </div>

                            <!-- Avatar Upload -->
                            <div class="col-12">
                                <label for="avatar" class="form-label fw-semibold small text-muted">
                                    Ảnh đại diện
                                </label>
                                <input type="file" class="form-control" id="avatar" name="avatar" accept="image/*" onchange="previewImage(this)">
                                <div class="form-text">Hỗ trợ định dạng: JPG, PNG, WEBP, GIF. Kích thước tối đa 10MB.</div>
                                
                                <div class="mt-3 text-center d-none" id="previewContainer">
                                    <img id="avatarPreview" src="#" alt="Xem trước ảnh"
                                         class="rounded-circle border shadow-sm object-fit-cover"
                                         style="width: 100px; height: 100px;">
                                    <p class="text-muted small mt-1">Ảnh xem trước</p>
                                </div>
                            </div>

                            <!-- Active Status -->
                            <div class="col-12">
                                <div class="form-check form-switch mt-2">
                                    <input class="form-check-input" type="checkbox" role="switch" id="isActive" name="isActive" ${isActive != null && isActive ? 'checked' : 'checked'}>
                                    <label class="form-check-label fw-semibold small" for="isActive">
                                        Kích hoạt tài khoản ngay (Người dùng có thể đăng nhập ngay)
                                    </label>
                                </div>
                            </div>
                        </div>

                        <hr class="my-4 text-secondary opacity-25">

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary rounded-pill px-4 shadow-sm">
                                <i class="fas fa-save me-1"></i> Lưu người dùng
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
            <div class="card border-0 shadow-sm rounded-4 bg-primary text-white p-4">
                <h5 class="fw-bold mb-3"><i class="fas fa-circle-info me-2"></i>Lưu ý khi tạo tài khoản</h5>
                <ul class="small ps-3 mb-0" style="line-height: 1.8;">
                    <li><strong>Username</strong> là định danh duy nhất và không thể thay đổi sau khi tạo.</li>
                    <li><strong>Email</strong> được sử dụng để gửi mã OTP khi đăng nhập hoặc quên mật khẩu.</li>
                    <li>Tài khoản có vai trò <strong>Admin</strong> sẽ có toàn quyền truy cập bảng điều khiển và quản lý dữ liệu.</li>
                    <li>Nếu tắt <em>Kích hoạt tài khoản</em>, người dùng sẽ không thể đăng nhập cho đến khi được kích hoạt lại.</li>
                </ul>
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
