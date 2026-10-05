package vn.hcmute.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hcmute.entity.Video_24133016;
import vn.hcmute.service.IFavoriteService_24133016;
import vn.hcmute.service.IShareService_24133016;
import vn.hcmute.service.IVideoService_24133016;

@Controller
public class VideoDetailController_24133016 {

    @Autowired
    private IVideoService_24133016 videoService;

    @Autowired
    private IShareService_24133016 shareService;

    @Autowired
    private IFavoriteService_24133016 favoriteService;

    @GetMapping("/video/{videoId}")
    public String videoDetail(@PathVariable("videoId") String videoId, Model model) {
        Video_24133016 video = videoService.findById(videoId);
        if (video == null) {
            return "redirect:/home";
        }

        long shareCount = shareService.countByVideoId(videoId);
        long likeCount = favoriteService.countByVideoId(videoId);

        model.addAttribute("video", video);
        model.addAttribute("shareCount", shareCount);
        model.addAttribute("likeCount", likeCount);

        return "video-detail";
    }
}
