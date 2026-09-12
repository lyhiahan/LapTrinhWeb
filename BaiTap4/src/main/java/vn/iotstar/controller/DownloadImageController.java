package vn.iotstar.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.iotstar.util.Constant;

@Controller
public class DownloadImageController {

    @GetMapping("/image")
    public void downloadImage(@RequestParam(name = "fname", required = false) String fileName,
                              HttpServletResponse resp) throws IOException {
        if (fileName == null || fileName.trim().isEmpty() || fileName.trim().equalsIgnoreCase("null")) {
            resp.sendRedirect("https://ui-avatars.com/api/?name=No+Image&background=f1f5f9&color=94a3b8");
            return;
        }

        File file = new File(Constant.DIR + "/" + fileName.trim());
        if (file.exists()) {
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            resp.setContentType(contentType);
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.transferTo(resp.getOutputStream());
            }
        } else {
            resp.sendRedirect("https://ui-avatars.com/api/?name=No+Image&background=f1f5f9&color=94a3b8");
        }
    }
}
