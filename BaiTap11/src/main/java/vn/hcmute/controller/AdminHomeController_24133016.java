package vn.hcmute.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import vn.hcmute.entity.User_24133016;

@Controller
public class AdminHomeController_24133016 {

    @GetMapping("/admin/home")
    public String adminHome(HttpSession session) {
        User_24133016 user = (User_24133016) session.getAttribute("account");
        if (user == null || user.getAdmin() == null || !user.getAdmin()) {
            return "redirect:/login";
        }
        return "admin/home";
    }
}
