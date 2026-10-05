package vn.hcmute.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

        Page<Video_24133016> videoPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            videoPage = videoService.search(keyword.trim(), PageRequest.of(page - 1, PAGE_SIZE));
            model.addAttribute("keyword", keyword.trim());
        } else {
            videoPage = videoService.findAll(PageRequest.of(page - 1, PAGE_SIZE));
        }

        model.addAttribute("videos", videoPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", videoPage.getTotalPages());
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
    public String doAdd(@RequestParam("videoId") String videoId,
                        @RequestParam("title") String title,
                        @RequestParam("description") String description,
                        @RequestParam("views") int views,
                        @RequestParam("price") BigDecimal price,
                        @RequestParam("stock") int stock,
                        @RequestParam("categoryId") int categoryId,
                        @RequestParam(value = "active", defaultValue = "false") boolean active,
                        @RequestParam(value = "posterFile", required = false) MultipartFile posterFile,
                        HttpSession session, RedirectAttributes ra) {
        if (!checkAdmin(session)) return "redirect:/login";

        Video_24133016 video = new Video_24133016();
        video.setVideoId(videoId.trim());
        video.setTitle(title.trim());
        video.setDescription(description != null ? description.trim() : "");
        video.setViews(views);
        video.setActive(active);
        video.setPrice(price.max(BigDecimal.ZERO));
        video.setStock(Math.max(0, stock));

        Category_24133016 category = categoryService.findById(categoryId);
        video.setCategory(category);

        // Xử lý upload poster
        if (posterFile != null && !posterFile.isEmpty()) {
            String fileName = saveUploadFile(posterFile);
            video.setPoster(fileName);
        }

        videoService.insert(video);
        ra.addFlashAttribute("message", "Thêm video mới thành công!");
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
                         @RequestParam("title") String title,
                         @RequestParam("description") String description,
                         @RequestParam("views") int views,
                         @RequestParam("price") BigDecimal price,
                         @RequestParam("stock") int stock,
                         @RequestParam("categoryId") int categoryId,
                         @RequestParam(value = "active", defaultValue = "false") boolean active,
                         @RequestParam(value = "posterFile", required = false) MultipartFile posterFile,
                         HttpSession session, RedirectAttributes ra) {
        if (!checkAdmin(session)) return "redirect:/login";

        Video_24133016 video = videoService.findById(videoId);
        if (video == null) {
            ra.addFlashAttribute("error", "Không tìm thấy video!");
            return "redirect:/admin/videos";
        }

        video.setTitle(title.trim());
        video.setDescription(description != null ? description.trim() : "");
        video.setViews(views);
        video.setActive(active);
        video.setPrice(price.max(BigDecimal.ZERO));
        video.setStock(Math.max(0, stock));

        Category_24133016 category = categoryService.findById(categoryId);
        video.setCategory(category);

        // Upload poster mới nếu có
        if (posterFile != null && !posterFile.isEmpty()) {
            if (video.getPoster() != null && !video.getPoster().startsWith("http")) {
                File oldFile = new File("upload/" + video.getPoster());
                if (oldFile.exists()) oldFile.delete();
            }
            String fileName = saveUploadFile(posterFile);
            video.setPoster(fileName);
        }

        videoService.update(video);
        ra.addFlashAttribute("message", "Cập nhật video thành công!");
        return "redirect:/admin/videos";
    }

    // Xóa video
    @GetMapping("/delete/{videoId}")
    public String doDelete(@PathVariable("videoId") String videoId, HttpSession session, RedirectAttributes ra) {
        if (!checkAdmin(session)) return "redirect:/login";
        try {
            Video_24133016 video = videoService.findById(videoId);
            if (video != null && video.getPoster() != null && !video.getPoster().isEmpty()) {
                File posterFile = new File("upload/" + video.getPoster());
                if (posterFile.exists()) posterFile.delete();
            }
            videoService.delete(videoId);
            ra.addFlashAttribute("message", "Xóa video [" + videoId + "] thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Lỗi xóa video: " + e.getMessage());
        }
        return "redirect:/admin/videos";
    }

    // Upload file helper: phân loại lưu vào poster hoặc video
    private String saveUploadFile(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String extension = originalName != null && originalName.contains(".")
                    ? originalName.substring(originalName.lastIndexOf(".")).toLowerCase()
                    : ".jpg";

            boolean isVideo = extension.equals(".mp4") || extension.equals(".webm") || extension.equals(".mkv")
                    || extension.equals(".avi") || extension.equals(".mov")
                    || (file.getContentType() != null && file.getContentType().startsWith("video/"));

            String subFolder = isVideo ? "video" : "poster";
            String fileName = UUID.randomUUID().toString() + extension;

            // 1. Lưu vào thư mục upload/poster hoặc upload/video
            File dir1 = new File("upload/" + subFolder);
            if (!dir1.exists()) dir1.mkdirs();
            Path path1 = Paths.get("upload", subFolder, fileName);
            Files.write(path1, file.getBytes());

            // 2. Lưu vào thư mục poster hoặc video trực tiếp tại gốc dự án
            try {
                File dir2 = new File(subFolder);
                if (!dir2.exists()) dir2.mkdirs();
                Path path2 = Paths.get(subFolder, fileName);
                Files.write(path2, file.getBytes());
            } catch (Exception ignored) {}

            // 3. Lưu vào thư mục src/main/webapp/upload/poster hoặc upload/video
            try {
                File dir3 = new File("src/main/webapp/upload/" + subFolder);
                if (!dir3.exists()) dir3.mkdirs();
                Path path3 = Paths.get("src", "main", "webapp", "upload", subFolder, fileName);
                Files.write(path3, file.getBytes());
            } catch (Exception ignored) {}

            return subFolder + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
