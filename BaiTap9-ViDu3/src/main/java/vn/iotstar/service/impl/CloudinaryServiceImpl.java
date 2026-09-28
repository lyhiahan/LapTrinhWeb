package vn.iotstar.service.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.service.CloudinaryService;
import vn.iotstar.service.CloudinaryUploadResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Override
    public CloudinaryUploadResult upload(MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new IllegalArgumentException("Chưa chọn ảnh");
        String type = file.getContentType();
        if (type == null || !type.startsWith("image/"))
            throw new IllegalArgumentException("Chỉ cho phép file hình ảnh");
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                Map.of("folder", "shop/products")
            );
            return new CloudinaryUploadResult(
                String.valueOf(result.get("secure_url")),
                String.valueOf(result.get("public_id"))
            );
        } catch (Exception e) {
            log.warn("Cloudinary upload error ({}), using local fallback storage...", e.getMessage());
            return saveLocal(file);
        }
    }

    private CloudinaryUploadResult saveLocal(MultipartFile file) {
        try {
            Path uploadDir = Paths.get("uploads", "products").toAbsolutePath().normalize();
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }
            String filename = UUID.randomUUID() + ext;
            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return new CloudinaryUploadResult("/uploads/products/" + filename, "local_" + filename);
        } catch (IOException ex) {
            throw new IllegalStateException("Lưu ảnh thất bại: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank()) return;
        if (publicId.startsWith("local_")) {
            try {
                String filename = publicId.substring("local_".length());
                Path target = Paths.get("uploads", "products").resolve(filename);
                Files.deleteIfExists(target);
            } catch (Exception ex) {
                log.warn("Lỗi khi xóa file local: {}", ex.getMessage());
            }
            return;
        }
        try {
            cloudinary.uploader().destroy(
                publicId,
                Map.of("resource_type", "image")
            );
        } catch (Exception e) {
            log.warn("Lỗi khi xóa ảnh Cloudinary: {}", e.getMessage());
        }
    }
}
