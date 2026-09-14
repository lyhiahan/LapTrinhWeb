package vn.iotstar.config;

import java.io.File;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import vn.iotstar.util.Constant;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File uploadFolder = new File("upload").getAbsoluteFile();
        String uploadBasePath = uploadFolder.getAbsolutePath().replace("\\", "/");
        if (!uploadBasePath.endsWith("/")) {
            uploadBasePath += "/";
        }

        File baiTap4Upload = new File("../BaiTap4/upload").getAbsoluteFile();
        String baiTap4Path = baiTap4Upload.getAbsolutePath().replace("\\", "/");
        if (!baiTap4Path.endsWith("/")) {
            baiTap4Path += "/";
        }

        registry.addResourceHandler("/upload/**")
                .addResourceLocations(
                        "file:" + uploadBasePath,
                        "file:" + uploadBasePath + "category/",
                        "file:" + uploadBasePath + "product/",
                        "file:" + uploadBasePath + "avatar/",
                        "file:" + baiTap4Path,
                        "file:" + baiTap4Path + "avatar/",
                        "file:" + baiTap4Path + "product/",
                        "file:d:/LAPTRINH/LAPTRINHWEB/BAITAP/BT/LapTrinhWeb/BaiTap4/upload/",
                        "file:d:/LAPTRINH/LAPTRINHWEB/BAITAP/BT/LapTrinhWeb/BaiTap4/upload/avatar/",
                        "file:upload/",
                        "file:upload/category/",
                        "file:upload/product/",
                        "file:upload/avatar/",
                        "classpath:/static/upload/",
                        "classpath:/static/upload/category/",
                        "classpath:/static/upload/product/",
                        "classpath:/static/upload/avatar/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
