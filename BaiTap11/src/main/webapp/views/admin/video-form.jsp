<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
    <title>${isEdit ? 'Cập nhật' : 'Thêm mới'} Video</title>
    <style>
        .form-card { background: #fff; border-radius: 15px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); padding: 30px; }
        .form-card h3 { font-weight: 700; color: #333; margin-bottom: 25px; }
        .preview-img { max-width: 200px; max-height: 150px; border-radius: 8px; margin-top: 10px; border: 2px solid #ddd; }
    </style>
</head>
<body>
    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/admin/videos" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left"></i> Quay lại danh sách
        </a>
    </div>

    <div class="form-card">
        <h3>
            <i class="fas ${isEdit ? 'fa-edit' : 'fa-plus-circle'}"></i>
            ${isEdit ? 'Cập nhật Video' : 'Thêm Video mới'}
        </h3>

        <form action="${pageContext.request.contextPath}/admin/videos/${isEdit ? 'edit' : 'add'}"
              method="post" enctype="multipart/form-data">

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label"><strong>Mã Video *</strong></label>
                    <input type="text" class="form-control" name="videoId" value="${video.videoId}"
                           ${isEdit ? 'readonly' : ''} required placeholder="VD: VD001">
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label"><strong>Tiêu đề *</strong></label>
                    <input type="text" class="form-control" name="title" value="${video.title}" required placeholder="Nhập tiêu đề video">
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label"><strong>Giá bán (VNĐ) *</strong></label>
                    <input type="number" class="form-control" name="price" value="${video.price != null ? video.price : 0}" min="0" step="1000" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label"><strong>Số lượng tồn kho *</strong></label>
                    <input type="number" class="form-control" name="stock" value="${video.stock != null ? video.stock : 0}" min="0" required>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label"><strong>Category *</strong></label>
                    <select class="form-select" name="categoryId" required>
                        <option value="">-- Chọn Category --</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.categoryId}"
                                ${video.category != null && video.category.categoryId == cat.categoryId ? 'selected' : ''}>
                                ${cat.categoryname}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label"><strong>Lượt xem</strong></label>
                    <input type="number" class="form-control" name="views" value="${video.views != null ? video.views : 0}" min="0">
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label"><strong>Poster / Video File</strong></label>
                    <input type="file" class="form-control" name="posterFile" accept="image/*,video/*" onchange="previewMedia(this)">


                    <div id="previewContainer" class="mt-2">
                        <c:if test="${isEdit && not empty video.poster}">
                            <p class="text-muted mt-1 mb-1"><small>File hiện tại: <strong>${video.poster}</strong></small></p>
                            <c:choose>
                                <c:when test="${video.poster.endsWith('.mp4') || video.poster.endsWith('.webm') || video.poster.contains('/video/')}">
                                    <video controls class="preview-img" style="max-height: 200px; width: 100%; border-radius: 8px;">
                                        <source src="${pageContext.request.contextPath}/upload/${video.poster}" type="video/mp4">
                                    </video>
                                </c:when>
                                <c:when test="${video.poster.startsWith('http://') || video.poster.startsWith('https://')}">
                                    <img src="${video.poster}" class="preview-img" id="imgPreview">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/upload/${video.poster}" class="preview-img" id="imgPreview">
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <c:if test="${!isEdit}">
                            <img src="" class="preview-img d-none" id="imgPreview">
                            <video controls class="preview-img d-none" id="videoPreview" style="max-height: 200px; width: 100%; border-radius: 8px;"></video>
                        </c:if>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label"><strong>Trạng thái</strong></label>
                    <div class="form-check form-switch mt-2">
                        <input class="form-check-input" type="checkbox" name="active" value="true"
                            ${video.active == null || video.active ? 'checked' : ''}>
                        <label class="form-check-label">Active</label>
                    </div>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label"><strong>Mô tả</strong></label>
                <textarea class="form-control" name="description" rows="4" placeholder="Nhập mô tả video">${video.description}</textarea>
            </div>

            <div class="text-end">
                <a href="${pageContext.request.contextPath}/admin/videos" class="btn btn-secondary me-2">
                    <i class="fas fa-times"></i> Hủy
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> ${isEdit ? 'Cập nhật' : 'Thêm mới'}
                </button>
            </div>
        </form>
    </div>

    <script>
        function previewMedia(input) {
            var imgPreview = document.getElementById('imgPreview');
            var videoPreview = document.getElementById('videoPreview');
            if (input.files && input.files[0]) {
                var file = input.files[0];
                var isVideo = file.type.startsWith('video') || file.name.match(/\.(mp4|webm|avi|mkv|mov)$/i);
                var url = URL.createObjectURL(file);

                if (isVideo) {
                    if (imgPreview) imgPreview.classList.add('d-none');
                    if (!videoPreview) {
                        videoPreview = document.createElement('video');
                        videoPreview.id = 'videoPreview';
                        videoPreview.controls = true;
                        videoPreview.className = 'preview-img';
                        videoPreview.style = 'max-height: 200px; width: 100%; border-radius: 8px;';
                        document.getElementById('previewContainer').appendChild(videoPreview);
                    }
                    videoPreview.src = url;
                    videoPreview.classList.remove('d-none');
                } else {
                    if (videoPreview) videoPreview.classList.add('d-none');
                    if (!imgPreview) {
                        imgPreview = document.createElement('img');
                        imgPreview.id = 'imgPreview';
                        imgPreview.className = 'preview-img';
                        document.getElementById('previewContainer').appendChild(imgPreview);
                    }
                    imgPreview.src = url;
                    imgPreview.classList.remove('d-none');
                }
            }
        }
    </script>
</body>
</html>
