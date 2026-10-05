package vn.hcmute.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.hcmute.entity.Category_24133016;
import vn.hcmute.entity.Video_24133016;
import vn.hcmute.service.ICategoryService_24133016;
import vn.hcmute.service.IFavoriteService_24133016;
import vn.hcmute.service.IShareService_24133016;
import vn.hcmute.service.IVideoService_24133016;

@Controller
public class HomeController_24133016 {

    @Autowired
    private ICategoryService_24133016 categoryService;

    @Autowired
    private IVideoService_24133016 videoService;

    @Autowired
    private IShareService_24133016 shareService;

    @Autowired
    private IFavoriteService_24133016 favoriteService;

    @GetMapping({"/", "/home"})
    public String home(Model model,
                       @RequestParam(value = "catPage", required = false) Integer catPage,
                       @RequestParam(value = "catId", required = false) Integer catId) {

        List<Category_24133016> categories = categoryService.findAll();
        int videosPerPage = 3; // Câu 4: phân trang 3 video/trang

        // Xây dựng dữ liệu cho từng category
        List<Map<String, Object>> categoryDataList = new ArrayList<>();
        for (Category_24133016 cat : categories) {
            Map<String, Object> catData = new HashMap<>();
            catData.put("category", cat);

            // Đếm số lượng video (Câu 5)
            long videoCount = videoService.countByCategoryId(cat.getCategoryId());
            catData.put("videoCount", videoCount);

            int totalPages = videoCount == 0 ? 0
                    : (int) Math.min(Integer.MAX_VALUE, ((videoCount - 1) / videosPerPage) + 1);

            // Tính trang hiện tại cho category này
            int currentPage = 1;
            if (catId != null && catId == cat.getCategoryId() && catPage != null) {
                currentPage = Math.max(1, catPage);
            }
            if (totalPages > 0) {
                currentPage = Math.min(currentPage, totalPages);
            }

            Page<Video_24133016> videoPage = videoService.findByCategoryId(
                cat.getCategoryId(), PageRequest.of(currentPage - 1, videosPerPage,
                    Sort.by(Sort.Direction.ASC, "videoId")));

            catData.put("videos", videoPage.getContent());
            catData.put("currentPage", currentPage);
            catData.put("totalPages", totalPages);

            // Tính share count và like count cho mỗi video
            Map<String, Long> shareCountMap = new HashMap<>();
            Map<String, Long> likeCountMap = new HashMap<>();
            for (Video_24133016 v : videoPage.getContent()) {
                shareCountMap.put(v.getVideoId(), shareService.countByVideoId(v.getVideoId()));
                likeCountMap.put(v.getVideoId(), favoriteService.countByVideoId(v.getVideoId()));
            }
            catData.put("shareCountMap", shareCountMap);
            catData.put("likeCountMap", likeCountMap);

            categoryDataList.add(catData);
        }

        model.addAttribute("categoryDataList", categoryDataList);
        return "home";
    }
}
