package vn.hcmute.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import vn.hcmute.entity.Category_24133016;
import vn.hcmute.entity.User_24133016;
import vn.hcmute.entity.Video_24133016;
import vn.hcmute.service.ICategoryService_24133016;
import vn.hcmute.service.IVideoService_24133016;

@Controller
@RequestMapping("/admin/videos")
public class AdminVideoController_24133016 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminVideoController_24133016.class);
    private static final int MAX_TITLE_LENGTH = 200;
    private static final int MAX_DESCRIPTION_LENGTH = 500;
    private static final int MAX_STOCK = 1_000_000;
    private static final long MAX_UPLOAD_SIZE = 50L * 1024 * 1024;
    private static final BigDecimal MAX_PRICE = new BigDecimal("9999999999999999.99");
    private static final Set<String> IMAGE_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp");
    private static final Set<String> VIDEO_EXTENSIONS = Set.of(".mp4", ".webm", ".mkv", ".avi", ".mov");
    private static final List<Path> UPLOAD_ROOTS = List.of(
            Paths.get("upload"),
            Paths.get("."),
            Paths.get("src", "main", "webapp", "upload"));

    @Autowired
    private IVideoService_24133016 videoService;

    @Autowired
    private ICategoryService_24133016 categoryService;

    private static final int PAGE_SIZE = 6; // Câu 2: phân trang 6 video trên 01 trang

    // Kiểm tra quyền admin
    private boolean checkAdmin(HttpSession session) {
        User_24133016 user = (User_24133016) session.getAttribute("account");
        return user != null && user.getAdmin() != null && user.getAdmin();
    }

    // Danh sách video (có phân trang)
    @GetMapping
    public String listVideos(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             HttpSession session, Model model) {
        if (!checkAdmin(session)) return "redirect:/login";

        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE,
                Sort.by(Sort.Direction.ASC, "videoId"));

        // Đọc trang đầu trước để biết giới hạn hợp lệ. Cách này cũng tránh lỗi
        // offset tràn số khi URL chứa một page rất lớn.
        Page<Video_24133016> videoPage;
        if (!normalizedKeyword.isEmpty()) {
            videoPage = videoService.search(normalizedKeyword, pageRequest);
            model.addAttribute("keyword", normalizedKeyword);
        } else {
            videoPage = videoService.findAll(pageRequest);
        }

        // Trang vượt quá giới hạn được đưa về trang cuối; danh sách rỗng dùng trang 1.
        int totalPages = videoPage.getTotalPages();
        int currentPage = totalPages == 0 ? 1 : Math.min(Math.max(1, page), totalPages);
        if (currentPage > 1) {
            pageRequest = PageRequest.of(currentPage - 1, PAGE_SIZE,
                    Sort.by(Sort.Direction.ASC, "videoId"));
            if (!normalizedKeyword.isEmpty()) {
                videoPage = videoService.search(normalizedKeyword, pageRequest);
            } else {
                videoPage = videoService.findAll(pageRequest);
            }
        }

        model.addAttribute("videos", videoPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", videoPage.getTotalElements());

        return "admin/video-list";
    }

    // Form thêm mới video
    @GetMapping("/add")
    public String addForm(HttpSession session, Model model) {
        if (!checkAdmin(session)) return "redirect:/login";

        List<Category_24133016> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("video", new Video_24133016());
        model.addAttribute("isEdit", false);

        return "admin/video-form";
    }

    // Xử lý thêm mới video
    @PostMapping("/add")
    public String doAdd(@RequestParam(value = "title", required = false) String title,
                        @RequestParam(value = "description", required = false) String description,
                        @RequestParam(value = "views", defaultValue = "0") String views,
                        @RequestParam(value = "price", required = false) String price,
                        @RequestParam(value = "stock", required = false) String stock,
                        @RequestParam(value = "categoryId", required = false) String categoryId,
                        @RequestParam(value = "active", defaultValue = "false") boolean active,
                        @RequestParam(value = "posterFile", required = false) MultipartFile posterFile,
                        HttpSession session, RedirectAttributes ra) {
        if (!checkAdmin(session)) return "redirect:/login";

        String uploadedFile = null;
        try {
            VideoFormData form = validateForm(title, description, views, price, stock, categoryId);
            validateUploadFile(posterFile);

            Video_24133016 video = new Video_24133016();
            // VideoId để trống: service tự sinh mã tăng dần và dùng persist,
            // do đó request thêm mới không thể ghi đè bản ghi cũ.
            video.setTitle(form.title());
            video.setDescription(form.description());
            video.setViews(form.views());
            video.setActive(active);
            video.setPrice(form.price());
            video.setStock(form.stock());
            video.setCategory(form.category());

            if (posterFile != null && !posterFile.isEmpty()) {
                uploadedFile = saveUploadFile(posterFile);
                video.setPoster(uploadedFile);
            }

            videoService.insert(video);
            ra.addFlashAttribute("message", "Thêm video [" + video.getVideoId() + "] thành công!");
        } catch (IllegalArgumentException e) {
            deleteUploadFile(uploadedFile);
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/videos/add";
        } catch (Exception e) {
            deleteUploadFile(uploadedFile);
            LOGGER.error("Không thể thêm video", e);
            ra.addFlashAttribute("error", "Không thể thêm video. Vui lòng kiểm tra dữ liệu và thử lại.");
            return "redirect:/admin/videos/add";
        }
        return "redirect:/admin/videos";
    }

    // Form chỉnh sửa video
    @GetMapping("/edit/{videoId}")
    public String editForm(@PathVariable("videoId") String videoId,
                           HttpSession session, Model model) {
        if (!checkAdmin(session)) return "redirect:/login";

        Video_24133016 video = videoService.findById(videoId);
        if (video == null) return "redirect:/admin/videos";

        List<Category_24133016> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("video", video);
        model.addAttribute("isEdit", true);

        return "admin/video-form";
    }

    // Xử lý cập nhật video
    @PostMapping("/edit")
    public String doEdit(@RequestParam("videoId") String videoId,
                         @RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "description", required = false) String description,
                         @RequestParam(value = "views", defaultValue = "0") String views,
                         @RequestParam(value = "price", required = false) String price,
                         @RequestParam(value = "stock", required = false) String stock,
                         @RequestParam(value = "categoryId", required = false) String categoryId,
                         @RequestParam(value = "active", defaultValue = "false") boolean active,
                         @RequestParam(value = "posterFile", required = false) MultipartFile posterFile,
                         HttpSession session, RedirectAttributes ra) {
        if (!checkAdmin(session)) return "redirect:/login";

        if (videoId == null || videoId.isBlank() || videoId.length() > 50) {
            ra.addFlashAttribute("error", "Mã video không hợp lệ.");
            return "redirect:/admin/videos";
        }

        Video_24133016 video = videoService.findById(videoId);
        if (video == null) {
            ra.addFlashAttribute("error", "Không tìm thấy video!");
            return "redirect:/admin/videos";
        }

        String oldPoster = video.getPoster();
        String uploadedFile = null;
        try {
            VideoFormData form = validateForm(title, description, views, price, stock, categoryId);
            validateUploadFile(posterFile);

            if (posterFile != null && !posterFile.isEmpty()) {
                uploadedFile = saveUploadFile(posterFile);
                video.setPoster(uploadedFile);
            }

            video.setTitle(form.title());
            video.setDescription(form.description());
            video.setViews(form.views());
            video.setActive(active);
            video.setPrice(form.price());
            video.setStock(form.stock());
            video.setCategory(form.category());

            videoService.update(video);

            // Chỉ xóa file cũ sau khi transaction cập nhật database đã thành công.
            if (uploadedFile != null && !uploadedFile.equals(oldPoster)) {
                deleteUploadFile(oldPoster);
            }
            ra.addFlashAttribute("message", "Cập nhật video thành công!");
        } catch (IllegalArgumentException e) {
            deleteUploadFile(uploadedFile);
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/videos/edit/" + videoId;
        } catch (Exception e) {
            deleteUploadFile(uploadedFile);
            LOGGER.error("Không thể cập nhật video {}", videoId, e);
            ra.addFlashAttribute("error", "Không thể cập nhật video. Vui lòng thử lại.");
            return "redirect:/admin/videos/edit/" + videoId;
        }
        return "redirect:/admin/videos";
    }

    // Xóa video
    @PostMapping("/delete/{videoId}")
    public String doDelete(@PathVariable("videoId") String videoId, HttpSession session, RedirectAttributes ra) {
        if (!checkAdmin(session)) return "redirect:/login";
        try {
            Video_24133016 video = videoService.findById(videoId);
            if (video == null) {
                ra.addFlashAttribute("error", "Không tìm thấy video [" + videoId + "].");
                return "redirect:/admin/videos";
            }

            String poster = video.getPoster();
            videoService.delete(videoId);

            // Service đã xóa Favorites, Shares và Video trong một transaction.
            // File chỉ được xóa sau khi thao tác database hoàn tất thành công.
            deleteUploadFile(poster);
            ra.addFlashAttribute("message", "Xóa video [" + videoId + "] thành công!");
        } catch (Exception e) {
            LOGGER.error("Không thể xóa video {}", videoId, e);
            ra.addFlashAttribute("error", "Không thể xóa video [" + videoId + "]. Dữ liệu chưa bị thay đổi.");
        }
        return "redirect:/admin/videos";
    }

    private VideoFormData validateForm(String title, String description, String views,
                                       String price, String stock, String categoryId) {
        String normalizedTitle = title == null ? "" : title.trim();
        if (normalizedTitle.isEmpty()) {
            throw new IllegalArgumentException("Tiêu đề video không được để trống.");
        }
        if (normalizedTitle.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Tiêu đề không được vượt quá " + MAX_TITLE_LENGTH + " ký tự.");
        }

        String normalizedDescription = description == null ? "" : description.trim();
        if (normalizedDescription.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Mô tả không được vượt quá " + MAX_DESCRIPTION_LENGTH + " ký tự.");
        }

        int normalizedViews = parseNonNegativeInteger(views, "Lượt xem", Integer.MAX_VALUE);
        int normalizedStock = parseNonNegativeInteger(stock, "Tồn kho", MAX_STOCK);
        BigDecimal normalizedPrice = parsePrice(price);
        int normalizedCategoryId = parsePositiveInteger(categoryId, "Danh mục");

        Category_24133016 category = categoryService.findById(normalizedCategoryId);
        if (category == null) {
            throw new IllegalArgumentException("Danh mục đã chọn không tồn tại.");
        }

        return new VideoFormData(normalizedTitle, normalizedDescription, normalizedViews,
                normalizedPrice, normalizedStock, category);
    }

    private int parseNonNegativeInteger(String value, String fieldName, int maximum) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " không được để trống.");
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed < 0 || parsed > maximum) {
                throw new IllegalArgumentException(fieldName + " phải từ 0 đến " + maximum + ".");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " phải là số nguyên hợp lệ.");
        }
    }

    private int parsePositiveInteger(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Vui lòng chọn " + fieldName.toLowerCase(Locale.ROOT) + ".");
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed <= 0) {
                throw new IllegalArgumentException(fieldName + " không hợp lệ.");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " không hợp lệ.");
        }
    }

    private BigDecimal parsePrice(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Giá bán không được để trống.");
        }
        try {
            BigDecimal parsed = new BigDecimal(value.trim());
            if (parsed.compareTo(BigDecimal.ZERO) < 0 || parsed.compareTo(MAX_PRICE) > 0) {
                throw new IllegalArgumentException("Giá bán phải từ 0 đến " + MAX_PRICE.toPlainString() + ".");
            }
            if (parsed.stripTrailingZeros().scale() > 2) {
                throw new IllegalArgumentException("Giá bán chỉ được có tối đa 2 chữ số thập phân.");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá bán phải là một số hợp lệ.");
        }
    }

    private void validateUploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return;
        if (file.getSize() > MAX_UPLOAD_SIZE) {
            throw new IllegalArgumentException("Tệp tải lên không được vượt quá 50 MB.");
        }
        String extension = getExtension(file.getOriginalFilename());
        if (!IMAGE_EXTENSIONS.contains(extension) && !VIDEO_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Chỉ chấp nhận tệp ảnh JPG, PNG, GIF, WEBP hoặc video MP4, WEBM, MKV, AVI, MOV.");
        }
    }

    // Phân loại và lưu file với tên UUID; đường dẫn trả về luôn là poster/... hoặc video/...
    private String saveUploadFile(MultipartFile file) throws IOException {
        String extension = getExtension(file.getOriginalFilename());
        String subFolder = VIDEO_EXTENSIONS.contains(extension) ? "video" : "poster";
        String relativeName = subFolder + "/" + UUID.randomUUID() + extension;
        byte[] content = file.getBytes();

        // Bản trong upload/ là bản chính được WebMvc phục vụ.
        writeUploadCopy(UPLOAD_ROOTS.get(0), relativeName, content);

        // Giữ hai bản tương thích với cấu trúc cũ. Nếu việc sao chép phụ thất bại,
        // bản chính vẫn đủ để ứng dụng hiển thị media.
        for (int i = 1; i < UPLOAD_ROOTS.size(); i++) {
            try {
                writeUploadCopy(UPLOAD_ROOTS.get(i), relativeName, content);
            } catch (IOException e) {
                LOGGER.warn("Không thể tạo bản sao upload tại {}", UPLOAD_ROOTS.get(i), e);
            }
        }
        return relativeName;
    }

    private void writeUploadCopy(Path root, String relativeName, byte[] content) throws IOException {
        Path rootPath = root.toAbsolutePath().normalize();
        Path target = rootPath.resolve(relativeName).normalize();
        if (!target.startsWith(rootPath)) {
            throw new IOException("Đường dẫn upload không hợp lệ.");
        }
        Files.createDirectories(target.getParent());
        Files.write(target, content);
    }

    // Xóa đủ ba bản do saveUploadFile tạo. Chỉ nhận đường dẫn nội bộ thuộc poster/ hoặc video/.
    private void deleteUploadFile(String storedPath) {
        if (storedPath == null || storedPath.isBlank()) return;

        String normalized = storedPath.trim().replace('\\', '/');
        String lower = normalized.toLowerCase(Locale.ROOT);
        if (lower.startsWith("http://") || lower.startsWith("https://")
                || (!lower.startsWith("poster/") && !lower.startsWith("video/"))) {
            return;
        }

        for (Path root : UPLOAD_ROOTS) {
            Path rootPath = root.toAbsolutePath().normalize();
            Path target = rootPath.resolve(normalized).normalize();
            if (!target.startsWith(rootPath)) {
                LOGGER.warn("Bỏ qua đường dẫn xóa file không an toàn: {}", storedPath);
                continue;
            }
            try {
                Files.deleteIfExists(target);
            } catch (IOException e) {
                // Database đã cập nhật thành công; lỗi dọn file không được biến thành lỗi nghiệp vụ.
                LOGGER.warn("Không thể xóa file upload {}", target, e);
            }
        }
    }

    private String getExtension(String originalName) {
        if (originalName == null || originalName.isBlank()) return "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == originalName.length() - 1) return "";
        return originalName.substring(dotIndex).toLowerCase(Locale.ROOT);
    }

    private record VideoFormData(String title, String description, int views,
                                 BigDecimal price, int stock, Category_24133016 category) {}
}
